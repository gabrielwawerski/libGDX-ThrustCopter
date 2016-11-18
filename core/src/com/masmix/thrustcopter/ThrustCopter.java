package com.masmix.thrustcopter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ThrustCopter extends ApplicationAdapter {
	private SpriteBatch batch;
    private FPSLogger fpsLogger;
    private OrthographicCamera camera;
    private Texture background;
    private float terrainOffset;
    private TextureRegion terrainAbove;
    private TextureRegion terrainBelow;
    private float bgOffset;
    private Animation plane;
    private float planeAnimTime;

    private Vector2 planeVelocity;
    private Vector2 planePosition;
    private Vector2 planeDefaultPos;
    private Vector2 gravity;

    private static final Vector2 DAMPING = new Vector2(0.99f, 0.99f);

	@Override
	public void create () {
		batch = new SpriteBatch();
        fpsLogger = new FPSLogger();
        camera = new OrthographicCamera();
        background = new Texture("background.png");
        terrainAbove = new TextureRegion(new Texture("terrainBelow.png"));
        terrainBelow = new TextureRegion(terrainAbove);
        plane = new Animation(0.045f, new TextureRegion(new Texture("plane1.png")),
                new TextureRegion(new Texture("plane2.png")),
                new TextureRegion(new Texture("plane3.png")));
        plane.setPlayMode(Animation.PlayMode.LOOP);
        planeAnimTime = 0;

        planeVelocity = new Vector2();
        planePosition = new Vector2();
        planeDefaultPos = new Vector2();
        gravity = new Vector2();

        terrainAbove.flip(true, true);
        camera.setToOrtho(false, 800, 400);
        resetScene();
	}

	@Override
	public void render () {
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

        if (terrainOffset * -1 > terrainBelow.getRegionWidth()) {
            terrainOffset = 0;
        }

        if (terrainOffset > 0) {
            terrainOffset -= terrainBelow.getRegionWidth();
        }
    }

    private void drawScene() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

    batch.begin();
        batch.disableBlending();
        batch.draw(background,bgOffset , 0);
        batch.draw(background,bgOffset + background.getWidth() , 0);
        batch.enableBlending();


//        batch.draw(plane.getKeyFrame(planeAnimTime), 350, 200);
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
