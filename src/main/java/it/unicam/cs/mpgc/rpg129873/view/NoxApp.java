package it.unicam.cs.mpgc.rpg129873.view;

import it.unicam.cs.mpgc.rpg129873.game.GameFactory;
import it.unicam.cs.mpgc.rpg129873.level.LevelFactory;
import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129873.persistence.JsonGameRepository;
import it.unicam.cs.mpgc.rpg129873.view.component.BackgroundProvider;
import it.unicam.cs.mpgc.rpg129873.view.component.PixelFont;
import it.unicam.cs.mpgc.rpg129873.view.component.ResourceBackgroundProvider;
import it.unicam.cs.mpgc.rpg129873.view.component.sprites.ResourceSpriteProvider;
import it.unicam.cs.mpgc.rpg129873.view.component.sprites.SpriteProvider;
import javafx.application.Application;
import javafx.stage.Stage;

import java.nio.file.Path;

public class NoxApp extends Application {

    @Override
    public void start(Stage stage) {
        PixelFont.load();

        GuiEventListener listener = new GuiEventListener();
        LevelProvider levels = new LevelFactory();
        GameRepository repository = new JsonGameRepository(Path.of("nox-save.json"));
        GameFactory factory = new GameFactory(levels,listener);
        BackgroundProvider backgrounds = new ResourceBackgroundProvider();
        SpriteProvider sprites = new ResourceSpriteProvider();

        AppNavigator navigator = new AppNavigator(stage, repository, factory, listener, backgrounds,sprites);

        stage.setTitle("Nox");
        navigator.showMenu();
        stage.show();
    }
}
