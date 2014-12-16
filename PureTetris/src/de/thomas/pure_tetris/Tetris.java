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

import java.util.Scanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.thomas.pure_tetris.screens.MainMenuScreen;

public class Tetris extends Game {
	public SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this, true, 0));
		readOptions();
	}
	
	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
	}
	
	private void readOptions() {
		FileHandle file = Gdx.files.local("options.txt");
		
		if (file.exists()) {
			String input = file.readString();
			
			Scanner scanner = new Scanner(input);
			
			String showGridStr = "";
			String levelStr = "";
			
			if (scanner.hasNext()) {
				showGridStr = scanner.next();
			}
			if (scanner.hasNext()) {
				levelStr = scanner.next();
			}
			
			if (! showGridStr.equals("") && ! levelStr.equals("")) {
				if (showGridStr.equals("true"))
					Options.showGrid = true;
				else
					Options.showGrid = false;
				
				Options.startLevel = Integer.parseInt(levelStr);
			}
			
			scanner.close();
		}
	}
}
