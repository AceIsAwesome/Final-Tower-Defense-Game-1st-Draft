package pathfinding;

import java.awt.Point;

import math.Point2D;
import model.World;
import model.entities.Entity;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class NavGraph implements TileBasedMap {
	private static final float sq2 = (float) Math.sqrt(2.0);
	private TiledMap map;
	private World world;
	private boolean[][] blocked;
	
	public NavGraph(TiledMap map, World world) {
		super();
		this.map = map;
		this.world = world;
		
		blocked = new boolean[map.getWidth()][map.getHeight()];

		int layer = 1; 

		for(int i = 0; i < map.getHeight(); i++) {
		    for(int j = 0; j < map.getWidth(); j++) {
		        int tileID = map.getTileId(j, i, layer);
		        blocked[i][j] = (tileID != 0);
		    }
		}
	}

	public Point worldToTile(Point2D cur) {
		int x = (int) ((map.getWidth() * map.getTileWidth())/world.getWorldW() * cur.getX());
		int y = (int) ((map.getHeight() * map.getTileHeight())/world.getWorldH() * cur.getY());
		return new Point(x/map.getTileWidth(), map.getHeight() - y/map.getTileHeight() - 1);
	}
	public Point2D tileToWorld(int x, int y) {
		double wx = ((x + 0.5)/map.getWidth())*world.getWorldW();
		double wy = world.getWorldH() - ((y + 0.5)/map.getHeight())*world.getWorldH();
		return new Point2D(wx,wy);
	}
	
	public Point getRandomOpenTile(){
		boolean block = true;
		int x = 28;
		int y = 28;
		while(block){
			x+=1;
			y-=1;
			//x = (int) (Math.random() * map.getWidth());
			//y = (int) (Math.random() * map.getHeight());
			block = blocked[y][x];
		}
		//System.out.println(x+"  "+ y);
		return new Point(x,y);
	}
	
	public boolean blocked(Point2D cur){
		Point mapCord = worldToTile(cur);
		//Allows World wrapping.
		if(mapCord.y < 0){
			mapCord.y = blocked.length -1;
		}

		if(mapCord.x < 0){
		mapCord.y = blocked.length -1;
		}

		if(mapCord.x > blocked.length-1){
		mapCord.x = 0;
		}

		if(mapCord.y > blocked.length-1){
		mapCord.y = 0;
		}
		
		return blocked[mapCord.y][mapCord.x];
	}

	@Override
	public boolean blocked(PathFindingContext c, int tx, int ty) {
		if(blocked[ty][tx]){
			return true;
		}
		boolean cornerBlocked = false;
		if(c instanceof AStarPathFinder){
			AStarPathFinder finder = (AStarPathFinder) c;
			if(finder.getCurrentX() != tx && finder.getCurrentY() != ty){
				if(finder.getCurrentX() < tx){
					if(blocked[ty][tx - 1]){
						cornerBlocked = true;
					}
				}
				else{
					if(blocked[ty][tx + 1]){
						cornerBlocked = true;
					}
				}
				if(finder.getCurrentY() < ty){
					if(blocked[ty-1][tx]){
						cornerBlocked = true;
					}
				}
				else{
					if(blocked[ty+1][tx]){
						cornerBlocked = true;
					}
				}
			}
		}
		return cornerBlocked;
	}

	@Override
	public float getCost(PathFindingContext c, int tx, int ty) {
		if(c instanceof AStarPathFinder){
			AStarPathFinder finder = (AStarPathFinder) c;
			if(finder.getCurrentX() != tx && finder.getCurrentY() != ty){
				return sq2;
			}
		}
		return 1;
	}

	@Override
	public int getHeightInTiles() {
		return map.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return map.getWidth();
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
	}

	public boolean blocked(int cx, int cy, int tx, int ty) {
		if(blocked[ty][tx]){
			return true;
		}
		boolean cornerBlocked = false;
		if(cx != tx && cy != ty){
			if(cx < tx){
				if(blocked[ty][tx - 1]){
					cornerBlocked = true;
				}
			}else{
				if(blocked[ty][tx + 1]){
					cornerBlocked = true;
				}
			}
			if(cy < ty){
				if(blocked[ty-1][tx]){
					cornerBlocked = true;
				}
			}else{
				if(blocked[ty+1][tx]){
					cornerBlocked = true;
				}
			}
		}
		return cornerBlocked;
	}
	
	
}
