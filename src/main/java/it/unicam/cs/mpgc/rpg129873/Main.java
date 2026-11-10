package it.unicam.cs.mpgc.rpg129873;

import it.unicam.cs.mpgc.rpg129873.controller.ConsoleEventListener;
import it.unicam.cs.mpgc.rpg129873.controller.GameController;
import it.unicam.cs.mpgc.rpg129873.controller.GameEventListener;
import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.game.GameFactory;
import it.unicam.cs.mpgc.rpg129873.level.LevelFactory;
import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129873.persistence.JsonGameRepository;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameEventListener listener = new ConsoleEventListener();
        LevelProvider levels = new LevelFactory();
        GameRepository repository = new JsonGameRepository(Path.of("nox-save.json"));
        GameFactory factory = new GameFactory(levels, listener);

        Game game = repository.load().map(factory::restore).orElseGet(factory::newGame);
        GameController controller = new GameController(game, repository);
        controller.start();

        Scanner in = new Scanner(System.in);
        while (!controller.isFinished() && in.hasNextLine()) {
            switch (in.nextLine().trim()) {
                case "a" -> controller.onAttack();
                case "p" -> controller.onPotion();
                case "f" -> controller.onAbility();
                case "q" -> { return; }
                default -> System.out.println("Comandi: a = attacco, p = pozione, f = abilità, q = esci");
            }
        }
    }
}
