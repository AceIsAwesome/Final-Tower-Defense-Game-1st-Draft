package enemystates;



import org.newdawn.slick.util.pathfinding.Path;

import math.Point2D;
import math.Vector2D;
import model.entities.Enemy;
import model.managers.ConsoleLog;

public class RestState extends EnemyState {
	private long timer;
	@Override
	public void enter(Enemy e) {
		//log
		ConsoleLog.getInstance().log("Agent: " + e.getId() + " is going to rest" );
		
		Point2D[] rest_locs = {new Point2D(16,90), new Point2D(36,50), new Point2D(80,10)} ;
		int min = Integer.MAX_VALUE;
		Path best_path = null;
		for(Point2D rest_loc : rest_locs){
			e.getPathing().generatePath(rest_loc);
			ConsoleLog.getInstance().log("Distance to rest location: " + rest_loc + " " + e.getPathing().getPath_finder().getSearchDistance());
			if(e.getPathing().getPath_finder().getSearchDistance() < min){
				min = e.getPathing().getPath_finder().getSearchDistance();
				best_path = e.getPathing().getCur_path();
			}
		}
		e.getPathing().setCur_path(best_path);
		
		timer = 1500;
	}

	@Override
	public void execute(Enemy e, int delta) {
		if(e.getPathing().done()){
			timer -= delta;
			if(timer <= 0){
				e.getMy_state_machine().ChangeState(new WanderState());
			}
		}
		else{
			e.getPathing().walkPath(delta);
		}
	}

	@Override
	public Vector2D computeHeading(Enemy e) {
		return new Vector2D(0,0);
	}

	@Override
	public void exit(Enemy e) {
		e.rest = 20000;
		
	}

	@Override
	public void resetToStart(Enemy e) {
		Point2D[] rest_locs = {new Point2D(16,90), new Point2D(36,50), new Point2D(80,10)} ;
		int min = Integer.MAX_VALUE;
		Path best_path = null;
		for(Point2D rest_loc : rest_locs){
			e.getPathing().generatePath(rest_loc);
			ConsoleLog.getInstance().log("Distance to rest location: " + rest_loc + " " + e.getPathing().getPath_finder().getSearchDistance());
			if(e.getPathing().getPath_finder().getSearchDistance() < min){
				min = e.getPathing().getPath_finder().getSearchDistance();
				best_path = e.getPathing().getCur_path();
			}
		}
		e.getPathing().setCur_path(best_path);
	}

}
