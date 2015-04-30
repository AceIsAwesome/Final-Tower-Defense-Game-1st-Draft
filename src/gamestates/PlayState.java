package gamestates;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import math.Point2D;
import model.World;
import model.entities.Bullet;
import model.entities.Enemy;
import model.entities.Entity;
import model.entities.Player;
import model.entities.Tower;
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

import enemystates.AttackState;
import pathfinding.NavGraph;
import view.ConsoleView;
import view.CoordinateTrans;
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
	private int spawnTimer = 0;
	private int spawnTime = 150;
	
	private CoordinateTranslator translator;
	private CoordinateTrans trans;
	
	private Music music;
	private int deaths = 0;
	private int points = 0;
	
	private Sound win;
	private Sound lose;
	private Sound ding;
	
	private World world;
	private Player player;
	private Tower tower;
	private NavGraph nav;
	private String mapName = "";
	private String musicName = "";
	
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
		world = new World(30, 30);
		translator = new CoordinateTranslator(world.getWorldW(), world.getWorldH(), camera.mapWidth, camera.mapHeight);
		trans = new CoordinateTrans(camera.mapWidth, camera.mapHeight, world.getWorldW(), world.getWorldH(), 0, 600);
		nav = new NavGraph(map, world);
		//Load Entities
		e = new EntityManager();
		player = new Player(15, 15, .5, .01, world, nav, e);

		e.addEntity(player);
		
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
		
		win = new Sound("src/youWin.ogg");
		lose = new Sound("src/youLost.ogg");
		ding = new Sound("src/chime.ogg");
		
		music = new Music("src/"+musicName);
		music.setVolume(0.5f);
		music.loop();
		
		
	}
	private void addAgents() throws SlickException{
		
		Enemy agent = new Enemy(14, 29, .25, .0095, 10, world, nav);
		e.addEntity(agent);
		/*Enemy agent1 = new Enemy(15, 29, .25, .0095, 10, world, nav);
		e.addEntity(agent1);
		Enemy agent2 = new Enemy(16, 29, .25, .0095, 10, world, nav);
		e.addEntity(agent2);
		Enemy agent3 = new Enemy(9, 24, .25, .0095, 10, world, nav);
		e.addEntity(agent3);
		Enemy agent4 = new Enemy(10, 24, .25, .0095, 10, world, nav);
		e.addEntity(agent4);
		Enemy agent5 = new Enemy(11, 24, .25, .0095, 10, world, nav);
		e.addEntity(agent5);
		Enemy agent6 = new Enemy(12, 24, .25, .0095, 10, world, nav);
		e.addEntity(agent6);
		Enemy agent7 = new Enemy(13, 24, .25, .0095, 10, world, nav);
		e.addEntity(agent7);
		Enemy agent8 = new Enemy(14, 24, .25, .0095, 10, world, nav);
		e.addEntity(agent8);*/
		
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
	
private void addBullet(double d, double f) throws SlickException{
		
		Bullet bullet = new Bullet(d, f, 1, .02, world, nav, e);
		e.addEntity(bullet);
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
		Color[] agent_colors = {Color.red, Color.black, Color.pink,};//terrible
		for (Entity en : e.getArray()) {
			if(en instanceof Enemy || en instanceof Tower || en instanceof Bullet){
			spriteRender.render(en, gc, g);
			}
			if(showAgentPaths && en instanceof Enemy){
				navView.renderAgentPath((Enemy)en, agent_colors[0], gc, g);
			}
			if(showConsideredSmoothPaths && en instanceof Enemy){
				navView.renderConsideredSmoothPath((Enemy)en, agent_colors[1], gc, g);
			}
			if(showConsideredUnsmoothedPaths && en instanceof Enemy){
				navView.renderConsideredUnsmoothedPath((Enemy)en, agent_colors[2], gc, g);
			}
		}
		
		camera.untranslateGraphics();
		
		//draw Console
		if(showLog)
		consoleView.render(ConsoleLog.getInstance(), gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int t)
			throws SlickException {
		ArrayList<Entity> enlist = new ArrayList<Entity>();
		enlist = e.getArray();
		for(int i = 0; i < e.getArray().size(); i++)
		{
			if(enlist.get(i).isDead()){
				deaths++;
				player.setDeaths(deaths);
				enlist.remove(i);
				i--;
			}
			if(enlist.get(i).wasKilled()){
				points++;
				player.setPoints(points);
				enlist.remove(i);
				i--;
			}
			if(enlist.get(i) instanceof Tower){
				if(((Tower)enlist.get(i)).getCanFire()){
					addBullet(enlist.get(i).getX(), enlist.get(i).getY());
					((Tower)enlist.get(i)).setCanFire(false);
				}
			}
		}
		spawnTimer++;
		if(spawnTimer >= spawnTime){
			addAgents();
			spawnTimer = 0;
			if(spawnTime > 1){
				spawnTime--;
			}
		}
		
		
		for (Entity en : e.getArray()) {
			en.update(t);
		}
		spriteRender.updateAnimations(t);
		
			
		if(player.getPoints() == 20){
			music.stop();
			win.play();
			falsifyInput();
			game.enterState(1);
		}
		
		if(player.getDeaths() >= 5){
			music.stop();
			lose.play();
			falsifyInput();
			game.enterState(2);
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
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
		Point2D p = new Point2D();
		p = translator.screenToWorld(x, y);
		 try {
			tower = new Tower((p.getX()+(camera.cameraX/32)), (p.getY()-(camera.cameraY/32)), 1, 20, world, nav, e);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //System.out.println("cmaera x = "+camera.cameraX+"; y = "+camera.cameraY);
		 e.addEntity(tower);
	}

}
