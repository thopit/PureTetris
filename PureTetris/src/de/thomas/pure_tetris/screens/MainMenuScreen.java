// Copyright (c) 2014  Thomas Opitz
//  
// This file is part of PureTetris.
//
// PureTetris is free software: you can redistribute it and/or modify it 
// under the terms of the GNU General Public License as published by 
// the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
//
// PureTetris is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
// without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with PureTetris.
// If not, see http://www.gnu.org/licenses/.
//
//  e-mail : opitz-t@web.de
//
// *************************************************************************

package de.thomas.pure_tetris.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.thomas.pure_tetris.Tetris;

/**
 * Screen that is shown while viewing the main-menu
 * @author Thomas Opitz
 *
 */
public class MainMenuScreen implements Screen, InputProcessor {
	private final Tetris game;
	private OrthographicCamera camera;
	private BitmapFont font;
	private BitmapFont authorFont;
	private boolean startPossible;
	ShapeRenderer shapeRenderer;


	public MainMenuScreen(final Tetris game, boolean startPossible) {
		this.game = game;
		
		

		font = new BitmapFont(Gdx.files.internal("arialBold40.fnt"), false);
		authorFont = new BitmapFont(Gdx.files.internal("arialBold32.fnt"), false);


		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		
		Gdx.input.setInputProcessor(this);

		this.startPossible = startPossible;
		
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		
		/*
		int distance = 10;
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.box(480 / 2 - font.getBounds(play).width / 2 - 10, 500 - font.getBounds(play).height - 10, 0, 
				font.getBounds(play).width + 20, font.getBounds(play).height + 20, 0);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.box(480 / 2 - font.getBounds(options).width / 2 - 10, 400, 0, 
				font.getBounds(options).width + 20, font.getBounds(options).height + 20, 0);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.box(480 / 2 - font.getBounds(highscore).width / 2 - 10, 300, 0, 
				font.getBounds(highscore).width + 20, font.getBounds(highscore).height + 20, 0);
		shapeRenderer.end();
		*/
		

		game.batch.begin();
		
		
		
		
		
		String pureTetris = "PureTetris";
		font.draw(game.batch, pureTetris, 480 / 2 - font.getBounds(pureTetris).width / 2, 750);
		
		String play = "Play";
		font.draw(game.batch, play, 480 / 2 - font.getBounds(play).width / 2, 550);
		String options = "Options";
		font.draw(game.batch, options, 480 / 2 - font.getBounds(options).width / 2, 450);
		String highscore = "Highscore";
		font.draw(game.batch, highscore, 480 / 2 - font.getBounds(highscore).width / 2, 350);
		
		/*
		String welcome = "Welcome to Tetris!";
		font.draw(game.batch, welcome, 480 / 2 - font.getBounds(welcome).width / 2, 500);
		
		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			String message = "Tap anywhere to begin";
			font.draw(game.batch, message, 480 / 2 - font.getBounds(message).width / 2, 400);
		}
		else {
			String message = "Press any key to begin";
			font.draw(game.batch, message, 480 / 2 - font.getBounds(message).width / 2, 400);
		}
		*/



		authorFont.draw(game.batch, "(c) Thomas Opitz", 200, 40);

		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
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
		font.dispose();
		authorFont.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (! Gdx.app.getType().equals(ApplicationType.Android))
			startGame();
		
		return true;
	}
	
	private void startGame() {
		game.setScreen(new GameScreen(game));
		dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (startPossible)
			startGame();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		startPossible = true;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}