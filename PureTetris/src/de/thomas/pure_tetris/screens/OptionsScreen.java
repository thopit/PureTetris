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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.thomas.pure_tetris.Options;
import de.thomas.pure_tetris.Tetris;

/**
 * Screen that is shown, while viewing the highscore
 * @author Thomas Opitz
 *
 */
public class OptionsScreen implements Screen, InputProcessor {
	private OrthographicCamera camera;
	private Tetris game;
	private BitmapFont font;
	private int menuPosition = 0;

	public OptionsScreen(final Tetris game) {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		this.game = game;
		font = new BitmapFont(Gdx.files.internal("arialBold40.fnt"), false);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();

		font.setColor(Color.WHITE);

		String options = "Options";
		font.draw(game.batch, options, 480 / 2 - font.getBounds(options).width / 2, 780);

		if (menuPosition == 0)
			font.setColor(14, 59, 240, 1);
		else
			font.setColor(Color.WHITE);

		String sG = "Show Grid";
		font.draw(game.batch, sG, 80, 550);

		font.setColor(Color.WHITE);
		if (Options.showGrid)
			font.draw(game.batch, "Yes", 80 + font.getBounds(sG).width + 30, 550);
		else
			font.draw(game.batch, "No", 80 + font.getBounds(sG).width + 30, 550);

		if (menuPosition == 1)
			font.setColor(14, 59, 240, 1);
		else
			font.setColor(Color.WHITE);

		String sL = "Start Level";
		font.draw(game.batch, sL, 80, 450);

		font.setColor(Color.WHITE);
		font.draw(game.batch, String.valueOf(Options.startLevel), 80 + font.getBounds(sL).width + 30, 450);

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
		if (keycode == Input.Keys.UP)
			menuPosition = (menuPosition + 3) % 2;
		if (keycode == Input.Keys.DOWN)
			menuPosition = (menuPosition + 1) % 2;

		if (menuPosition == 0 && (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT || keycode == Input.Keys.ENTER))
			swapGrid();

		if (menuPosition == 1) {
			if (keycode == Input.Keys.LEFT) {
				int startLevel = (Options.startLevel + 10) % 11;

				if (startLevel == 0)
					startLevel = 10;

				Options.startLevel = startLevel;
			}
			else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.ENTER) {
				int startLevel = (Options.startLevel + 1) % 11;

				if (startLevel == 0)
					startLevel = 1;

				Options.startLevel = startLevel;
			}
		}

		if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACKSPACE) {
			saveOptions();
			startMainMenu();
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
		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			saveOptions();
			startMainMenu();
		}

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

	private void startMainMenu() {
		game.setScreen(new MainMenuScreen(game, false, 1));
		dispose();
	}
	
	private void saveOptions() {
		FileHandle file = Gdx.files.local("options.txt");
		
		if (file.exists()) {
			file.writeString(Options.showGrid + String.format("%n") + Options.startLevel, false);
		}
	}

	private void swapGrid() {
		if (Options.showGrid)
			Options.showGrid = false;
		else
			Options.showGrid = true;
	}

}
