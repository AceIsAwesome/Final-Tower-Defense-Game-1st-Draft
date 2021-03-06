package model.entities;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import math.Vector2D;
import model.World;
import model.managers.EntityManager;
import pathfinding.NavGraph;

public class Bullet extends MovingEntity {
	private Entity target;
	private EntityManager e;
	private boolean hasTarget = false;
	private boolean dead;
	private Sound fire;
	private Sound kill;
	private Tower tow;

	public Bullet(double initX, double initY, double velocity, double r,
			World myWorld, NavGraph graph, EntityManager ent, Tower gt) throws SlickException {
		super(initX, initY, velocity, r, myWorld, graph);
		// TODO Auto-generated constructor stub
		e = ent;
		fire = new Sound("src/whoosh.ogg");
		kill = new Sound("src/wilhelm.ogg");
		tow = gt;
	}

	@Override
	public void update(int delta) {
		for(Entity en: e.getArray()){
			if(en instanceof Enemy){
				if(hasTarget == false){
					if(((Enemy)en).getHasBullet() == false){
						target = en;
						hasTarget = true;
						((Enemy) en).setHasBullet(true);
						fire.play();
						tow.sethasBullet(false);
					}
				}
			}
		}
		if(target != null){
			if(this.isCollided(target)){
				super.setKilled(true);
				target.setKilled(true);
				kill.play();
			}
		Vector2D v = new Vector2D(target.getX() - loc.getX(), target.getY() - loc.getY());
		move(v, delta);
		}
		
	}
	
	public void setHasTarget(boolean sht){
		hasTarget = sht;
	}
	
	public boolean getDead(){
		return dead;
	}
	
	public void setDead(boolean d){
		dead = d;
	}

}
