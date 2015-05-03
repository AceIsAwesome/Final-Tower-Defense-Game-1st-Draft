package view;

import java.awt.Point;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.entities.Bullet;
import model.entities.Enemy;
import model.entities.Entity;
import model.entities.Player;
import model.entities.Tower;
import model.entities.goTower;
import model.entities.kameBullet;

public class SpriteRenderer {
	
	private CoordinateTranslator convert;
	
	private Image playerImg;
	private Animation playerAnim;
	
	private Image agentImg;
	private Animation enemyAnim;
	
	private Image towerImg;
	
	private Image bulletImg;
	
	private Image gotowerImg;
	
	private Image kamebulletImg;
	
	public SpriteRenderer(CoordinateTranslator convert){
		this.convert = convert;
		try {
			playerImg = new Image("src/linkStand.png");
			playerAnim = getAnimation (playerImg, 3, 1, 119, 128, 3, 100);
			
			agentImg = new Image("src/octork.png");
			enemyAnim = getAnimation ( agentImg, 3, 1, 21, 20, 3, 150);
			
			towerImg = new Image("src/tower.png");
			bulletImg = new Image("src/Arrow.png");
			gotowerImg = new Image("src/goTower.png");
			kamebulletImg = new Image("src/kameBullet.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public void render(Entity e, GameContainer gc, Graphics g){
		if(e instanceof Player){
			renderH((Player)e, gc, g);
		}
		else if(e instanceof Enemy){
			renderH((Enemy)e,gc,g);
		}
		else if(e instanceof Tower){
			renderH((Tower)e, gc, g);
		}
		else if(e instanceof Bullet){
			renderH((Bullet)e,gc,g);
		}
		else if(e instanceof goTower){
			renderH((goTower)e, gc, g);
		}
		else if(e instanceof kameBullet){
			renderH((kameBullet)e,gc,g);
		}
	}

	public void updateAnimations(int t){
		enemyAnim.update(t);
		playerAnim.update(t);
	}
	
	public void setSprites(Image i){
		playerImg = i;
		playerAnim = getAnimation (playerImg, 10, 1, 119, 128, 10, 100);
	}
	
	public void setStand() throws SlickException{
		playerImg = new Image("src/linkStand.png");;
		playerAnim = getAnimation (playerImg, 3, 1, 119, 128, 3, 100);
	}
	
	private void drawSprite(Image i, Entity e){
		Point pc = convert.worldToScreen(e.getX(), e.getY());
		i.draw((float) pc.x -20, (float) pc.y -20, 40, 40);
	}
	
	private void drawkameSprite(Image i, Entity e){
		Point pc = convert.worldToScreen(e.getX(), e.getY());
		i.draw((float) pc.x -20, (float) pc.y -20, 30, 30);
	}
	
	private void drawEnemySprite(Image i, Entity e){
		Point pc = convert.worldToScreen(e.getX(), e.getY());
		
		float rotation = (float) Math.toDegrees(e.getDirection());
		i.setRotation(- rotation);
		//i.draw((float) pc.x -20, (float) pc.y -20, 80, 80);
		i.drawCentered((float) pc.x, (float) pc.y);
	}
	
	private void renderH(Enemy e, GameContainer gc, Graphics g) {
		drawEnemySprite(enemyAnim.getCurrentFrame(),e);
	}
	private void renderH(Player e, GameContainer gc, Graphics g) {
		drawSprite(playerAnim.getCurrentFrame(), e);
	}
	private void renderH(Bullet e, GameContainer gc, Graphics g) {
		drawSprite(bulletImg,e);
	}
	private void renderH(Tower e, GameContainer gc, Graphics g) {
		drawSprite(towerImg, e);
	}
	private void renderH(goTower e, GameContainer gc, Graphics g) {
		drawSprite(gotowerImg, e);
	}
	private void renderH(kameBullet e, GameContainer gc, Graphics g) {
		drawkameSprite(kamebulletImg, e);
	}
	
	public Animation getAnimation (Image i , int sX, int sY , int sW , int sH, int f, int d){
		Animation a = new Animation(false);
		
		int c = 0;
		for( int y = 0 ; y < sY; y++)
		{
			for( int x = 0 ; x < sX; x++)
			{
				if( c < f ) a.addFrame( i.getSubImage(x*sW, y*sH, sW, sH), d);
				c++;
			}
		}
		
		return a;
	}
}