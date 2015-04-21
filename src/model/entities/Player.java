package model.entities;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import pathfinding.NavGraph;
import math.Point2D;
import math.Vector2D;
import model.World;
import model.managers.ConsoleLog;
import model.managers.EntityManager;

public class Player extends MovingEntity {
	private Point2D startLoc;
	private int points;
	private int deaths;
	private Sound die;
	private EntityManager e;
	
	public Player(double initX, double initY, double r, double velocity, World myWorld, NavGraph graph, EntityManager ent) throws SlickException {
		super(initX, initY,r, velocity, myWorld, graph);
		startLoc = new Point2D(initX, initY);
		die = new Sound("src/whoosh.ogg");
		e = ent;
	}

	public boolean moveLeft;
	public boolean moveRight;
	public boolean moveUp;
	public boolean moveDown;
	
	@Override
	public void update(int delta) {
		Vector2D v = new Vector2D(0,0);
		if(moveLeft){
			v.plusEquals(new Vector2D(-1,0));
		}
		if(moveRight){
			v.plusEquals(new Vector2D(1,0));
		}
		if(moveUp){
			v.plusEquals(new Vector2D(0,1));
		}
		if(moveDown){
			v.plusEquals(new Vector2D(0,-1));
		}
		move(v, delta);
		
		//make it so the player wraps
		if(loc.getX() > myWorld.getWorldW())
			loc.setX(0);
		if(loc.getY() > myWorld.getWorldW())
			loc.setY(0);
		if(loc.getX() < 0)
			loc.setX(myWorld.getWorldW());
		if(loc.getY() < 0)
			loc.setY(myWorld.getWorldW());
		
		//checkCollisons();
	}
	/*private void checkCollisons(){
		for(Entity en : e.getArray()){
			if(en != this){
				if(this.isCollided(en)){
					if(en instanceof Enemy){
						loc.set(startLoc);
						Point2D reset = ((Enemy)en).getStartLoc();
						en.setLoc(reset);
						die.play();
						deaths++;
						ConsoleLog.getInstance().log("Player Died");
						ConsoleLog.getInstance().log("You have " + (5 - deaths) + " lives remaining");
					}
				}
			}
		}
	}*/

	
	public int getPoints() {
		return points;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setPoints(int i) {
		points = i;	
	}
	public void setDeaths(int i) {
		deaths = i;		
	}
}
