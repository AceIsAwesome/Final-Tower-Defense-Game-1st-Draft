package view;

import gamestates.Camera;

import java.awt.Point;

import model.World;
import model.entities.Entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class MiniMap {

	   private Image miniMap;
	   private int x;
	   private int y;
	   private CoordinateTranslator convertWtoM;
	   
	   public MiniMap(Image map, World w) {
		   miniMap = map;
		   convertWtoM = new CoordinateTranslator(w.getWorldW(), w.getWorldH(),190, 190);
		  
	   }


	   public void render(GameContainer gc, Graphics g, Camera c, Entity e) {
		  x = gc.getWidth() - 190;
		  y = gc.getHeight() - 190;
		  //Draw MiniMap image
		  miniMap.draw(x, y);
		  //Draw player dot
		  Point p = convertWtoM.worldToScreen(e.getLoc());
		  g.drawOval(x + p.x, y + p.y, 1, 1);
		  
		  //Draw Screen rect
		  int leftOfMini = (int) (c.cameraX * 190.0/c.mapWidth);
		  int topOfMini = (int) (c.cameraY * 190.0/c.mapHeight);
		  p = new Point(leftOfMini, topOfMini);
		  Color col = g.getColor();
		  g.setColor(Color.white);
		  g.drawRect(x + p.x, y + p.y, 190 / ((float)(c.mapWidth)/gc.getWidth()), 190 / ((float)(c.mapHeight)/gc.getHeight()));
		  g.setColor(col);
	   }
	}