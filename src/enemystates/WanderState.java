package enemystates;

import java.awt.Point;

import math.Point2D;
import math.Vector2D;
import model.entities.Enemy;
import model.managers.ConsoleLog;

public class WanderState extends EnemyState {

	@Override
	public void enter(Enemy e) {
		// Pick a random location to wander to
		Point p = e.getNav().getRandomOpenTile();
		
		// Generate path to that point
		e.getPathing().generatePath(p);
	}

	@Override
	public void execute(Enemy e, int delta) {
		// If at end of path, pick new random point
		if(e.getPathing().done()){
			// Pick a random location to wander to
			Point p = e.getNav().getRandomOpenTile();
			
			// Generate path to that point
			e.getPathing().generatePath(p);
		}
		// Else, get hungry and tired and move
		e.rest -= delta;
		e.food -= delta;
		//if rest timer out then rest
		if(e.rest < 0){
			e.getMy_state_machine().ChangeState(new RestState());
		}
		//if out of food, eat
		if(e.food < 0){
			e.getMy_state_machine().ChangeState(new EatState());
		}	
		// If agent sees the player, attack
		Point2D target_loc = e.getTarget().getLoc();
		Vector2D d_p = target_loc.minus(e.getLoc());
		if(d_p.dot(d_p) <= (e.sight * e.sight)){//if agent is close enough
			Vector2D a_view = e.getVelocity();
			if(a_view.dot(d_p) >= 0){// if 180 deg in front of agent
				if(e.getPathing().getOpenStraightPath(e.getNav().worldToTile(e.getLoc()), e.getNav().worldToTile(target_loc)) != null){
					//print the angle spotted at
					ConsoleLog.getInstance().log("Found player at angle: " + Math.acos(a_view.getNormalized().dot(d_p.getNormalized())));
					e.getMy_state_machine().ChangeState(new ChaseState());
				}
			}	
		}
		e.getPathing().walkPath(delta);
	}

	@Override
	public Vector2D computeHeading(Enemy e) {
		//I made the pathing handle movement... for better or worse
		return new Vector2D(0,0);
	}

	@Override
	public void exit(Enemy e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetToStart(Enemy e) {
		// Pick a random location to wander to
		Point p = e.getNav().getRandomOpenTile();
		
		// Generate path to that point
		e.getPathing().generatePath(p);
	}

}
