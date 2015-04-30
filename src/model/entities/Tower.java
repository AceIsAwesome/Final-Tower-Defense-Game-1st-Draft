package model.entities;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import math.Point2D;
import math.Vector2D;
import model.World;
import model.managers.ConsoleLog;
import model.managers.EntityManager;
import pathfinding.NavGraph;

public class Tower extends Entity {
	private Point2D startLoc;
	private EntityManager e;
	private boolean canSee;
	private double sight;
	private int fireLag = 200;
	private boolean canFire = false;

	public Tower(double initX, double initY, double r, double sight, World myWorld, NavGraph graph, EntityManager ent) throws SlickException {
		super(initX, initY,r, myWorld, graph);
		startLoc = new Point2D(initX, initY);
		e = ent;
		canSee = false;
		this.sight = sight;
		//System.out.println("tower created");
	}

	@Override
	public void update(int delta) {
		/*for (Entity en : e.getArray()) {
			Point2D target_loc = ((Enemy) en).getTarget().getLoc();
			Vector2D d_p = target_loc.minus(en.getLoc());
			if(d_p.dot(d_p) <= (sight * sight)){//if agent is close enough
				Vector2D a_view = ((MovingEntity) en).getVelocity();
					
			}
		}*/
		if(canFire){
			fireLag = 200;
		}
		else{
			fireLag--;
			if(fireLag == 0){
				canFire = true;
			}
		}
		
	}
	
	public boolean getCanFire(){
		return canFire;
	}
	
	public void setCanFire(boolean b){
		canFire = b;
		fireLag = 200;
	}

}
