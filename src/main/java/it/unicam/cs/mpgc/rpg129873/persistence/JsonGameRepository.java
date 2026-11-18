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

public class JsonGameRepository implements GameRepository {

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonGameRepository(Path file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public void save(GameState state) {
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
    public Optional<GameState> load() {
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        try (Reader reader = Files.newBufferedReader(file)) {
            return Optional.ofNullable(gson.fromJson(reader, GameState.class));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (RuntimeException e) {

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
        try {
            Files.move(temporary, file,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

