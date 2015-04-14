package model.entities;

import pathfinding.NavGraph;
import model.World;

public class Prize extends Entity {
	
	public boolean caught;
	
	public Prize(double initX, double initY, double r, World myWorld, NavGraph graph) {
		super(initX, initY, r, myWorld, graph);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(int delta) {
		// do nothing
	}

	public void catchIt() {
		caught = true;
		//when caught give new random location
		//loc.setX(myWorld.getWorldW() * Math.random());
		//loc.setY(myWorld.getWorldH() * Math.random());
	}
	
	
}
