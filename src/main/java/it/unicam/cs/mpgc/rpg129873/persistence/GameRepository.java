package it.unicam.cs.mpgc.rpg129873.persistence;

import it.unicam.cs.mpgc.rpg129873.game.GameState;

import java.util.Optional;

/**
 * Punto di estensione per la persistenza: chi la implementa decide dove e come si
 * conserva un {@link GameState} (file JSON, database, servizio remoto...). Il resto
 * del programma dipende solo da questa interfaccia, quindi cambiare supporto non
 * richiede modifiche alle regole di gioco. Conserva un solo salvataggio alla volta.
 */

public interface GameRepository {

    void save(GameState state);

    /**
     * @return il salvataggio, oppure {@code Optional.empty()} se non c'è un salvataggio
     * utilizzabile: mai fatto, cancellato, oppure illeggibile o non valido. In tutti questi
     * casi il chiamante si comporta allo stesso modo e inizia una nuova partita.
     */
    Optional<GameState> load();

    void clear();
}
