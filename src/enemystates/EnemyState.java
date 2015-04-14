package enemystates;

import math.Vector2D;
import model.entities.Enemy;
import model.entities.Entity;

public abstract class EnemyState extends EntityState {

	public abstract void enter(Enemy e);

	public abstract void execute(Enemy e, int delta);

	public abstract Vector2D computeHeading(Enemy e);

	public abstract void exit(Enemy e);
	
	public abstract void resetToStart(Enemy e);

	
	//these should point these to the Agent versions,
	//if the Entity is an Agent
	@Override
	public void enter(Entity e) {
	}
	@Override
	public void execute(Entity e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void computeHeading(Entity e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exit(Entity e) {
		// TODO Auto-generated method stub
		
	}
	


}
