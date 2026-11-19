package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.controller.*;
import it.unicam.cs.mpgc.rpg129873.game.GameEvent;
import it.unicam.cs.mpgc.rpg129873.game.GameStatus;
import it.unicam.cs.mpgc.rpg129873.view.component.*;
import it.unicam.cs.mpgc.rpg129873.view.component.sprites.SpriteProvider;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

/**
 * Schermata di combattimento. Segue il flusso del gioco in due modi:
 * <ul>
 * <li>{@link #onEvent}: riceve gli eventi, li scrive nel log, avvia le animazioni e mette in
 * coda gli annunci a tutto schermo, mostrati uno alla volta;</li>
 * <li>{@link #refresh}: rilegge lo stato e aggiorna barre, sprite e pulsanti.</li>
 * </ul>
 * Dopo ogni azione del giocatore la risposta del nemico parte dopo una breve pausa
 * ({@link EnemyTurnScheduler}), così l'utente vede prima la propria mossa. Se il nemico deve ancora rispondere,
 * c'è un annuncio a schermo o la partita è finita, i pulsanti restano bloccati.
 * A partita finita avvisa chi l'ha creata ({@code onFinished}) dopo una breve attesa, una sola volta.
 */
public class GameView {

    private static final double END_DELAY_MS = 1500;
    private static final int MAX_LOG_LINES = 3;
    private static final int SPRITE_SCALE = 3;
    private static final double SPRITE_SLOT_HEIGHT = 200;

    private final GameController controller;
    private final Runnable onFinished;
    private final EventTextFormatter formatter = new EventTextFormatter();
    private final EventAnimationMapper animations = new EventAnimationMapper();
    private final Queue<Announcement> announcements = new ArrayDeque<>();
    private final StackPane root = new StackPane();
    private final LevelBackground background;
    private final OverlayView overlay;
    private final Label levelLabel = new Label();
    private final FighterPanel heroPanel;
    private final FighterPanel enemyPanel;
    private final ActionBar actionBar;
    private final MessageLog log = new MessageLog(MAX_LOG_LINES);
    private final EnemyTurnScheduler enemyTurns;
    private final DelayedTask endDelay = new DelayedTask();
    private boolean endScheduled = false;

    public GameView(GameController controller, BackgroundProvider backgrounds,
                    SpriteProvider sprites, Runnable onBackToMenu, Runnable onFinished) {
        this.controller = Objects.requireNonNull(controller);
        this.onFinished = Objects.requireNonNull(onFinished);
        Objects.requireNonNull(onBackToMenu);
        this.background = new LevelBackground(Objects.requireNonNull(backgrounds));
        Objects.requireNonNull(sprites);

        this.heroPanel = new FighterPanel(sprites, Color.STEELBLUE, SPRITE_SCALE,
                SPRITE_SLOT_HEIGHT, "hero-name");
        this.enemyPanel = new FighterPanel(sprites, Color.DARKRED, SPRITE_SCALE,
                SPRITE_SLOT_HEIGHT, "enemy-name");
        this.overlay = new OverlayView(this::showNextAnnouncement);
        this.enemyTurns = new EnemyTurnScheduler(controller::isEnemyTurnPending, () -> {
            controller.onEnemyTurn();
            refresh();
        });
        this.actionBar = new ActionBar(controller.getStatus().maxPotions(),
                () -> act(controller::onAttack),
                () -> act(controller::onPotion),
                () -> act(controller::onAbility),
                () -> {
                    cancelTimers();
                    onBackToMenu.run();
                });

        levelLabel.getStyleClass().add("level-label");
        BorderPane.setAlignment(levelLabel, Pos.CENTER);
        BorderPane.setMargin(levelLabel, new Insets(16, 0, 0, 0));

        HBox fighters = new HBox(120, heroPanel, enemyPanel);
        fighters.setAlignment(Pos.CENTER);
        fighters.setFillHeight(false);

        VBox bottom = new VBox(12, log, actionBar);
        bottom.setPadding(new Insets(16));

        BorderPane content = new BorderPane();
        content.setTop(levelLabel);
        content.setCenter(fighters);
        content.setBottom(bottom);

        root.getChildren().addAll(background, content, overlay);
    }

    public Parent getRoot() {
        return root;
    }

    public void onEvent(GameEvent event) {
        log.add(formatter.format(event));
        animations.cueFor(event).ifPresent(this::playCue);
        if (formatter.isAnnouncement(event)) {
            announcements.add(formatter.toAnnouncement(event));
            if (!overlay.isShowing()) {
                showNextAnnouncement();
            }
        }
    }

    public void refresh() {
        GameStatus status = controller.getStatus();
        // Il giocatore può agire solo se la partita è in corso, il nemico non deve
        // ancora rispondere e non c'è un annuncio da chiudere.
        boolean locked = status.finished()
                || controller.isEnemyTurnPending()
                || overlay.isShowing();

        background.setLevel(status.levelNumber());
        levelLabel.setText("Livello " + status.levelNumber());
        heroPanel.show(status.heroId(), status.heroName(), status.heroHp(), status.heroMaxHp());
        enemyPanel.show(status.enemyId(), status.enemyName(), status.enemyHp(), status.enemyMaxHp());
        actionBar.update(status.potions(), status.abilityAvailable(), locked);

        if (status.finished() && !endScheduled) {
            endScheduled = true;
            endDelay.schedule(END_DELAY_MS, onFinished);
        }
    }

    private void playCue(AnimationCue cue) {
        FighterPanel target = cue.actor() == AnimationCue.Actor.HERO ? heroPanel : enemyPanel;
        target.play(cue.animation());
    }

    /**
     * Mostra il prossimo annuncio in coda. Quando non ce ne sono più (all'ultimo click), aggiorna la
     * schermata e fa partire la risposta del nemico, che era stata rimandata durante gli annunci.
     */
    private void showNextAnnouncement() {
        Announcement next = announcements.poll();
        if (next != null) {
            overlay.show(next.title(), next.subtitle());
        } else {
            refresh();
            enemyTurns.scheduleIfNeeded(overlay.isShowing());
        }
    }

    private void act(Runnable action) {
        action.run();
        refresh();
        enemyTurns.scheduleIfNeeded(overlay.isShowing());
    }

    private void cancelTimers() {
        enemyTurns.cancel();
        endDelay.cancel();
    }
}

