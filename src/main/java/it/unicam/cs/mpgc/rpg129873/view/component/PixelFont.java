package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;

public final class PixelFont {

    private static final String RESOURCE = "/fonts/PressStart2P-Regular.ttf";
    private static final double DEFAULT_SIZE = 12;

    private PixelFont() {
    }

    public static boolean load() {
        try (InputStream in = PixelFont.class.getResourceAsStream(RESOURCE)) {
            return in != null && Font.loadFont(in, DEFAULT_SIZE) != null;
        } catch (IOException e) {
            return false;
        }
    }
}
