package controller;
import gamestates.LoseState;
import gamestates.PlayState;
import gamestates.WinState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class Game extends StateBasedGame {
	
		public Game ()
	{
		super("Game");
	}

		@Override
		public void initStatesList(GameContainer gc) throws SlickException {
			// TODO Auto-generated method stub
			addState(new PlayState());
			addState(new WinState());
			addState(new LoseState());
		}
}
