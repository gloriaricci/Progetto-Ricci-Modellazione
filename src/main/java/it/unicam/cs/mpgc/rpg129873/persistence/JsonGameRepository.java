package it.unicam.cs.mpgc.rpg129873.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg129873.game.GameState;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class JsonGameRepository implements GameRepository{

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonGameRepository(Path file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public void save(GameState state) {
        try (Writer writer = Files.newBufferedWriter(file)) {
            gson.toJson(state, writer);
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
}

