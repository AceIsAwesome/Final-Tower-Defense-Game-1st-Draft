package enemystates;

import model.entities.Entity;

public abstract class EntityState {
	
	public abstract void enter(Entity e);
	
	public abstract void execute(Entity e);
	public abstract void computeHeading(Entity e);
	
	public abstract void exit(Entity e);
	
}
