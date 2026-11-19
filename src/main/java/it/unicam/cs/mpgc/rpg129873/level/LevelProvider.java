package it.unicam.cs.mpgc.rpg129873.level;

/**
 * Punto di estensione per i livelli: fornisce i livelli numerati da 1 a
 * {@link #getTotalLevels()}. {@code Game} e {@code GameFactory} dipendono solo da questa
 * interfaccia, quindi i livelli possono arrivare da codice, da un file o da altro
 * senza modificare le regole di gioco.
 */
public interface LevelProvider {
    /**
     * Ogni chiamata deve restituire un livello nuovo, con nemici a vita piena e al primo
     * scontro: i nemici sono oggetti modificabili e riusarli porterebbe in una nuova
     * partita i danni della precedente. Un numero fuori intervallo va rifiutato
     * ({@code LevelFactory} lancia {@link IllegalArgumentException}).
     * <p>
     * {@code GameFactory.restore} la usa anche per rileggere le ricompense dei livelli già superati.
     */
    Level createLevel(int number);

    int getTotalLevels();
}
