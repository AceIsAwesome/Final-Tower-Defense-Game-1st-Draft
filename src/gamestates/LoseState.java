package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LoseState extends BasicGameState {
	private boolean ng;
	
	private Music music;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		music = new Music("src/LegendofZelda.ogg");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("You Have Failed and The Village Was Destroyed!", gc.getWidth()/4, gc.getHeight()/2);
		g.drawString("Press 'n' to play again!", gc.getWidth()/3 + 20, gc.getHeight()/2 + 20);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame game, int arg2) throws SlickException {
		if(ng){
			ng = false;
			game.enterState(0);
			music.loop();
		}
	}

	@Override
	public int getID() {
		return 2;
	}
	
	public void keyPressed(int key, char c) {
		if (c == 'n') {
			ng = true;	
		}
	}

}
