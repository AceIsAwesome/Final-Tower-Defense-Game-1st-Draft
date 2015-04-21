package enemystates;

import java.awt.Point;

import math.Point2D;
import math.Vector2D;
import model.entities.Enemy;
import model.managers.ConsoleLog;

public class AttackState extends EnemyState {

	@Override
	public void enter(Enemy e) {
		// Pick a random location to wander to
		Point p = new Point(28,2);
		
		// Generate path to that point
		e.getPathing().generatePath(p);
	}

	@Override
	public void execute(Enemy e, int delta) {
		if(e.getPathing().done()){
			// Pick a random location to wander to
			Point p = e.getNav().getRandomOpenTile();
			
			// Generate path to that point
			e.getPathing().generatePath(p);
		}
		e.getPathing().walkPath(delta);
	}

	@Override
	public Vector2D computeHeading(Enemy e) {
		//I made the pathing handle movement
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
