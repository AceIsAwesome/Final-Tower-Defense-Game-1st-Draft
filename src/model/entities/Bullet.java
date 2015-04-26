package model.entities;

import math.Vector2D;
import model.World;
import model.managers.EntityManager;
import pathfinding.NavGraph;

public class Bullet extends MovingEntity {
	private Entity target;
	private EntityManager e;
	private boolean hasTarget = false;
	private boolean dead;

	public Bullet(double initX, double initY, double velocity, double r,
			World myWorld, NavGraph graph, EntityManager ent) {
		super(initX, initY, velocity, r, myWorld, graph);
		// TODO Auto-generated constructor stub
		e = ent;
		
	}

	@Override
	public void update(int delta) {
		if(hasTarget == false){
			for(Entity en: e.getArray()){
				if(en instanceof Enemy){
					target = en;
					hasTarget = true;
				}
			}
		}
		
		Vector2D v = new Vector2D(target.getX() - loc.getX(), target.getY() - loc.getY());
		move(v, delta);
		
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
