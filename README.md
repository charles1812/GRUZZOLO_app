# Gruzzolo 📈

App Android per **gestire e analizzare il proprio portafoglio di investimenti** (ETF e azioni): prezzi live, grafici, statistiche di rischio, previsioni e notizie — tutto in un'unica interfaccia, semplice e in italiano.

🔗 **Repository:** https://github.com/charles1812/GRUZZOLO_test_live

---

## 🔴 Prova la demo dal browser

### 👉 https://charles1812.github.io/GRUZZOLO_test_live/

Nessuna installazione, nessun account: la demo parte già con un **portafoglio di esempio** e usa **dati simulati**, così puoi vedere tutte le schermate funzionanti — portafoglio, grafici, statistiche, previsioni e notizie.

> Se lo vuoi vedere offline, apri la demo anche così:
> `https://htmlpreview.github.io/?https://raw.githubusercontent.com/charles1812/GRUZZOLO_test_live/main/docs/index.html`


---

## Cos'è

Gruzzolo è un'app Android leggera per tenere sotto controllo i propri investimenti. È costruita come **single-file WebView**: tutta l'interfaccia e la logica stanno in un unico file, mentre un piccolo guscio nativo si occupa di scaricare i dati live e delle notifiche. I dati arrivano da un backend che fa da cache verso Yahoo Finance (nella demo pubblica, invece, sono simulati).

## Cosa fa

- **Portafoglio** multi-valuta (con conversione automatica in EUR) e prezzi live
- **Profitti/perdite** totali, **giornalieri** e **settimanali**
- **Grafici** dello storico di ogni titolo (fino a 48 mesi)
- **Notizie** per titolo
- **Statistica**: punteggio di rischio, volatilità, indice di Sharpe, drawdown massimo, VaR/CVaR, concentrazione, **simulazioni Monte Carlo**, mappa di correlazione e contributo al rischio; più una panoramica di **economia globale**
- **Predizione**: proiezione del valore nel tempo con un modello **matematico** e uno basato su **AI**, con un sistema di **scommesse** che si verificano da sole alla scadenza
- **Piano di accumulo (PAC)** con promemoria degli acquisti ricorrenti
- **Analisi AI** opzionale e **modalità privacy** per nascondere gli importi

## Come usarla

- **Solo guardare l'interfaccia** → apri la [demo](https://charles1812.github.io/GRUZZOLO_test_live/).
- **Installarla sul telefono** → apri la cartella del progetto in **Android Studio** e premi *Run* (oppure `./gradlew assembleDebug`). Requisiti: Android 9+ (minSdk 28), Java 17.

## Tecnologie

Android (Java, WebView) · HTML/CSS/JavaScript vanilla (single-file) · backend PHP 8 + MySQL come cache dati · nessun framework pesante.

## Disclaimer

Progetto personale a scopo **dimostrativo e didattico**. **Non è consulenza finanziaria.** I dati possono essere ritardati o imprecisi; nella demo sono del tutto simulati.
