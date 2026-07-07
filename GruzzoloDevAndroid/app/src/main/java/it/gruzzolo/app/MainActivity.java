package it.gruzzolo.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.webkit.WebViewAssetLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String APP_URL = "https://appassets.androidplatform.net/assets/index.html";
    private WebView web;
    private WebViewAssetLoader assetLoader;
    private final ExecutorService io = Executors.newFixedThreadPool(3);

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        web = new WebView(this);
        setContentView(web);

        assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);   // enables localStorage used by the app
        s.setDatabaseEnabled(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        web.addJavascriptInterface(new WebAppInterface(this), "GruzzoloNative");

        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // 1) serve the bundled web app over an https origin
                WebResourceResponse local = assetLoader.shouldInterceptRequest(request.getUrl());
                if (local != null) return local;

                // 2) proxy Yahoo Finance natively so the WebView is not blocked by CORS
                String host = request.getUrl().getHost();
                if (host != null && host.contains("finance.yahoo.com")) {
                    return fetchNatively(request.getUrl().toString());
                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String host = uri.getHost();
                // keep the app's own pages inside the WebView
                if (host != null && host.equals("appassets.androidplatform.net")) return false;
                // open external links (news articles) in the system browser
                try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch (Exception ignored) {}
                return true;
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (web.canGoBack()) web.goBack();
                else moveTaskToBack(true);   // keep the app alive instead of killing it
            }
        });

        // Android 13+ needs runtime permission to post notifications
        if (Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        web.loadUrl(APP_URL);
    }

    private WebResourceResponse fetchNatively(String urlStr) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, OPTIONS");
        try {
            URL url = new URL(urlStr);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android) Gruzzolo/1.0");
            c.setRequestProperty("Accept", "application/json,text/plain,*/*");
            c.setConnectTimeout(10000);
            c.setReadTimeout(10000);

            int code = c.getResponseCode();
            InputStream in = (code >= 200 && code < 400) ? c.getInputStream() : c.getErrorStream();
            byte[] body = readAll(in);

            String ct = c.getContentType();
            String mime = (ct != null) ? ct.split(";")[0].trim() : "application/json";
            if (mime.isEmpty()) mime = "application/json";

            return new WebResourceResponse(mime, "utf-8",
                    code > 0 ? code : 200, "OK", headers, new ByteArrayInputStream(body));
        } catch (Exception e) {
            byte[] err = ("{\"error\":\"" + e.getMessage() + "\"}").getBytes();
            return new WebResourceResponse("application/json", "utf-8",
                    502, "Bad Gateway", headers, new ByteArrayInputStream(err));
        }
    }

    private static byte[] readAll(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
        in.close();
        return out.toByteArray();
    }
}
