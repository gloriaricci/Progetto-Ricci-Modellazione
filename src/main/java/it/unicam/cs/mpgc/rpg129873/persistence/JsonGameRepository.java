package it.unicam.cs.mpgc.rpg129873.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg129873.game.GameState;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

/**
 * Salva l'unico checkpoint in un file JSON (via Gson). La scrittura è sicura contro
 * le interruzioni: il salvataggio precedente non viene mai lasciato a metà.
 */
public class JsonGameRepository implements GameRepository {

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonGameRepository(Path file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public void save(GameState state) {
        // Si scrive prima su un file temporaneo accanto a quello vero e poi lo si sposta
        // al suo posto: se il programma si interrompe durante la scrittura, il vecchio
        // salvataggio resta intatto.
        Path temporary = file.resolveSibling(file.getFileName() + ".tmp");
        try {
            try (Writer writer = Files.newBufferedWriter(temporary)) {
                gson.toJson(state, writer);
            }
            replaceFileWith(temporary);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    /**
     * Un file mancante, con JSON malformato o con valori rifiutati da {@link GameState}
     * conta come "nessun salvataggio". Solo un vero errore di lettura del disco
     * viene propagato come {@link UncheckedIOException}.
     */
    public Optional<GameState> load() {
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        try (Reader reader = Files.newBufferedReader(file)) {
            return Optional.ofNullable(gson.fromJson(reader, GameState.class));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (RuntimeException e) {
            // JSON non valido o valori fuori intervallo (il costruttore di GameState li rifiuta):
            // un file rovinato non deve impedire di giocare, si riparte da zero.
            return Optional.empty();
        }
    }

    @Override
    public void clear() {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void replaceFileWith(Path temporary) throws IOException {
        // ATOMIC_MOVE rende la sostituzione tutto-o-niente. Non tutti i file system lo
        // supportano: in quel caso si ripiega su una sostituzione normale.
        try {
            Files.move(temporary, file,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

