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

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

import de.thomas.pure_tetris.Tetris;
import de.thomas.pure_tetris.util.Score;

/**
 * Screen that is shown, when the player has lost
 * @author Thomas Opitz
 *
 */
public class GameOverScreen implements Screen, TextInputListener {
	private OrthographicCamera camera;
	private Tetris game;
	private BitmapFont font;
	private int score;
	private float wait;
	private boolean askForText;
	private boolean notGoodEnough;

	public GameOverScreen(final Tetris game, int score) {
		wait = 0;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		this.game = game;
		this.score = score;
		askForText = false;
		font = new BitmapFont(Gdx.files.internal("arialBold40.fnt"), false);
		
		Array<Score> scores = new Array<Score>();
		
		
		FileHandle file = Gdx.files.local("scores.txt");
		
		
		if (! file.exists())
			file.writeString("", true);
		
		
		scores = GameOverScreen.readScores(file);
		
		notGoodEnough = false;

		if (scores.size >= 10 && score <= scores.get(scores.size - 1).y)
			notGoodEnough = true;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		String gameOver = "Game Over!";
		font.draw(game.batch, gameOver, 480 / 2 - font.getBounds(gameOver).width / 2, 550);
		
		String scoreStr =  "Your score is " + score;
		font.draw(game.batch, scoreStr, 480 / 2 - font.getBounds(scoreStr).width / 2, 450);

		game.batch.end();

		wait += delta;

		if (wait > 1) {
			if (! askForText && ! notGoodEnough && score > 0) {
				Gdx.input.getTextInput(this, "Enter your name", "Player");
				askForText = true;
			}
			else if (score <= 0 || notGoodEnough) {
				game.setScreen(new HighScoreScreen(game));
				dispose();
			}
		}

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
	public void input(String text) {
		text = text.substring(0, Math.min(text.length(), 10));

		FileHandle file = Gdx.files.local("scores.txt");

		Array<Score> scores = readScores(file);

		if (scores.size > 0) {
			boolean isLast = true;

			for (int x = 0; x < scores.size && x < 10; x++) {
				if (score > scores.get(x).y) {
					scores.insert(x, new Score(text, score));
					isLast = false;
					break;
				}
			}

			if (isLast)
				scores.add(new Score(text, score));
			
			try {
				file.writer(false).write("");
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}


			file = Gdx.files.local("scores.txt");
			
			int amount = 0;
			for (Score s : scores) {
				if (amount++ >= 10)
					break;
				
				file.writeString(s.x + " " + s.y + "\n", true);
			}
		}
		else {
			file.writeString(text + " " + score + "\n", true);
		}

		game.setScreen(new HighScoreScreen(game));
		dispose();
	}

	public static Array<Score> readScores(FileHandle file) {
		Array<Score> scores = new Array<Score>();

		Scanner scanner = new Scanner(file.readString());
		scanner.useDelimiter("\n");
		String pattern = "(.+) (\\d+)";
		int amount = 0;

		while (scanner.hasNext() && amount++ < 10) {
			String string = scanner.next();
			String playerName = string.replaceAll(pattern, "$1");
			int score = 0;
			try {
				score = Integer.parseInt(string.replaceAll(pattern, "$2"));
			}
			catch (NumberFormatException e) {
				System.err.println("Error reading scores");
			}

			scores.add(new Score(playerName, score));
		}
		scanner.close();

		return scores;
	}

	@Override
	public void canceled() {
		game.setScreen(new HighScoreScreen(game));
		dispose();
	}

}
