package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.controller.Announcement;
import it.unicam.cs.mpgc.rpg129873.controller.EventTextFormatter;
import it.unicam.cs.mpgc.rpg129873.controller.GameController;
import it.unicam.cs.mpgc.rpg129873.game.GameEvent;
import it.unicam.cs.mpgc.rpg129873.game.GameStatus;
import it.unicam.cs.mpgc.rpg129873.view.component.HealthBar;
import it.unicam.cs.mpgc.rpg129873.view.component.OverlayView;
import it.unicam.cs.mpgc.rpg129873.view.component.PotionIndicator;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public class GameView {

    private static final double ENEMY_DELAY_MS = 700;
    private static final double END_DELAY_MS = 1500;
    private static final int MAX_POTIONS = 2;
    private static final double BAR_WIDTH = 240;
    private static final double BAR_HEIGHT = 24;
    private static final double SPRITE_SIZE = 160;
    private static final double ABILITY_BUTTON_SIZE = 100;
    private static final String ABILITY_STYLE =
            "-fx-background-color: gold; -fx-text-fill: black; -fx-font-size: 10px;"
                    + "-fx-border-color: black; -fx-border-width: 3;"
                    + "-fx-background-radius: 50; -fx-border-radius: 50;";

    private final GameController controller;
    private final Runnable onFinished;
    private final EventTextFormatter formatter = new EventTextFormatter();
    private final Queue<Announcement> announcements = new ArrayDeque<>();
    private final StackPane root = new StackPane();
    private final BorderPane content = new BorderPane();
    private final OverlayView overlay;
    private final Label levelLabel = new Label();
    private final Label heroNameLabel = new Label();
    private final Label enemyNameLabel = new Label();
    private final HealthBar heroBar = new HealthBar(BAR_WIDTH, BAR_HEIGHT);
    private final HealthBar enemyBar = new HealthBar(BAR_WIDTH, BAR_HEIGHT);
    private final PotionIndicator potionIndicator;
    private final Button attackButton = new Button("Attacco");
    private final Button abilityButton = new Button("Abilità");
    private final TextArea log = new TextArea();
    private PauseTransition enemyPause;
    private PauseTransition endPause;
    private boolean endScheduled = false;

    public GameView(GameController controller, Runnable onBackToMenu, Runnable onFinished) {
        this.controller = Objects.requireNonNull(controller);
        this.onFinished = Objects.requireNonNull(onFinished);
        this.potionIndicator = new PotionIndicator(MAX_POTIONS, () -> act(controller::onPotion));
        this.overlay = new OverlayView(this::showNextAnnouncement);

        attackButton.setOnAction(e -> act(controller::onAttack));
        abilityButton.setOnAction(e -> act(controller::onAbility));
        abilityButton.setStyle(ABILITY_STYLE);
        abilityButton.setMinSize(ABILITY_BUTTON_SIZE, ABILITY_BUTTON_SIZE);

        Button menuButton = new Button("Menu");
        menuButton.setOnAction(e -> {
            stopPauses();
            onBackToMenu.run();
        });

        log.setEditable(false);
        log.setWrapText(true);
        log.setPrefHeight(110);
        log.setStyle("-fx-font-size: 10px;");

        levelLabel.setStyle("-fx-font-size: 16px;");
        BorderPane.setAlignment(levelLabel, Pos.CENTER);
        BorderPane.setMargin(levelLabel, new Insets(16, 0, 0, 0));

        HBox fighters = new HBox(120,
                fighterPanel(heroNameLabel, heroBar, Color.STEELBLUE),
                fighterPanel(enemyNameLabel, enemyBar, Color.DARKRED));
        fighters.setAlignment(Pos.CENTER);

        HBox buttons = new HBox(24, attackButton, potionIndicator, abilityButton, menuButton);
        buttons.setAlignment(Pos.CENTER);

        VBox bottom = new VBox(12, log, buttons);
        bottom.setPadding(new Insets(16));

        content.setTop(levelLabel);
        content.setCenter(fighters);
        content.setBottom(bottom);

        root.getChildren().addAll(content, overlay);
    }

    public Parent getRoot() {
        return root;
    }

    public void onEvent(GameEvent event) {
        appendLog(formatter.format(event));
        if (formatter.isAnnouncement(event)) {
            announcements.add(formatter.toAnnouncement(event));
            if (!overlay.isShowing()) {
                showNextAnnouncement();
            }
        }
    }

    public void refresh() {
        GameStatus status = controller.getStatus();
        boolean locked = status.finished()
                || controller.isEnemyTurnPending()
                || overlay.isShowing();

        levelLabel.setText("Livello " + status.levelNumber());
        heroNameLabel.setText(status.heroName());
        enemyNameLabel.setText(status.enemyName());
        heroBar.update(status.heroHp(), status.heroMaxHp());
        enemyBar.update(status.enemyHp(), status.enemyMaxHp());

        attackButton.setDisable(locked);
        potionIndicator.update(status.potions(), !locked);

        abilityButton.setVisible(status.abilityAvailable());
        abilityButton.setManaged(status.abilityAvailable());
        abilityButton.setDisable(locked);

        if (status.finished() && !endScheduled) {
            endScheduled = true;
            scheduleEnd();
        }
    }

    private void appendLog(String message) {
        log.appendText(message + System.lineSeparator());
    }

    private void showNextAnnouncement() {
        Announcement next = announcements.poll();
        if (next != null) {
            overlay.show(next.title(), next.subtitle());
        } else {
            refresh();
            scheduleEnemyTurnIfNeeded();
        }
    }

    private VBox fighterPanel(Label name, HealthBar bar, Color spriteColor) {
        Rectangle sprite = new Rectangle(SPRITE_SIZE, SPRITE_SIZE, spriteColor);
        sprite.setStroke(Color.BLACK);
        sprite.setStrokeWidth(4);
        name.setStyle("-fx-font-size: 12px;");

        VBox panel = new VBox(10, name, bar, new StackPane(sprite));
        panel.setAlignment(Pos.CENTER);
        return panel;
    }

    private void scheduleEnemyTurnIfNeeded() {
        boolean alreadyScheduled = enemyPause != null
                && enemyPause.getStatus() == Animation.Status.RUNNING;
        if (!controller.isEnemyTurnPending() || overlay.isShowing() || alreadyScheduled) {
            return;
        }
        enemyPause = new PauseTransition(Duration.millis(ENEMY_DELAY_MS));
        enemyPause.setOnFinished(e -> {
            controller.onEnemyTurn();
            refresh();
        });
        enemyPause.play();
    }

    private void scheduleEnd() {
        endPause = new PauseTransition(Duration.millis(END_DELAY_MS));
        endPause.setOnFinished(e -> onFinished.run());
        endPause.play();
    }

    private void stopPauses() {
        if (enemyPause != null) {
            enemyPause.stop();
        }
        if (endPause != null) {
            endPause.stop();
        }
    }

    private void act(Runnable action) {
        action.run();
        refresh();
        scheduleEnemyTurnIfNeeded();
    }
}