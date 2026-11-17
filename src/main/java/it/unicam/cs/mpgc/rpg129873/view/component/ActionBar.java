package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class ActionBar extends HBox {

    private static final double SPACING = 24;
    private static final double ABILITY_BUTTON_SIZE = 100;

    private final Button attackButton = new Button("Attacco");
    private final Button abilityButton = new Button("Abilità");
    private final PotionIndicator potionIndicator;

    public ActionBar(int maxPotions, Runnable onAttack, Runnable onPotion,
                     Runnable onAbility, Runnable onMenu) {
        super(SPACING);
        Objects.requireNonNull(onAttack);
        Objects.requireNonNull(onAbility);
        Objects.requireNonNull(onMenu);

        potionIndicator = new PotionIndicator(maxPotions, onPotion);

        attackButton.getStyleClass().addAll("pixel-button", "attack-button");
        attackButton.setOnAction(e -> onAttack.run());

        abilityButton.getStyleClass().add("ability-button");
        abilityButton.setMinSize(ABILITY_BUTTON_SIZE, ABILITY_BUTTON_SIZE);
        abilityButton.setOnAction(e -> onAbility.run());

        Button menuButton = new Button("Menu");
        menuButton.getStyleClass().add("pixel-button");
        menuButton.setOnAction(e -> onMenu.run());

        setAlignment(Pos.CENTER);
        getChildren().addAll(attackButton, potionIndicator, abilityButton, menuButton);
    }

    public void update(int potions, boolean abilityAvailable, boolean locked) {
        attackButton.setDisable(locked);
        potionIndicator.update(potions, !locked);

        abilityButton.setVisible(abilityAvailable);
        abilityButton.setManaged(abilityAvailable);
        abilityButton.setDisable(locked);
    }
}
