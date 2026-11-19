# Nox

Nox è un gioco di ruolo a turni in Java con interfaccia grafica JavaFX. La protagonista, Nox, attraversa 5 livelli e affronta un boss alla fine di ciascuno, fino al creatore dello Shimmer. Non ci sono inventario né negozi: ogni nuova arma o abilità sostituisce la precedente.

Progetto per il corso di Metodologie di Programmazione (Unicam, AA 2025/26).

## Requisiti

- Java 25 (se non è installato, Gradle prova a scaricarlo da solo)
- Non serve installare Gradle: nel repository c'è il wrapper

## Come eseguire il progetto

```bash
git clone https://github.com/gloriaricci/Progetto-Ricci-Modellazione.git
cd Progetto-Ricci-Modellazione
./gradlew build
./gradlew run
```

Per lanciare solo i test: `./gradlew test`

## Come si gioca

- **Menu**: "Inizia a giocare" riprende dall'ultimo checkpoint (salvato all'inizio di ogni livello nel file `nox-save.json`) oppure parte da zero se non c'è un salvataggio; "Ricomincia il gioco" cancella il salvataggio e riparte dal livello 1.
- **Attacco**: attacca il nemico con l'arma corrente.
- **Pozioni**: due cerchi che curano Nox; diventano grigi quando sono usati.
- **Abilità**: il pulsante compare quando Nox ha un'abilità da attivare.

## Documentazione

La descrizione completa del progetto (funzionalità, responsabilità, classi e interfacce, persistenza, estensibilità) è nella [Wiki del repository](https://github.com/gloriaricci/Progetto-Ricci-Modellazione/wiki).

## Uso di strumenti di AI

Ho usato strumenti di AI, nello specifico Claude e Gemini, come supporto per:

- modifiche e rifiniture del codice;
- la scrittura dei commenti Javadoc;
- l'utilizzo della grafica (font, sfondi e sprite) nel gioco.
- una prima base per i testi della Wiki.

La dichiarazione dettagliata è nella [Wiki](https://github.com/gloriaricci/Progetto-Ricci-Modellazione/wiki).

## Crediti

- Font: Press Start 2P (CodeMan38), licenza SIL Open Font License 1.1. Il testo della licenza è in `src/main/resources/fonts/OFL.txt`.
- Sprite e sfondi: scaricati gratuitamente da internet e da itch.io e dichiarati liberamente utilizzabili; non è disponibile l'indicazione di autore o licenza.






