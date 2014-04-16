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

package de.thomas.pure_tetris.pieces;

import com.badlogic.gdx.utils.Array;

import de.thomas.pure_tetris.World.ColorType;
import de.thomas.pure_tetris.util.Position;

public class ZPiece extends Piece {
	Array<Position> firstPosition;
	Array<Position> secondPosition;
	
	public ZPiece() {
		super();
		rotationAmount = 2;
		colorType = ColorType.Z;
		firstPosition = new Array<Position>();
		firstPosition.add(new Position(0, 1));
		firstPosition.add(new Position(1, 1));
		firstPosition.add(new Position(1, 0));
		firstPosition.add(new Position(2, 0));
		
		secondPosition = new Array<Position>();
		secondPosition.add(new Position(1, 2));
		secondPosition.add(new Position(1, 1));
		secondPosition.add(new Position(0, 1));
		secondPosition.add(new Position(0, 0));
		
		rotations.add(firstPosition);
		rotations.add(secondPosition);
	}
}
