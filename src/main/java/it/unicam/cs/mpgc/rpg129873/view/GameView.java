package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.controller.GameController;
import it.unicam.cs.mpgc.rpg129873.game.GameStatus;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Objects;

public class GameView {

    private static final double ENEMY_DELAY_MS = 700;

    private final GameController controller;
    private final BorderPane root = new BorderPane();
    private final Label levelLabel = new Label();
    private final Label heroLabel = new Label();
    private final Label enemyLabel = new Label();
    private final Button attackButton = new Button("Attacco");
    private final Button potionButton = new Button("Pozione");
    private final Button abilityButton = new Button("Abilità");
    private final TextArea log = new TextArea();
    private PauseTransition enemyPause;

    public GameView(GameController controller, Runnable onBackToMenu) {
        this.controller = Objects.requireNonNull(controller);

        attackButton.setOnAction(e -> act(controller::onAttack));
        potionButton.setOnAction(e -> act(controller::onPotion));
        abilityButton.setOnAction(e -> act(controller::onAbility));

        Button menuButton = new Button("Menu");
        menuButton.setOnAction(e -> {
                    if (enemyPause != null){
                        enemyPause.stop();
                    }
                    onBackToMenu.run();
                });

        log.setEditable(false);
        log.setWrapText(true);

        VBox top = new VBox(8, levelLabel, heroLabel, enemyLabel);
        top.setPadding(new Insets(16));

        HBox buttons = new HBox(12, attackButton, potionButton, abilityButton, menuButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(16));

        root.setTop(top);
        root.setCenter(log);
        root.setBottom(buttons);
    }

    public Parent getRoot() {
        return root;
    }

    public void appendMessage(String message) {
        log.appendText(message + System.lineSeparator());
    }

    public void refresh() {
        GameStatus status = controller.getStatus();
        levelLabel.setText("Livello " + status.levelNumber());
        heroLabel.setText(status.heroName() + ": " + status.heroHp() + "/" + status.heroMaxHp()
                + "   Pozioni: " + status.potions());
        enemyLabel.setText(status.enemyName() + ": " + status.enemyHp() + "/" + status.enemyMaxHp());

        boolean locked = status.finished() || controller.isEnemyTurnPending();
        attackButton.setDisable(status.finished());
        potionButton.setDisable(status.finished() || status.potions() == 0);
        abilityButton.setDisable(status.finished() || !status.abilityAvailable());
    }

    private void scheduleEnemyTurn(){
        enemyPause = new PauseTransition(Duration.millis(ENEMY_DELAY_MS));
        enemyPause.setOnFinished(e ->{
            controller.onEnemyTurn();
            refresh();
        });
        enemyPause.play();
    }

    private void act(Runnable action) {
        action.run();
        refresh();
        if (controller.isEnemyTurnPending()){
            scheduleEnemyTurn();
        }
    }
}