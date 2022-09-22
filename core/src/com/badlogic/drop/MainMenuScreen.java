package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;


public class MainMenuScreen implements Screen {

    final Drop game;

    OrthographicCamera camera;

    public MainMenuScreen(final Drop gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }

            ScreenUtils.clear(0, 0, 0.2f, 1);

            camera.update();
            game.batch.setProjectionMatrix(camera.combined);

            game.batch.begin();
            game.batch.draw(game.img, 0,0);
            game.batch.end();

            if (Gdx.input.isTouched()) {
                game.setScreen(new GameScreen(game));
                dispose();
            }

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to the EggWars!!! ", 50, 460);
        game.font.draw(game.batch, "Tap anywhere to begin!", 50, 430);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
