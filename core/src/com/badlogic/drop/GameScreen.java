package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class GameScreen implements Screen {
    final Drop game;

    Texture Egg1;
    Texture wolfImage;
    Texture Egg2;
    Sound dropSound;
    Texture Egg3;
    TextureRegion backGround;
    Music backMusic;
    Rectangle wolf;
    Texture dogImage;
    OrthographicCamera camera;
    long lastDropTime;
    int dropsGathered = 0;
    int type = 0;
    Texture[] textures;
    Array<Donate> eggDrop;

    static class Donate{
        int index;
        Rectangle rectangle;
        public Donate(Rectangle rectangle, int index){
            this.index = index;
            this.rectangle = rectangle;
        }
    }

    public GameScreen(final Drop gam) {
        this.game = gam;

        backGround = new TextureRegion(new Texture("background.png"),0,0,1440,800);

        Egg1 = new Texture(Gdx.files.internal("egg1.png"));
        Egg2 = new Texture(Gdx.files.internal("egg2.png"));
        Egg3 = new Texture(Gdx.files.internal("egg3.png"));
        dogImage = new Texture(Gdx.files.internal("dog.png"));
        textures = new Texture[]{Egg1, Egg2, Egg3, dogImage};
        wolfImage = new Texture(Gdx.files.internal("wolf.png"));

        dropSound = Gdx.audio.newSound(Gdx.files.internal("thanks.wav"));
        backMusic = Gdx.audio.newMusic(Gdx.files.internal("backmus.mp3"));

        backMusic.setLooping(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);


        wolf = new Rectangle();
        wolf.x = 800 / 2 - 64 / 2;
        wolf.y = 20;
        wolf.width = 64;
        wolf.height = 64;

        eggDrop = new Array<>();
        spawnRaindrop();

    }

    private void spawnRaindrop() {

        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        if(MathUtils.randomBoolean(0.25f)){
            type = 0;
        }
        else if(MathUtils.randomBoolean(0.2f)){
            type = 1;
        }
        else if(MathUtils.randomBoolean(0.25f)){
            type = 2;
        }
        else if(MathUtils.randomBoolean(0.3f)){
            type = 3;
        }
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        eggDrop.add(new Donate(raindrop, type));
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backGround,0,0);
        game.font.draw(game.batch, "Eggs Collected: " + dropsGathered, 330, 480);
        game.batch.draw(wolfImage, wolf.x, wolf.y);
        for (Donate raindrop : eggDrop) {
            game.batch.draw(textures[raindrop.index], raindrop.rectangle.x, raindrop.rectangle.y);
        }
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            wolf.x = touchPos.x - 64 / 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            wolf.x -= 550 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            wolf.x += 550 * Gdx.graphics.getDeltaTime();

        if (wolf.x < 0)
            wolf.x = 0;
        if (wolf.x > 800 - 64)
            wolf.x = 800 - 64;

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        Iterator<Donate> iter = eggDrop.iterator();
        while (iter.hasNext()) {
            Donate donate = iter.next();
            donate.rectangle.y -= 200 * Gdx.graphics.getDeltaTime();
            if(dropsGathered > 2){
                donate.rectangle.y -= 200 * Gdx.graphics.getDeltaTime();
            }
            if(dropsGathered > 4){
                donate.rectangle.y -= 250 * Gdx.graphics.getDeltaTime();
            }
            if(dropsGathered > 6){
                donate.rectangle.y -= 300 * Gdx.graphics.getDeltaTime();
            }
            if(dropsGathered > 8){
                donate.rectangle.y -= 350 * Gdx.graphics.getDeltaTime();
            }
            if(dropsGathered > 10){
                donate.rectangle.y -= 400 * Gdx.graphics.getDeltaTime();
            }
            if (donate.rectangle.y + 64 < 0 && donate.index != 3){
                iter.remove();
                dropsGathered = 0;
            }
            if(dropsGathered == 4){
                game.setScreen(new Win(game));
                dispose();
            }
            if (donate.rectangle.overlaps(wolf)) {
                if(donate.index == 3 && (dropsGathered >= 10)){
                    dropsGathered -= 10;
                }
                else if(donate.index == 3 && (dropsGathered <= 9)){
                    dropsGathered = 0;
                }
                else if (donate.index != 3){
                    dropsGathered++;
                }
                dropSound.play();
                iter.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        backMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        Egg3.dispose();
        Egg2.dispose();
        Egg1.dispose();
        wolfImage.dispose();
        dropSound.dispose();
        backMusic.dispose();
    }
}
