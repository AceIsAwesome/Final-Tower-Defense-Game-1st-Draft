package enemystates;

import org.newdawn.slick.util.pathfinding.Path;
import math.Point2D;
import math.Vector2D;
import model.entities.Enemy;
import model.managers.ConsoleLog;

public class EatState extends EnemyState {

	private long timer;
	
	@Override
	public void enter(Enemy e) {
		//log
		ConsoleLog.getInstance().log("Agent: " + e.getId() + " is going to eat" );
		
		Point2D[] eat_locs = {new Point2D(14,6), new Point2D(34,48), new Point2D(80,90), new Point2D(37,96), new Point2D(90,15)} ;
		int min = Integer.MAX_VALUE;
		Path best_path = null;
		for(Point2D eat_loc : eat_locs){
			e.getPathing().generatePath(eat_loc);
			ConsoleLog.getInstance().log("Distance to rest location: " + eat_loc + " " + e.getPathing().getPath_finder().getSearchDistance());
			if(e.getPathing().getPath_finder().getSearchDistance() < min){
				min = e.getPathing().getPath_finder().getSearchDistance();
				best_path = e.getPathing().getCur_path();
			}
		}
		e.getPathing().setCur_path(best_path);
		
		//eat for 1 seconds
		timer = 1000;
	}

	@Override
	public void execute(Enemy e, int delta) {
		//reduce timer by delta if on eating spot
		if(e.getPathing().done()){
			timer -= delta;
			//go back to wandering if done eating
			if(timer <= 0){
				e.getMy_state_machine().ChangeState(new WanderState());
			}
		}else{//else move down path
			e.getPathing().walkPath(delta);
		}
	}

	@Override
	public Vector2D computeHeading(Enemy e) {
		//do not move while resting
		return new Vector2D(0,0);
	}

	@Override
	public void exit(Enemy e) {
		//tell the agent that it doesn't need to eat for 15 seconds
		e.food = 15000;
		
	}

	@Override
	public void resetToStart(Enemy e) {
		Point2D[] eat_locs = {new Point2D(14,8), new Point2D(34,48), new Point2D(80,90)} ;
		int min = Integer.MAX_VALUE;
		Path best_path = null;
		for(Point2D eat_loc : eat_locs){
			e.getPathing().generatePath(eat_loc);
			ConsoleLog.getInstance().log("Distance to rest location: " + eat_loc + " " + 
					e.getPathing().getPath_finder().getSearchDistance());
			if(e.getPathing().getPath_finder().getSearchDistance() < min){
				min = e.getPathing().getPath_finder().getSearchDistance();
				best_path = e.getPathing().getCur_path();
			}
		}
		e.getPathing().setCur_path(best_path);
		
	}
}
