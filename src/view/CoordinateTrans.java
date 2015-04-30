package view;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class CoordinateTrans{
	double screenH = 0;
	double screenW = 0;
	double worldH = 0;
	double worldW = 0;
	double wlowleftx = 0;
	double wlowlefty = 0;
	
	public CoordinateTrans(double sw, double sh, double ww, double wh, double wllx, double wlly)
    {	
		screenW = sw;
		screenH = sh;
		worldH = wh;
		worldW = ww;
		wlowleftx = wllx;
		wlowlefty = wlly;
        
    }
	
	public Point2D ScreenToWorld(int x, int y){
		Point2D p = new Point2D.Double();
		double px = 0;
		double py = 0;
		
		
		px = wlowleftx + (x * (worldW/screenW));
		py = -(y * (worldH/screenH)) + wlowlefty + worldH;
		
		p.setLocation(px, py);
		
		return(p);	
	}
	
	public Point2D ScreenToWorld(Point p){
		Point2D p2d = new Point2D.Double();
		double px = 0;
		double py = 0;
		
		px = wlowleftx + (p.getX()) * (worldW/screenW);
		py = -(p.getY() * (worldH/screenH)) + wlowlefty + worldH;
		p2d.setLocation(px, py);
		
		return(p2d);
	}

	public Point WorldToScreen(int x, int y){
		Point p = new Point();
		double px = 0;
		double py = 0;
		
		px = (x - wlowleftx) * (screenW/worldW);
		py = (worldH - y + wlowlefty) * (screenH/worldH);
		
		p.setLocation(px, py);
		
		return(p);
	}

	public Point WorldToScreen(Point2D p){
		Point p2d = new Point();
		double px = 0;
		double py = 0;
		
		px = (p.getX() - wlowleftx) * (screenW/worldW);
		py = (worldH - p.getY() + wlowlefty) * (screenH/worldH);
		
		p2d.setLocation(px, py);
		
		return(p2d);
	}

}