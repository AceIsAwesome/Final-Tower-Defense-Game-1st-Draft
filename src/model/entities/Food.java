package model.entities;

import model.World;

public class Food extends Entity{
	
	public Food(double initX, double initY, double r, World myWorld) {
		super(initX, initY, r, myWorld, null);
	}

	@Override
	public void update(int delta) {
		
	}
}
