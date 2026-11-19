package it.unicam.cs.mpgc.rpg129873.view.component;

import it.unicam.cs.mpgc.rpg129873.view.component.sprites.SpriteProvider;
import it.unicam.cs.mpgc.rpg129873.view.component.sprites.SpriteView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Nome, barra della vita e sprite di un combattente. Lo sprite sta in uno spazio di altezza
 * fissa, appoggiato in basso: così nomi e barre restano allineate anche se gli sprite
 * hanno dimensioni diverse.
 */
public class FighterPanel extends VBox {

    private static final double SPACING = 10;
    private static final double BAR_WIDTH = 240;
    private static final double BAR_HEIGHT = 24;

    private final Label nameLabel = new Label();
    private final HealthBar healthBar = new HealthBar(BAR_WIDTH, BAR_HEIGHT);
    private final SpriteView spriteView;

    public FighterPanel(SpriteProvider sprites, Color fallbackColor, int spriteScale,
                        double slotHeight, String nameStyleClass) {
        super(SPACING);
        this.spriteView = new SpriteView(sprites, fallbackColor, spriteScale);
        nameLabel.getStyleClass().add(nameStyleClass);

        StackPane slot = new StackPane(spriteView);
        slot.setMinHeight(slotHeight);
        slot.setPrefHeight(slotHeight);
        slot.setMaxHeight(slotHeight);
        StackPane.setAlignment(spriteView, Pos.BOTTOM_CENTER);

        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(nameLabel, healthBar, slot);
    }

    public void show(String spriteId, String name, int hp, int maxHp) {
        nameLabel.setText(name);
        healthBar.update(hp, maxHp);
        spriteView.setSprite(spriteId);
    }

    public void play(String animation) {
        spriteView.play(animation);
    }
}
