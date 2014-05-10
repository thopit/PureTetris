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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

import de.thomas.pure_tetris.Tetris;
import de.thomas.pure_tetris.util.Score;

/**
 * Screen that is shown, while viewing the highscore
 * @author Thomas Opitz
 *
 */
public class HighScoreScreen implements Screen, InputProcessor {
	private OrthographicCamera camera;
	private Tetris game;
	private BitmapFont font;
	Array<Score> scores;


	public HighScoreScreen(final Tetris game) {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		this.game = game;
		font = new BitmapFont(Gdx.files.internal("arialBold40.fnt"), false);

		Gdx.input.setInputProcessor(this);

		FileHandle file = Gdx.files.local("scores.txt");

		if (file.exists())
			scores = GameOverScreen.readScores(file);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		
		String highscores = "Highscores";
		font.draw(game.batch, highscores, 480 / 2 - font.getBounds(highscores).width / 2, 780);

		int y = 600;
		for (Score s : scores) {
			String singleScore = s.x + ": " + s.y;
			font.draw(game.batch, singleScore, 480 / 2 - font.getBounds(singleScore).width / 2, y);
			y -= 50;

		}

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
	}

	@Override
	public boolean keyDown(int keycode) {
		if (! Gdx.app.getType().equals(ApplicationType.Android)) {
			runGame();
		}
		
		return true;
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
		runGame();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
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
	
	private void runGame() {
		game.setScreen(new MainMenuScreen(game, false));
		dispose();
	}

}
