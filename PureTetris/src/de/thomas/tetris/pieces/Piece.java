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

package de.thomas.tetris.pieces;

import com.badlogic.gdx.utils.Array;

import de.thomas.pure_tetris.World.ColorType;
import de.thomas.tetris.util.Position;

/**
 * Represents a tetris piece
 * @author Thomas Opitz
 *
 */
public class Piece {
	protected Array<Array<Position>> rotations;
	protected int rotationCount;
	protected int rotationAmount;
	protected ColorType colorType;
	private Position pos;
	
	
	public Piece() {
		rotations = new Array<Array<Position>>();
		pos = new Position(4, 15);
		rotationCount = 0;
	}
	
	public Piece(Piece p) {
		rotations = new Array<Array<Position>>(p.rotations);
		rotationCount = p.rotationCount;
		rotationAmount = p.rotationAmount;
		colorType = p.colorType;
		pos = new Position(p.pos.x, p.pos.y);
	}
	
	
	public void rotate() {
		rotationCount = ++rotationCount % rotationAmount;
	}
	
	public Array<Position> getBoxes() {
		return rotations.get(rotationCount);
	}
	
	public Position getPos() {
		return pos;
	}
	
	public ColorType getColorType() {
		return colorType;
	}
}
