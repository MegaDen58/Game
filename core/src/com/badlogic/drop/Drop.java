package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game {
	SpriteBatch batch;
	BitmapFont font;
	Texture img;

	public void create() {
		img = new Texture(("start.png"));
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // важно!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
