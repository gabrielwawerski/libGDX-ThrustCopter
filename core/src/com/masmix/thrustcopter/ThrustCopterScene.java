package com.masmix.thrustcopter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ThrustCopterScene extends ScreenAdapter {
    ThrustCopter game;
	private SpriteBatch batch;
    private Texture background;
    private Camera camera;
    private float terrainOffset;
    private TextureRegion terrainAbove;
    private TextureRegion terrainBelow;
    private float bgOffset;
    private Animation plane;
    private FPSLogger fpsLogger;
    private float planeAnimTime;
    private TextureAtlas textureAtlas;

    private Vector2 planeVelocity;
    private Vector2 planePosition;
    private Vector2 planeDefaultPos;
    private Vector2 gravity;

    private static final Vector2 DAMPING = new Vector2(0.99f, 0.99f);

	public ThrustCopterScene (ThrustCopter thrustCopter) {
	    game = thrustCopter;
	    batch = game.batch;
	    camera = game.camera;
	    textureAtlas = game.textureAtlas;
        batch = game.batch;
        fpsLogger = game.fpsLogger;

        background = new Texture("background.png");
        terrainAbove = new TextureRegion(textureAtlas.findRegion("terrainBelow"));
        terrainBelow = new TextureRegion(textureAtlas.findRegion("terrainBelow"));
        plane = new Animation(0.070f, textureAtlas.findRegion("plane1"),
                textureAtlas.findRegion("plane2"),
                textureAtlas.findRegion("plane3"));
        plane.setPlayMode(Animation.PlayMode.LOOP);
        planeAnimTime = 0;

        planeVelocity = new Vector2();
        planePosition = new Vector2();
        planeDefaultPos = new Vector2();
        gravity = new Vector2();

        terrainAbove.flip(true, true);
//        camera.setToOrtho(false, 800, 400);
        resetScene();
	}

	public void render (float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fpsLogger.log();

        updateScene();
        drawScene();
	}

    private void updateScene() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        planeAnimTime += deltaTime;

        planeVelocity.scl(DAMPING);
        planeVelocity.add(gravity);
        planePosition.mulAdd(planeVelocity, deltaTime);
        terrainOffset -= planePosition.x - planeDefaultPos.x;
        planePosition.x = planeDefaultPos.x;

        if (terrainOffset * -1 > terrainBelow.getRegionWidth())
            terrainOffset = 0;

        if (terrainOffset > 0)
            terrainOffset -= terrainBelow.getRegionWidth();

        if (planePosition.y < -150)
            resetScene();
    }

    private void drawScene() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

    batch.begin();
        batch.disableBlending();
        batch.draw(background, bgOffset, 0);
        batch.draw(background, bgOffset + background.getWidth() , 0);
        batch.enableBlending();

        batch.draw(plane.getKeyFrame(planeAnimTime), planePosition.x, planePosition.y);

        batch.draw(terrainBelow, terrainOffset, 0);
        batch.draw(terrainBelow, terrainOffset + terrainBelow.getRegionWidth(), 0);

        batch.draw(terrainAbove, terrainOffset, 400 - terrainAbove.getRegionHeight());
        batch.draw(terrainAbove, terrainOffset + terrainAbove.getRegionWidth(), 400 - terrainAbove.getRegionHeight());
    batch.end();
    }

    @Override
	public void dispose () {
		batch.dispose();
        background.dispose();
        textureAtlas.dispose();
	}

	private void resetScene() {
        terrainOffset = 0;
        planeAnimTime = 0;
        planeVelocity.set(400, 0);
        gravity.set(0, -4);
        planeDefaultPos.set(400 - 76/2, 240 - 32/2);
        planePosition.set(planeDefaultPos.x, planeDefaultPos.y);
    }
}
