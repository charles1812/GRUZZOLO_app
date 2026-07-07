# Gruzzolo — app Android (variante utenti)

App Android per gestire e analizzare il proprio portafoglio (ETF e azioni).
Architettura **single-file WebView**: tutta la logica sta in
`app/src/main/assets/index.html`; il guscio nativo Java aggiunge i dati live
(scaricandoli **nativamente**, senza problemi di CORS) e le notifiche del
Replay/PAC anche ad app chiusa.

> **Fonte dati: backend proprio.** Questa build non parla direttamente con
> Yahoo: interroga un **backend PHP/MySQL** (cartella `backend/` del repo,
> archivio `gruzzolo-backend.zip`) che fa da cache verso Yahoo e serve
> quotazioni, storico (fino a 48 mesi) e notizie a tutte le app. L'URL del
> backend è già impostato di default e si può cambiare in *Impostazioni →
> Server dati*.

## Funzioni
Portafoglio multi-valuta con conversione in EUR · prezzi live · P&L totale,
giornaliero e settimanale · grafici storico per titolo (fino a 48 mesi) ·
notizie · Statistica (rischio, volatilità, Sharpe, drawdown, VaR/CVaR,
concentrazione, Monte Carlo, correlazione, contributo al rischio, economia
globale) · Predizione matematica + AI con scommesse auto-verificanti ·
Replay/PAC con notifiche · analisi AI opzionale (chiave Anthropic/OpenAI) ·
modalità privacy.

## Specifiche
- minSdk 28, targetSdk 34, compileSdk 34
- Java 17 (incluso in Android Studio)
- Linguaggio: Java, niente Kotlin (con `resolutionStrategy` per evitare il
  conflitto della kotlin-stdlib che WorkManager si porta dietro)

## Come compilarla
1. **Android Studio** → *Open* → cartella `GruzzoloAndroid`.
2. Attendi il sync Gradle (rigenera il wrapper mancante e scarica le
   dipendenze; serve connessione la prima volta).
3. Collega il telefono o avvia un emulatore → **Run**.
   Da terminale, dopo il sync: `./gradlew assembleDebug`
   (APK in `app/build/outputs/apk/debug/`).
4. Al primo avvio l'app chiede il permesso notifiche (Android 13+).

## Le due parti "delicate"
- **Dati senza CORS**: le richieste GET remote (backend: quotazioni, storico,
  notizie) sono intercettate in `MainActivity` e scaricate **nativamente**
  (`fetchNatively`), così la WebView non viene mai bloccata dalla CORS. Le
  chiamate AI (POST) restano invece nella WebView.
- **Certificato del dominio di prova**: il dominio temporaneo
  `*.hostingersite.com` presenta una catena SSL incompleta che il client
  nativo di Android rifiuta (il browser è più permissivo). `MainActivity`
  accetta il certificato **solo per quell'host** (`trustAllFactory`).
  ⚠️ È un workaround: **per la distribuzione usa un tuo dominio con SSL
  valido (o Cloudflare)** e togli questa parte — la connessione torna
  pienamente verificata.

## Backend
Vedi `backend/README.md`. Carica i PHP in `public_html/api/`, metti le
credenziali del database in `config.php` (non sovrascriverlo con quello
d'esempio!), pannello su `…/api/admin.php`.

## Disclaimer
Progetto personale, scopo dimostrativo/didattico. **Non è consulenza
finanziaria.** I dati possono essere ritardati o imprecisi.
