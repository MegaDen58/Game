package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class Win implements Screen {

    Drop drop;
    TextureRegion backgroundTexture;
    OrthographicCamera camera;

    Win(Drop drop){
        this.drop = drop;
        backgroundTexture = new TextureRegion(new Texture("win.png"), 0, 0, 800, 480);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        drop.batch.setProjectionMatrix(camera.combined);
        drop.batch.begin();
        drop.batch.draw(backgroundTexture, 0, 0);

        drop.batch.end();
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
