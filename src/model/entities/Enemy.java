package model.entities;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import enemystates.ChaseState;
import enemystates.RestState;
import enemystates.StateHandler;
import pathfinding.NavGraph;
import pathfinding.PathFinder;
import math.Point2D;
import math.Vector2D;
import model.World;

public class Enemy extends MovingEntity {
	private Entity target;
	private Point2D startLoc;
	private StateHandler my_state_machine;
	
	public int rest;
	public int food;
	public double sight;
	private PathFinder pathing;
	
	public Enemy(double initX, double initY, double r, double velocity, double sight, World myWorld,
			NavGraph graph) {
		super(initX, initY, r, velocity, myWorld, graph);
		startLoc = new Point2D(initX, initY);
		my_state_machine = new StateHandler(this);
		
		setPathing(new PathFinder(new AStarPathFinder(my_nav, Integer.MAX_VALUE, true), this));
		this.sight = sight;
		my_state_machine.ChangeState(new RestState());
		
	}

	@Override
	public void update(int delta) {
		my_state_machine.update(delta);
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public Point2D getStartLoc() {
		return startLoc;
	}

	public StateHandler getMy_state_machine() {
		return my_state_machine;
	}

	public PathFinder getPathing() {
		return pathing;
	}

	public void setPathing(PathFinder pathing) {
		this.pathing = pathing;
	}
}
