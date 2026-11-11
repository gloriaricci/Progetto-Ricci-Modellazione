package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.controller.GameController;
import it.unicam.cs.mpgc.rpg129873.game.Game;
import it.unicam.cs.mpgc.rpg129873.game.GameFactory;
import it.unicam.cs.mpgc.rpg129873.persistence.GameRepository;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class AppNavigator {
    private static final double WIDTH = 960;
    private static final double HEIGHT = 540;

    private final Stage stage;
    private final GameRepository repository;
    private final GameFactory factory;
    private final GuiEventListener listener;

    public AppNavigator(Stage stage, GameRepository repository,
                        GameFactory factory, GuiEventListener listener) {
        this.stage = Objects.requireNonNull(stage);
        this.repository = Objects.requireNonNull(repository);
        this.factory = Objects.requireNonNull(factory);
        this.listener = Objects.requireNonNull(listener);
    }

    public void showMenu() {
        MenuView menu = new MenuView(repository.load().isPresent(), this::play, this::restart);
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
        GameView view = new GameView(controller, this::showMenu,
                () -> showEnd(controller.getStatus().won()));
        listener.setHandler(view::appendMessage);
        controller.start();
        view.refresh();
        show(view.getRoot());
    }

    private void showEnd(boolean won){
        EndView ens = won
                ? new EndView("Vittoria!", "Nox, hai sconfitto il barone dello shimmer e liberato la città!", Color.GOLD, this::showMenu)
                : new EndView("GAME OVER", "Nox, sei stato sconfitto! Riprova dall'inizio del gioco.", Color.CRIMSON, this::showMenu);
        show(ens.getRoot());
    }

    private void show(Parent root) {
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
    }
}
