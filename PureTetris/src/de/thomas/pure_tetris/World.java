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

package de.thomas.pure_tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;

import de.thomas.tetris.pieces.*;
import de.thomas.tetris.screens.GameOverScreen;
import de.thomas.tetris.screens.GameScreen;
import de.thomas.tetris.util.Position;
import de.thomas.tetris.util.BackTuple;

/**
 * Contains and controls the state of the game
 * @author Thomas Opitz
 *
 */
public class World {
	private float moveTimeFix;
	private float actionTimeFix;
	private float moveTimeSave;
	
	private boolean leftReleased;
	private boolean rightReleased;
	private boolean upReleased;
	private boolean downReleased;

	
	private Piece currentPiece;
	private BackTuple[][] backGround;
	private float moveTime;
	private float actionTime;
	private boolean backGroundCollision;
	
	public final float FAST_MOVE_TIME;

	public static enum ColorType { I, J, L, O, S, T, Z };
	private int level;
	private int score;
	private int scoreToNextLevel;
	
	private boolean downFastPossible;
	
	private GameScreen screen;
	private Tetris game;
	
	boolean testMode = false;
	boolean debug = false;

	public World(final GameScreen screen, final Tetris game) {
		moveTime = 0;
		actionTime = 0;
		moveTimeFix = 0.7f;
		moveTimeSave = moveTimeFix;
		actionTimeFix = 0.1f;
		backGround = new BackTuple[10][17];
		backGroundCollision = false;
		leftReleased = true;
		rightReleased = true;
		upReleased = true;
		downReleased = true;
		level = 1;
		score = 0;
		downFastPossible = true;
		if (testMode)
			scoreToNextLevel = 1;
		else 
			scoreToNextLevel = 150;
		
		
		if (Gdx.app.getType().equals(ApplicationType.Android))
			FAST_MOVE_TIME = 0.06f;
		else
			FAST_MOVE_TIME = 0.03f;
		
		this.screen = screen;
		this.game = game;

		for (int x = 0; x < backGround.length; x++) {
			backGround[x] = new BackTuple[17];

			for (int y = 0; y < backGround[x].length; y++) {
				backGround[x][y] = new BackTuple(false, ColorType.I);
			}
		}

		currentPiece = createRandomPiece();
	}

	public void update(float delta) {
		checkCurrentPieceBackgroundCollision();
		checkTimers(delta);
		checkLineComplete();
		checkGameOver();
	}

	private void checkTimers(float delta) {
		if (moveTime <= 0) {
			if (! backGroundCollision) {
				currentPiece.getPos().y -= 1;
				moveTime = moveTimeFix;
			}
			else {
				handleCurrentPieceBackgroundCollision();
				backGroundCollision = false;
			}
		}

		if (actionTime > 0) {
			actionTime -= delta;
		}
		else {
			actionTime = 0;
		}

		if (moveTime > 0) {
			moveTime -= delta;
		}
		else {
			moveTime = 0;
		}
	}

	private void checkLineComplete() {
		int amount = 1;
		
		for (int y = 0; y < backGround[0].length; y++) {
			boolean lineComplete = true;

			for (int x = 0; x < backGround.length; x++) {
				if (! backGround[x][y].x) {
					lineComplete = false;
					break;
				}
			}

			if (lineComplete) {
				handleLineComplete(y, amount++);
				y--;
			}
		}
	}

	private void handleLineComplete(int completeY, int amount) {
		for (int y = completeY; y < backGround[0].length - 1; y++) {
			for (int x = 0; x < backGround.length; x++) {
				backGround[x][y] = backGround[x][y + 1];
			}
		}
		
		handleScore(amount);
	}
	
	private void handleScore(int amount) {
		score += level * 10 * amount;
		
		if (score > scoreToNextLevel && level < 10) {
			level++;
			
			if (moveTimeFix == FAST_MOVE_TIME) {
				moveTimeFix = moveTimeSave;
			}
			
			if (debug) System.out.println("Old: " + moveTimeFix);
			moveTimeFix *= 0.84;
			if (debug) System.out.println("New: " + moveTimeFix);
			
			moveTimeSave = moveTimeFix;
			
			if (testMode)
				scoreToNextLevel += 10;
			else
				scoreToNextLevel += 150 * level;
		}
	}

	private void checkCurrentPieceBackgroundCollision() {
		Piece piece = new Piece(currentPiece);
		piece.getPos().y -= 1;

		if (currentPiece.getPos().y <= 0 || pieceMapCollision(piece)) {
			backGroundCollision = true;
		}
		else {
			backGroundCollision = false;
		}
	}
	
	private void handleCurrentPieceBackgroundCollision() {
		for (Position p : currentPiece.getBoxes()) {
			int x = currentPiece.getPos().x + p.x;
			int y = currentPiece.getPos().y + p.y;

			backGround[x][y] = new BackTuple(true, currentPiece.getColorType());
		}
		
		moveTime = moveTimeFix;
		downFastPossible = false;
		handleMoveDownNormal();
		currentPiece = createRandomPiece();
	}
	
	public boolean currentPieceTouchCollision(float posX, float posY, float accuracy) {
		for (Position p : currentPiece.getBoxes()) {
			if (Math.abs(posX - currentPiece.getPos().x - p.x) < accuracy && 
					Math.abs(posY - currentPiece.getPos().y - p.y) < accuracy)
				return true;
		}

		return false;
	}
	
	private void checkGameOver() {
		for (int x = 0; x < backGround.length; x++) {
			if (backGround[x][backGround[0].length - 1].x) {
				game.setScreen(new GameOverScreen(game, score));
				screen.dispose();
			}
		}
	}
	
	public void handleMoveDownAtOnce() {
		
		Piece p = new Piece(currentPiece);
		
		while (! pieceMapCollision(p)) {
			p.getPos().y -= 1;
		}
		
		p.getPos().y += 1;
		currentPiece = p;
		handleCurrentPieceBackgroundCollision();
		
		actionTime = actionTimeFix;
		downReleased = false;
	}

	public void handleRotation() {
		Piece p = new Piece(currentPiece);
		p.rotate();

		if (! pieceMapCollision(p)) {
			currentPiece = p;
		}
		else {
			handleSpecialRotations();
		}
		
		actionTime = actionTimeFix;
		upReleased = false;
	}
	
	private void handleSpecialRotations() {
		int xBack = 0;
		int yBack = 0;

		if (currentPiece.getColorType() == ColorType.I) {
			xBack = 3;
			yBack = 4;
		}
		else if (currentPiece.getColorType() == ColorType.J || currentPiece.getColorType() == ColorType.L || currentPiece.getColorType() == ColorType.T
				|| currentPiece.getColorType() == ColorType.S || currentPiece.getColorType() == ColorType.Z) {
			xBack = 1;
			yBack = 2;
		}

		for (int y = 0; y <= yBack; y++) {
			for (int x = 0; x <= xBack; x++) {
				Piece p = new Piece(currentPiece);
				p.getPos().x -= x;
				p.getPos().y -= y;
				p.rotate();

				if (! pieceMapCollision(p)) {
					currentPiece = p;
					return;
				}
			}
		}
	}
	
	public void handleMoveLeft() {
		if (actionTime > 0) return;
		
		Piece piece = new Piece(currentPiece);
		piece.getPos().x -= 1;
		
		if (currentPiece.getPos().x > 0 && ! pieceMapCollision(piece))
			currentPiece.getPos().x -= 1;
		
		actionTime = actionTimeFix;
		leftReleased = false;
	}
	
	public void handleMoveRight() {
		if (actionTime > 0) return;
		
		Piece piece = new Piece(currentPiece);
		piece.getPos().x += 1;
		
		if (currentPiece.getPos().x < backGround.length && ! pieceMapCollision(piece))
			currentPiece.getPos().x += 1;
		
		actionTime = actionTimeFix;
		rightReleased = false;
	}
	
	public void handleMoveDown() {
		if (actionTime > 0) return;
		
		Piece piece = new Piece(currentPiece);
		piece.getPos().y += 1;
		
		if (currentPiece.getPos().y > 0 && ! pieceMapCollision(piece))
			currentPiece.getPos().y -= 1;
		
		actionTime = actionTimeFix;
	}
	
	private boolean pieceMapCollision(Piece piece) {
		for (Position p : piece.getBoxes()) {
			int x = piece.getPos().x + p.x;
			int y = piece.getPos().y + p.y;

			if (x < 0 || x >= backGround.length || y < 0 || y >= backGround[0].length || backGround[x][y].x)
				return true;
		}

		return false;
	}

	public int screenToWorld(float number) {
		return (int) (number / GameScreen.BLOCK_SIZE);
	}

	private Piece createRandomPiece() {
		int random = (int) (Math.random() * 7);

		switch (random) {
		case 0 : return new IPiece();
		case 1 : return new JPiece();
		case 2 : return new LPiece();
		case 3 : return new OPiece();
		case 4 : return new SPiece();
		case 5 : return new TPiece();
		case 6 : return new ZPiece();
		default : return null;
		}
	}
	
	public void handleMoveDownFast() {
		downFastPossible = false;
		moveTimeSave = moveTimeFix;
		moveTimeFix = FAST_MOVE_TIME;
		moveTime = 0;
	}
	
	public void handleMoveDownNormal() {
		downFastPossible = true;
		moveTimeFix = moveTimeSave;
	}

	public boolean isLeftReleased() {
		return leftReleased;
	}

	public void setLeftReleased(boolean leftReleased) {
		this.leftReleased = leftReleased;
	}

	public boolean isRightReleased() {
		return rightReleased;
	}

	public void setRightReleased(boolean rightReleased) {
		this.rightReleased = rightReleased;
	}

	public boolean isUpReleased() {
		return upReleased;
	}

	public void setUpReleased() {
		this.upReleased = true;
	}

	public boolean isDownReleased() {
		return downReleased;
	}

	public void setDownReleased() {
		downReleased = true;
		downFastPossible = true;
	}
	
	public Piece getCurrentPiece() {
		return currentPiece;
	}
	
	public BackTuple[][] getBackground() {
		return backGround;
	}

	public void setMoveTimeFix(float moveTimeFix) {
		this.moveTimeFix = moveTimeFix;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getScore() {
		return score;
	}

	public boolean isDownFastPossible() {
		return downFastPossible;
	}
	
}
