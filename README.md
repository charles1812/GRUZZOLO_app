# Gruzzolo

App Android per gestire e analizzare il proprio portafoglio di investimenti (ETF e azioni), pensata come **single-file WebView**: tutta l'interfaccia e la logica stanno in un unico `index.html`, un guscio nativo Java aggiunge i prezzi live senza problemi di CORS e le notifiche anche ad app chiusa.

## 🔴 Demo dal vivo (interfaccia e funzionamento)

La cartella [`docs/`](docs/) contiene una **demo con dati simulati** (nessun backend, nessuna rete): mostra tutte le schermate funzionanti direttamente dal browser.

**Opzione A — GitHub Pages (consigliata):**
1. Vai su *Settings → Pages*.
2. In *Build and deployment → Source* scegli **Deploy from a branch**, branch `main`, cartella **`/docs`**, poi *Save*.
3. Dopo qualche minuto la demo è online su `https://TUO-UTENTE.github.io/NOME-REPO/`.

**Opzione B — anteprima istantanea senza Pages:**
apri
`https://htmlpreview.github.io/?https://raw.githubusercontent.com/TUO-UTENTE/NOME-REPO/main/docs/index.html`

> La demo parte già con un portafoglio di esempio (SpaceX, Leonardo, MSCI World, EM IMI) e prezzi/grafici/notizie simulati. Le funzioni AI sono disattivate (servirebbe una chiave).

## Cosa fa

- **Portafoglio**: posizioni con quantità, prezzo medio e valuta; conversione automatica in EUR.
- **Prezzi live** e **P&L** totale, **giornaliero** e **settimanale**.
- **Grafici** dello storico per ogni titolo (fino a **48 mesi**).
- **Notizie** per titolo.
- **Statistica**: punteggio di rischio, volatilità, Sharpe, max drawdown, VaR/CVaR, concentrazione (HHI), **Monte Carlo** di portafoglio e per singola posizione, mappa di correlazione, contributo al rischio; più una sezione **Economia globale** (clima di mercato, ETF principali).
- **Predizione**: proiezione valore-nel-tempo con modello **matematico** (Monte Carlo) e modello **AI**, con sistema di **scommesse** che si auto-verificano alla data prevista.
- **Replay/PAC**: promemoria degli acquisti ricorrenti, anche ad app chiusa.
- **Analisi AI** (opzionale, con chiave Anthropic/OpenAI).
- **Modalità privacy** per nascondere gli importi.

## Architettura

L'app esiste in due varianti, stessa interfaccia:

| Variante | Fonte dati | A chi serve |
|---|---|---|
| **dev** (`GruzzoloDevAndroid`) | Yahoo Finance diretto (proxy nativo nel guscio) | uso personale dello sviluppatore |
| **utenti** (`GruzzoloAndroid`) | **backend PHP/MySQL** che fa da cache verso Yahoo | distribuzione ad altri |

Nella variante utenti un piccolo backend su hosting condiviso interroga Yahoo **una volta** per ticker e serve i dati a tutte le app (cache di quotazioni, storico e notizie), con **pannello admin** e contatori. Il codice del backend è nell'archivio `gruzzolo-backend.zip`.

## Struttura del repo (suggerita)

```
/docs/index.html      → demo dal vivo (dati simulati) per GitHub Pages
/GruzzoloAndroid/      → progetto Android "utenti" (Android Studio)
/GruzzoloDevAndroid/   → progetto Android "dev"
/backend/              → backend PHP/MySQL (da gruzzolo-backend.zip)
```

L'app è un unico file: tutta la logica sta in `app/src/main/assets/index.html`.

## Come compilare l'app

1. Apri **Android Studio** → *Open* → scegli `GruzzoloAndroid` (o `GruzzoloDevAndroid`).
2. Lascia completare il sync Gradle (rigenera il wrapper e scarica le dipendenze).
3. Collega il telefono o avvia un emulatore → **Run**. Da terminale: `./gradlew assembleDebug`.

Requisiti: minSdk 28, targetSdk 34, Java 17. Solo Java, niente Kotlin.

## Backend (variante utenti)

Vedi `backend/README.md`. In sintesi: carica i file PHP in `public_html/api/` su hosting con PHP 8 + MySQL, inserisci le credenziali del database in `config.php`, imposta l'URL del backend nell'app. Pannello: `…/api/admin.php`.

> ⚠️ **Nota SSL**: il dominio di prova `*.hostingersite.com` serve una catena di certificati incompleta che il client nativo di Android rifiuta. La build utenti include un workaround limitato a quell'host. **Per la distribuzione usa un tuo dominio con SSL valido (o Cloudflare)** e rimuovi il workaround: la connessione torna pienamente verificata.

## Disclaimer

Progetto personale a scopo dimostrativo/didattico. **Non è consulenza finanziaria.** I dati possono essere ritardati o imprecisi; nella demo sono del tutto simulati.
