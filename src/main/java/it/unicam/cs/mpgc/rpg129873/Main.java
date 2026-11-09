package it.unicam.cs.mpgc.rpg129873;

import it.unicam.cs.mpgc.rpg129873.controller.ConsoleEventListener;
import it.unicam.cs.mpgc.rpg129873.controller.GameController;

public class Main {
    public static void main(String[] args) {
        GameController controller = new GameController(new ConsoleEventListener());
        controller.playerAttack();
    }
}
