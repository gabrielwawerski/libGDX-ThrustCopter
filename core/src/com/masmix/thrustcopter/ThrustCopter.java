package com.masmix.thrustcopter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
 * Created by MasmiX on 01.12.2016.
 */
public class ThrustCopter extends Game {
    public static final int screenWidth = 800;
    public static final int screenHeight = 400;
    public FPSLogger fpsLogger;
    public OrthographicCamera camera;
    public Viewport viewport;
    public TextureAtlas textureAtlas;
    public SpriteBatch batch;
    private AssetManager manager;

    public ThrustCopter() {
        fpsLogger = new FPSLogger();
        camera = new OrthographicCamera();
        camera.position.set(screenWidth / 2, screenHeight / 2, 0);
        viewport = new FitViewport(screenWidth, screenHeight, camera);
    }

    @Override
    public void create() {
        manager = new AssetManager();

        manager.load("packed.pack", TextureAtlas.class);
        manager.finishLoading();

        batch = new SpriteBatch();
        textureAtlas = manager.get("packed.pack", TextureAtlas.class);

        setScreen(new ThrustCopterScene(this));
    }

    @Override
    public void render() {
        fpsLogger.log();
        super.render();
    }

    @Override
    public void resize(int screenWidth, int screenHeight) {
        viewport.update(screenWidth, screenHeight);
    }

    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();
    }
}
