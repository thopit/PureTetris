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

import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.thomas.pure_tetris.Tetris;
import de.thomas.pure_tetris.World;
import de.thomas.pure_tetris.World.ColorType;
import de.thomas.pure_tetris.util.Position;

/**
 * Screen that is shown, while playing the game
 * @author Thomas
 *
 */
public class GameScreen implements Screen, InputProcessor, GestureListener {
	private final Tetris game;
	public static final int BLOCK_SIZE = 48;

	private World world;
	private Map<ColorType, Texture> map;

	private OrthographicCamera camera;

	private Texture boxI;
	private Texture boxJ;
	private Texture boxL;
	private Texture boxO;
	private Texture boxS;
	private Texture boxT;
	private Texture boxZ;
	private Texture grid;

	private boolean showGrid;
	private boolean paused;
	
	private boolean movingFast;
	private float moveTimer;
	
	private boolean alternateHandling = false;
	

	BitmapFont font;

	public GameScreen(Tetris game) {
		this.game = game;
		world = new World(this, game);
		showGrid = true;
		paused = false;
		movingFast = false;
		moveTimer = 0;

		loadTextures();

		font = new BitmapFont(Gdx.files.internal("arialBold40.fnt"), false);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);


		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			GestureDetector gd = new GestureDetector(this);
			Gdx.input.setInputProcessor(gd);
		}
		else {
			Gdx.input.setInputProcessor(this);
		}
		

		initMap();
	}

	private void loadTextures() {
		boxI = new Texture(Gdx.files.internal("boxI.png"));
		boxJ = new Texture(Gdx.files.internal("boxJ.png"));
		boxL = new Texture(Gdx.files.internal("boxL.png"));
		boxO = new Texture(Gdx.files.internal("boxO.png"));
		boxS = new Texture(Gdx.files.internal("boxS.png"));
		boxT = new Texture(Gdx.files.internal("boxT.png"));
		boxZ = new Texture(Gdx.files.internal("boxZ.png"));
		grid = new Texture(Gdx.files.internal("grid.png"));
	}

	private void initMap() {
		map = new TreeMap<ColorType, Texture>();
		map.put(ColorType.I, boxI);
		map.put(ColorType.J, boxJ);
		map.put(ColorType.L, boxL);
		map.put(ColorType.O, boxO);
		map.put(ColorType.S, boxS);
		map.put(ColorType.T, boxT);
		map.put(ColorType.Z, boxZ);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();

		if (showGrid)
			game.batch.draw(grid, 0, 0);
		
		drawCurrentPiece();
		drawBackgroundPieces();


		font.draw(game.batch,  "Level: " + world.getLevel(), 10, 795);
		
		String score = "Score: " + world.getScore();
		font.draw(game.batch, score, 480 - font.getBounds(score).width - 10, 795);

		game.batch.end();

		if (! paused) {
			world.update(delta);

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				world.handleMoveLeft();
			}

			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				world.handleMoveRight();
			}
		}
		
		if (moveTimer > 0) {
			moveTimer--;
		}


		if (alternateHandling) {
			if (Gdx.app.getType().equals(ApplicationType.Android)) {
				float roll = Gdx.input.getRoll();

				if (roll < -10) {
					world.handleMoveLeft();
				}
				else if (roll > 10) {
					world.handleMoveRight();
				}

				float pitch = Gdx.input.getPitch();

				if (movingFast == false && world.getCurrentPiece().getPos().y < 14 && pitch < -15 && Math.abs(roll) < 10) {
					world.handleMoveDownFast();
					movingFast = true;
				}
				else if (pitch >= -15){
					world.handleMoveDownNormal();
					movingFast = false;
				}
			}
		}

	}

	private void drawCurrentPiece() {
		for (Position p :  world.getCurrentPiece().getBoxes()) {
			game.batch.draw(map.get(world.getCurrentPiece().getColorType()), 
					world.getCurrentPiece().getPos().x * BLOCK_SIZE + p.x * BLOCK_SIZE, 
					world.getCurrentPiece().getPos().y * BLOCK_SIZE + p.y * BLOCK_SIZE);
		}
	}

	private void drawBackgroundPieces() {
		for (int x = 0; x < world.getBackground().length; x++) {
			for (int y = 0; y < world.getBackground()[x].length; y++) {
				if (world.getBackground()[x][y].x) {
					game.batch.draw(map.get(world.getBackground()[x][y].y), x * BLOCK_SIZE, y * BLOCK_SIZE);
				}
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
		boxI.dispose();
		boxJ.dispose();
		boxL.dispose();
		boxO.dispose();
		boxS.dispose();
		boxT.dispose();
		boxZ.dispose();
		font.dispose();
		grid.dispose();

		font.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.UP && world.isUpReleased() && ! paused) {
			world.handleRotation();
		}
		if (keycode == Input.Keys.DOWN && world.isDownFastPossible() && ! paused) {
			world.handleMoveDownFast();
		}
		if (keycode == Input.Keys.SPACE) {
			if (paused) {
				paused = false;
			}
			else {
				paused = true;
			}
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.UP && ! paused) {
			world.setUpReleased();
		}
		if (keycode == Input.Keys.DOWN && ! paused) {
			world.setDownReleased();
			world.handleMoveDownNormal();
		}

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		world.handleMoveDownNormal();
		movingFast = false;
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

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		Vector3 touchPos = new Vector3();
		touchPos.set(x, y, 0);
		camera.unproject(touchPos);

		int posX = world.screenToWorld(touchPos.x);
		int posY = world.screenToWorld(touchPos.y);
		
		if (posY < 3 
				&& world.getCurrentPiece().getPos().y > 4 
				&& world.getCurrentPiece().getPos().y < 14
				&& ! world.currentPieceTouchCollision(x, posY, 2)
				&& ! movingFast) {
			world.handleMoveDownFast();
			movingFast = true;
		}
		else if (posY >= 4 && posY < 13) {
			if (posX < world.getCurrentPiece().getPos().x) {
				world.handleMoveLeft();
			}
			else if (posX >  world.getCurrentPiece().getPos().x) {
				world.handleMoveRight();
			}
		}
		else if (posY >= 13){
			world.handleRotation();
		}
		
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		world.handleMoveDownNormal();
		movingFast = false;
		
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

}
