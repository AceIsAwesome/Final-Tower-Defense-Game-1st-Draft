package pathfinding;

import java.awt.Point;

import math.Point2D;
import math.Vector2D;
import model.entities.Enemy;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

public class PathFinder {
	private AStarPathFinder path_finder;
	private Path curP;
	private Path considP;
	private int curS;
	private Enemy e;
	
	public PathFinder(AStarPathFinder pathFinder, Enemy e){
		this.path_finder = pathFinder;
		this.e = e;
		/*curP.appendStep(14, 28);
		curP.appendStep(14, 27);
		curP.appendStep(14, 26);
		curP.appendStep(14, 25);
		curP.appendStep(14, 24);
		curP.appendStep(13, 24);
		curP.appendStep(12, 24);
		curP.appendStep(11, 24);
		curP.appendStep(10, 24);
		curP.appendStep(9, 24);
		curP.appendStep(8, 24);
		curP.appendStep(7, 24);
		curP.appendStep(6, 24);
		curP.appendStep(5, 24);
		curP.appendStep(4, 24);
		curP.appendStep(3, 24);
		curP.appendStep(3, 23);
		curP.appendStep(3, 22);
		curP.appendStep(3, 21);
		curP.appendStep(3, 20);
		curP.appendStep(3, 19);
		curP.appendStep(3, 18);
		curP.appendStep(3, 17);
		curP.appendStep(4, 17);
		curP.appendStep(5, 17);
		curP.appendStep(6, 17);
		curP.appendStep(7, 17);
		curP.appendStep(8, 17);
		curP.appendStep(9, 17);
		curP.appendStep(10, 17);
		curP.appendStep(11, 17);
		curP.appendStep(12, 17);
		curP.appendStep(13, 17);
		curP.appendStep(14, 17);
		curP.appendStep(15, 17);
		curP.appendStep(16, 17);
		curP.appendStep(17, 17);
		curP.appendStep(18, 17);
		curP.appendStep(19, 17);
		curP.appendStep(20, 17);
		curP.appendStep(21, 17);
		*/
	}
	public void generatePath(Point2D dest){
		Point current = e.getNav().worldToTile(e.getLoc());
		Point destiny = e.getNav().worldToTile(dest);
		curP = path_finder.findPath(e, current.x, current.y, destiny.x, destiny.y);
		considP = curP;
		curP = smoothPath(curP);
		curS = 0;
	}
	public void generatePath(Point p) {
		//System.out.println("1");
		Point current = e.getNav().worldToTile(e.getLoc());
		//System.out.println("2");
		curP = path_finder.findPath(e, current.x, current.y, p.x, p.y);
		//System.out.println("3");
		considP = curP;
		//System.out.println("4");
		curP = smoothPath(curP);
		//System.out.println("5");
		curS = 0;
		//System.out.println("6");
	}
	
	public Path smoothPath(Path p){
		Path smoothPath = new Path();
		if(p == null || p.getLength() == 0){
			return smoothPath;
		}
		Step curS = p.getStep(0);
		smoothPath.appendStep(curS.getX(), curS.getY());
		Path lastSP = null;
		Path strP = null;
		for(int i=1; i < p.getLength(); i++){
			Step next = p.getStep(i);
			strP = getOpenStraightPath(new Point(curS.getX(),curS.getY()), new Point(next.getX(),next.getY()));
			
			if(strP == null){
				for(int j=0; j< lastSP.getLength(); j++){
					smoothPath.appendStep(lastSP.getX(j), lastSP.getY(j));
				}
				curS = next; 
			}else{
				lastSP = strP;
			}
		}
		if(strP != null){
			for(int j=0; j< strP.getLength(); j++){
				smoothPath.appendStep(strP.getX(j), strP.getY(j));
			}
		}
		return smoothPath;
	}
	public Path getOpenStraightPath(Point tail, Point head){
		Path strP = new Path();
		strP.appendStep(tail.x, tail.y);
		int difX = tail.x - head.x;
		int difY = tail.y - head.y;
		while(difX != 0 || difY != 0){
			int cx = tail.x;
			int cy = tail.y;
			
			tail.x -= Integer.signum(difX);
			tail.y -= Integer.signum(difY);
			if(e.getNav().blocked(cx, cy, tail.x, tail.y)){
				return null;
			}
			strP.appendStep(tail.x, tail.y);
			difX -= Integer.signum(difX);
			difY -= Integer.signum(difY);
		}
		return strP;
	}
	public void walkPath(int delta){
		if(curP == null || curS >= curP.getLength()){
			return;
		}
		Step curStep = curP.getStep(curS);
		Point2D curDest = e.getNav().tileToWorld(curStep.getX(), curStep.getY());
		
		Vector2D mVector = new Vector2D(curDest.getX(), curDest.getY());
		mVector.scalePlusEquals(-1, new Vector2D(e.getLoc().getX(), e.getLoc().getY()));
		e.move(mVector, delta);
		if(e.contains(curDest)){
			curS++;
		}
	}
	
	public Path getConsideredPaths(){
		return considP;
	}
	public Path getCur_path() {
		return curP;
	}

	public void setCur_path(Path cur_path) {
		this.curP = cur_path;
	}

	public AStarPathFinder getPath_finder() {
		return path_finder;
	}

	public void setPath_finder(AStarPathFinder path_finder) {
		this.path_finder = path_finder;
	}
	public boolean done() {
		if(curP == null){
			return true;
		}
		return (curS >= curP.getLength());
	}
}
