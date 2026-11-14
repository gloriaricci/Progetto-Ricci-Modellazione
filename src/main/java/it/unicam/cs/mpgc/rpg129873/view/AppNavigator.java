package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.controller.GameController;
import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.game.GameFactory;
import it.unicam.cs.mpgc.rpg129873.game.GameStatus;
import it.unicam.cs.mpgc.rpg129873.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129873.view.component.BackgroundProvider;
import it.unicam.cs.mpgc.rpg129873.view.component.sprites.SpriteProvider;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class AppNavigator {
    private static final double WIDTH = 960;
    private static final double HEIGHT = 540;
    private static final String STYLESHEET = "/pixel.css";

    private final Stage stage;
    private final GameRepository repository;
    private final GameFactory factory;
    private final GuiEventListener listener;
    private final BackgroundProvider backgrounds;
    private final SpriteProvider sprites;

    public AppNavigator(Stage stage, GameRepository repository, GameFactory factory, GuiEventListener listener, BackgroundProvider backgrounds, SpriteProvider sprites) {
        this.stage = Objects.requireNonNull(stage);
        this.repository = Objects.requireNonNull(repository);
        this.factory = Objects.requireNonNull(factory);
        this.listener = Objects.requireNonNull(listener);
        this.backgrounds = Objects.requireNonNull(backgrounds);
        this.sprites = Objects.requireNonNull(sprites);
    }

    public void showMenu() {
        MenuView menu = new MenuView(repository.load().isPresent(), backgrounds, this::play, this::restart);
        show(menu.getRoot());
    }

    private void play() {
        startGame(repository.load().map(factory::restore).orElseGet(factory::newGame));
    }

    private void restart() {
        repository.clear();
        startGame(factory.newGame());
    }

    private void startGame(Game game) {
        GameController controller = new GameController(game, repository);
        GameView view = new GameView(controller, backgrounds, sprites, this::showMenu,
                () -> showEnd(controller.getStatus()));
        listener.setHandler(view::onEvent);
        controller.start();
        view.refresh();
        show(view.getRoot());
    }

    private void showEnd(GameStatus status) {
        EndView end = status.won()
                ? new EndView("Vittoria!",
                "Nox, hai sconfitto il barone dello shimmer e liberato la città!",
                "end-title-victory", status.levelNumber(), backgrounds, this::showMenu)
                : new EndView("GAME OVER",
                "Nox, sei stato sconfitto! Riprova dall'inizio del livello.",
                "end-title-defeat", status.levelNumber(), backgrounds, this::showMenu);
        show(end.getRoot());
    }

    private void show(Parent root) {
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        URL stylesheet = AppNavigator.class.getResource(STYLESHEET);
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        }
        stage.setScene(scene);
    }
}

