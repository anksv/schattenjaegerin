package io.github.av.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    Texture backgroundTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Music music;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite bucketSprite; // Declare a new Sprite variable

    Vector2 touchPos;

    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        bucketSprite = new Sprite(bucketTexture); // Initialize the sprite based on the texture
        bucketSprite.setSize(1, 1); // Define the size of the sprite

        touchPos = new Vector2();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        // Keyboard inputs
        // Horizontal movement (X-Axis)
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta); // move the bucket right
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta); // move the bucket left
        }

        // Vertical movement (Y-Axis)
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bucketSprite.translateY(speed * delta); // move the bucket up
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            bucketSprite.translateY(-speed * delta); // move the bucket down
        }

        // Touch input
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            viewport.unproject(touchPos); // Convert the units to the world units of the viewport
            bucketSprite.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
            bucketSprite.setCenterY(touchPos.y);
        }
    }

    private void logic() {

        // Store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // Store the bucket size for brevity
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        // Clamp x to values between 0 and worldWidth
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));
        bucketSprite.setY(MathUtils.clamp(bucketSprite.getY(), 0, worldHeight - bucketHeight));

    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight); // draw the background
        // spriteBatch.draw(bucketTexture, 0, 0, 1, 1); // and then draw the bucket with width/height of 1 meter
        bucketSprite.draw(spriteBatch); // Sprites have their own draw method


        spriteBatch.end();
    }

    @Override
    public void dispose() {

    }
}
