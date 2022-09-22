package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.Stack;


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
    OrthographicCamera camera;
    long lastDropTime;
    int dropsGathered = 0;
    Texture[] textures;
    Array<Donate> eggDrop;

    class Donate{
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
        textures = new Texture[]{Egg1, Egg2, Egg3};
        wolfImage = new Texture(Gdx.files.internal("wolf.png"));

        dropSound = Gdx.audio.newSound(Gdx.files.internal("thanks-_2_.wav"));
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
        int type = 0;
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        if(MathUtils.randomBoolean(0.25f)){
            type = 0;
        }
        else if(MathUtils.randomBoolean(0.25f)){
            type = 1;
        }
        else if(MathUtils.randomBoolean(0.25f)){
            type = 2;
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
            if(dropsGathered < 20){
                donate.rectangle.y -= 200 * Gdx.graphics.getDeltaTime();
            }
            if(dropsGathered >= 20){
                donate.rectangle.y -= 250 * Gdx.graphics.getDeltaTime();
            }
            if(dropsGathered > 40){
                donate.rectangle.y -= 300 * Gdx.graphics.getDeltaTime();
            }
            if(dropsGathered > 60){
                donate.rectangle.y -= 350 * Gdx.graphics.getDeltaTime();
            }
            if(dropsGathered > 100){
                donate.rectangle.y -= 400 * Gdx.graphics.getDeltaTime();
            }
            if (donate.rectangle.y + 64 < 0){
                iter.remove();
                dropsGathered = 0;
            }

            if (donate.rectangle.overlaps(wolf)) {
                dropsGathered++;
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
        // воспроизведение фоновой музыки
        // когда отображается экрана
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
    public static int rnd(int max)
    {
        return (int) (Math.random() * ++max);
    }
}
