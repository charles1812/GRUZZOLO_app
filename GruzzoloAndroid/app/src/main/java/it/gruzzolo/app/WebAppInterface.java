package it.gruzzolo.app;

import android.content.Context;
import android.webkit.JavascriptInterface;

/** Bridge exposed to the web app as window.GruzzoloNative */
public class WebAppInterface {

    private final Context ctx;

    public WebAppInterface(Context c) {
        this.ctx = c.getApplicationContext();
    }

    /** Called by the web app whenever the Replay plan changes or the app opens. */
    @JavascriptInterface
    public void scheduleReminders(int dayOfMonth, String costText, String isoDate) {
        ReminderScheduler.schedule(ctx, dayOfMonth, costText);
    }

    @JavascriptInterface
    public void cancelReminders() {
        ReminderScheduler.cancel(ctx);
    }
}
