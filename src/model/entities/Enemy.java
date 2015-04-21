package model.entities;

import java.awt.Point;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import enemystates.AttackState;
import enemystates.EnemyState;
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
	private EnemyState attack = new AttackState();

	private PathFinder pathing;
	
	public Enemy(double initX, double initY, double r, double velocity, double sight, World myWorld,
			NavGraph graph) {
		super(initX, initY, r, velocity, myWorld, graph);
		startLoc = new Point2D(initX, initY);
		my_state_machine = new StateHandler(this);
		
		setPathing(new PathFinder(new AStarPathFinder(my_nav, Integer.MAX_VALUE, true), this));
		this.my_state_machine.ChangeState(attack);
		
	}

	@Override
	public void update(int delta) {
		my_state_machine.update(delta);
		if(pathing.done()){
			super.setDead(true);
		}
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
