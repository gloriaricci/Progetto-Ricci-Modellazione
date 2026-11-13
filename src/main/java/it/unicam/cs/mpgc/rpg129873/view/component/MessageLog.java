package it.unicam.cs.mpgc.rpg129873.view.component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class MessageLog extends VBox {

    private static final double LINE_SPACING = 4;
    private static final double LINE_HEIGHT = 16;
    private static final double MAX_TEXT_WIDTH = 900;
    private static final double OLDEST_OPACITY = 0.45;

    private final int maxLines;

    public MessageLog(int maxLines) {
        super(LINE_SPACING);
        if (maxLines < 1) {
            throw new IllegalArgumentException("maxLines must be at least 1");
        }
        this.maxLines = maxLines;
        setAlignment(Pos.BOTTOM_CENTER);
        setMinHeight(maxLines * (LINE_HEIGHT + LINE_SPACING));
        setMouseTransparent(true);
    }

    public void add(String message) {
        Label line = new Label(message);
        line.getStyleClass().add("log-line");
        line.setWrapText(true);
        line.setMaxWidth(MAX_TEXT_WIDTH);
        line.setTextAlignment(TextAlignment.CENTER);
        line.setAlignment(Pos.CENTER);

        getChildren().add(line);
        while (getChildren().size() > maxLines) {
            getChildren().remove(0);
        }
        fadeOlderLines();
    }

    private void fadeOlderLines() {
        List<Node> lines = getChildren();
        int count = lines.size();
        for (int i = 0; i < count; i++) {
            int age = count - 1 - i;
            double step = maxLines > 1 ? (1 - OLDEST_OPACITY) / (maxLines - 1) : 0;
            lines.get(i).setOpacity(1 - age * step);
        }
    }
}
