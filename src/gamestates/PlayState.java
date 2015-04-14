package gamestates;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import math.Point2D;
import model.World;
import model.entities.Enemy;
import model.entities.Entity;
import model.entities.Food;
import model.entities.Player;
import model.entities.Prize;
import model.managers.ConsoleLog;
import model.managers.EntityManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import enemystates.WanderState;
import pathfinding.NavGraph;
import view.ConsoleView;
import view.CoordinateTranslator;
import view.MiniMap;
import view.NavView;
import view.SpriteRenderer;

public class PlayState extends BasicGameState {

	private TiledMap map;
	private Camera camera;
	private SpriteRenderer spriteRender;
	
	private MiniMap minimap;
	private ConsoleView consoleView;
	private NavView navView;
	private boolean showNavGrid;
	private boolean showAgentPaths;
	private boolean showConsideredSmoothPaths;
	private boolean showConsideredUnsmoothedPaths;
	private boolean showLog;
	private EntityManager e;
	
	private Point2D villagePoint = new Point2D(60,61);
	
	private CoordinateTranslator translator;
	
	private Music music;
	
	private Sound win;
	private Sound lose;
	private Sound ding;
	
	private World world;
	private Player player;
	private Prize prize;
	private NavGraph nav;
	private String mapName = "";
	private String musicName = "";
	
	private Food food1;
	private Food food2;
	private Food food3;
	private Food food4;
	private Food food5;
	
	int newX;
	int newY;
	
	Point new_p;
	
	private Image i;

	

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		String mapfileName = "src/mapName.txt";
        String musicfileName = "src/musicName.txt";
        
        String mapLine = null;
        String musicLine = null;

        try {
            FileReader mapfileReader = new FileReader(mapfileName);
            FileReader musicfileReader = new FileReader(musicfileName);

            BufferedReader mapbufferedReader = new BufferedReader(mapfileReader);
            BufferedReader musicbufferedReader = new BufferedReader(musicfileReader);

            while((mapLine = mapbufferedReader.readLine()) != null) {
                mapName = mapLine;
            } 
            while((musicLine = musicbufferedReader.readLine()) != null) {
                musicName = musicLine;
            }
            mapbufferedReader.close(); 
            musicbufferedReader.close(); 
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + mapfileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + musicfileName + "'");                   
        }
		//Load Camera
        System.out.println(mapName);
        map = new TiledMap("src/"+mapName);
		camera = new Camera(gc, map);
		ConsoleLog.getInstance().log("Camera Loaded");
		
		//Load World stuff
		world = new World(100, 100);
		translator = new CoordinateTranslator(world.getWorldW(), world.getWorldH(), camera.mapWidth, camera.mapHeight);
		nav = new NavGraph(map, world);
		//Load Entities
		e = new EntityManager();
		
		player = new Player(55, 55, .5, .01, world, nav, e);
		prize = new Prize(50, 50, .5, world,nav);
		food1 = new Food(14, 6, .2, world);
		food2 = new Food(34, 48, .2, world);
		food3 = new Food(80, 90, .2, world);
		food4 = new Food(37, 96, .2, world);
		food5 = new Food(90, 15, .2, world);

		e.addEntity(player);
		e.addEntity(prize);
		e.addEntity(food1);
		e.addEntity(food2);
		e.addEntity(food3);
		e.addEntity(food4);
		e.addEntity(food5);
		
		addAgents();
		//Load UI Components
		//NavGraphView
		navView = new NavView(nav, map);
		//Minimap
		Image miniMapImage = new Image("/src/pff4.png");
		minimap = new MiniMap(miniMapImage, world);
		//Console
		consoleView = new ConsoleView();
		//Sprites
		spriteRender = new SpriteRenderer(translator);
		
		ConsoleLog.getInstance().log("Entities Loaded");
		
		newX = 60*32;
		newY = 60*32;
		
		new_p = new Point(50,50);
		
		win = new Sound("src/youWin.ogg");
		lose = new Sound("src/youLost.ogg");
		ding = new Sound("src/chime.ogg");
		
		music = new Music("src/"+musicName);
		music.setVolume(0.5f);
		music.loop();
	}
	private void addAgents(){
		
		Enemy agent = new Enemy(40, 40, .25, .0095, 10, world, nav);
		agent.setTarget(player);
		e.addEntity(agent);
		
		agent = new Enemy(35, 35, .25, .009, 10, world, nav);
		agent.setTarget(player);
		e.addEntity(agent);
		
		agent = new Enemy(45, 45, .25, .01, 10, world, nav);
		agent.setTarget(player);
		e.addEntity(agent);
		
		//Vision Debug agent
		/*
		Agent agent = new Agent(44, 48,.25, 0, 50, world, nav);
		agent.setTarget(player);
		agent.food = 120000;
		agent.rest = 120000;
		agent.getMy_state_machine().ChangeState(new WanderState());
		e.addEntity(agent);
		*/
	}
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		Point p = translator.worldToScreen(player.getX(), player.getY());
		camera.centerOn((float) p.x, (float) p.y);
		camera.drawMap();
		
		camera.translateGraphics();
		if(showNavGrid){
			navView.render(gc, g);
		}
		Color[] agent_colors = {Color.red, Color.black, Color.pink};//terrible
		int cur = 0;
		int curr = 0;
		int currr = 0;
		for (Entity en : e.getArray()) {
			spriteRender.render(en, gc, g);
			if(showAgentPaths && en instanceof Enemy){
				navView.renderAgentPath((Enemy)en, agent_colors[cur], gc, g);
				cur++;
			}
			if(showConsideredSmoothPaths && en instanceof Enemy){
				navView.renderConsideredSmoothPath((Enemy)en, agent_colors[curr], gc, g);
				curr++;
			}
			if(showConsideredUnsmoothedPaths && en instanceof Enemy){
				navView.renderConsideredUnsmoothedPath((Enemy)en, agent_colors[currr], gc, g);
				currr++;
			}
		}
		
		camera.untranslateGraphics();
		
		// now draw all UI elements
		//draw Text at Top of Screen
		DecimalFormat df = new DecimalFormat("0.00");
		String x = df.format(player.getX());
		String y = df.format(player.getY());
		g.drawString("World Coordinates: (" + x + ", " + y + ")", 100, 0);
		x = df.format(p.x - camera.cameraX);
		y = df.format(p.y - camera.cameraY);
		g.drawString("Screen Coordinates: (" + x + ", " + y + ")", 400, 0);
		g.drawString("Gems collected:  " + player.getPoints() + "/5", 150, 20);
		g.drawString("Deaths:  " + player.getDeaths() + "/5", 300, 40);
		g.drawString("Gem spotted near: x = " + (new_p.x) + ", y = " + (100-(new_p.y)), 350, 20);
		
		//draw MiniMap
		minimap.render(gc, g, camera, player);
		
		//draw Console
		if(showLog)
		consoleView.render(ConsoleLog.getInstance(), gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int t)
			throws SlickException {
		for (Entity en : e.getArray()) {
			en.update(t);
		}
		spriteRender.updateAnimations(t);
		
		if(prize.caught){
			new_p = nav.getRandomOpenTile();
			ding.play();
			prize.setLoc(nav.tileToWorld(new_p.x, new_p.y));
			ConsoleLog.getInstance().log("Prize moved to: " + new_p.toString());
			prize.caught = false;
			
			if(player.getPoints() == 5){
				music.stop();
				win.play();
				falsifyInput();
				game.enterState(1);
			}
		}
		
		if(player.getDeaths() == 5){
			music.stop();
			lose.play();
			falsifyInput();
			game.enterState(2);
		}
		
		if(player.getLoc().getX() >= (villagePoint.getX()-1) && player.getLoc().getX() <= (villagePoint.getX()+1) && player.getLoc().getY() >= (villagePoint.getY()-1) && player.getLoc().getY() <= (villagePoint.getY()+1)){
			mapName = "pathFinderForest.tmx";
			map = new TiledMap("src/"+mapName);
			camera.setMap(map);
			player.moveRight = false;
			player.moveLeft = false;
			player.moveUp = false;
			player.moveDown = false;
			game.enterState(3);
		}
	}
	private void falsifyInput(){
		player.moveLeft = false;
		player.moveRight = false;
		player.moveUp = false;
		player.moveDown = false;
		player.setDeaths(0);
		player.setPoints(0);
	}
	@Override
	public void keyPressed(int key, char c) {
		if (c == 'd') {
			player.moveRight = true;
			try {
				i = new Image("src/link.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
			spriteRender.setSprites(i);
		}
		
		if (c == 'a') {
			player.moveLeft = true;
			try {
				i = new Image("src/linkLeft.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
			spriteRender.setSprites(i);
		}
		
		if (c == 's') {
			player.moveDown = true;
			try {
				i = new Image("src/linkDown.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
			spriteRender.setSprites(i);
		}
		
		if (c == 'w') {
			player.moveUp = true;
			try {
				i = new Image("src/linkUp.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
			spriteRender.setSprites(i);
		}
		if (c == '`'){
			if(showLog){
				showLog = false;
			}
			else{
			showLog = true;
			}
		}
		if (c == '1'){
			if(showNavGrid){
				showNavGrid = false;
			}
			else{
				showNavGrid = true;
			}
		}
		if (c == '2'){
			if(showAgentPaths){
				showAgentPaths = false;
			}
			else{
				showAgentPaths = true;
			}
		}
		if (c == '3'){
			if(showConsideredSmoothPaths){
				showConsideredSmoothPaths = false;
			}
			else{
				showConsideredSmoothPaths = true;
			}
		}
		if (c == '4'){
			if(showConsideredUnsmoothedPaths){
				showConsideredUnsmoothedPaths = false;
			}
			else{
				showConsideredUnsmoothedPaths = true;
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		if (c == 'd') {
			player.moveRight = false;
		}
		if (c == 'a') {
			player.moveLeft = false;
		}
		if (c == 's') {
			player.moveDown = false;
		}
		if (c == 'w') {
			player.moveUp = false;
		}
		if (player.moveUp == false && player.moveDown == false && player.moveRight == false && player.moveLeft == false){
			try {
				spriteRender.setStand();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

}
