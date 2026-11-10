package it.unicam.cs.mpgc.rpg129873.persistence;

import it.unicam.cs.mpgc.rpg129873.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JsonGameRepositoryTest {

    @TempDir
    Path tempDir;

    private Path file;
    private JsonGameRepository repository;

    @BeforeEach
    void setUp() {
        file = tempDir.resolve("save.json");
        repository = new JsonGameRepository(file);
    }

    @Test
    void loadWithoutASaveFileIsEmpty() {
        assertTrue(repository.load().isEmpty());
    }

    @Test
    void savedStateCanBeLoadedBack() {
        GameState state = new GameState(3, 70, 1);

        repository.save(state);

        assertTrue(Files.exists(file));
        assertEquals(Optional.of(state), repository.load());
    }

    @Test
    void savingAgainOverwritesThePreviousState() {
        repository.save(new GameState(2, 80, 2));
        repository.save(new GameState(4, 60, 1));

        assertEquals(Optional.of(new GameState(4, 60, 1)), repository.load());
    }

    @Test
    void aCorruptedFileIsTreatedAsNoSave() throws IOException {
        Files.writeString(file, "questo non e' un json valido");

        assertTrue(repository.load().isEmpty());
    }
}