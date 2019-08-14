package turnController;


import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import UI.Menu;
import audio.AudioMaster;
import audio.Source;
import controllers.MasterProjectileController;
import engineTester.Clock;
import entities.Background;
import entities.Entity;
import entities.JellyFish;
import entities.MultiTyper;
import entities.PlaySpace;
import entities.Player;
import entities.Projectile;
import entities.ProjectileMask;
import entities.SpongeBob;
import entities.Typer;
import renderEngine.DisplayManager;

public class MasterTurnController {

	private static final int PLAYERTURN = 0;
	private static final int MIDDLE = 1;
	private static final int ENEMYTURN = 2;
	
	public int playerAction;

	public int turn;
	
	private ArrayList<Entity> entities;

	
	public static float timeSinceLastShot;
	
	private MasterProjectileController masterProjectileController;
	private Background bg;
	private Player player;
	private PlaySpace ps;
	private JellyFish jf;
	private Menu menu;
	private SpongeBob SB;
	private ProjectileMask pMask;
	private float turnTimer;
	private int turnCounter;
	
	// Soundtrack Audio
	private int buffer;
	private Source source;
	private float audioTimer;			// Used to replay the soundtrack
	private final float SOUNDTRACKLENGTH = 1500;
	
	// 
	private int soulHitBuffer;
	private Source playerHitSFX;
	private float hitTimer;
	private final float HITDELAY = 1;
	
	
	// middle state keyboard controls
	private float timeSinceLastPress = 0;
	private final float DELAY = 1f;
	private MultiTyper testMultiTyper;
	
	
	public MasterTurnController(){
		this.turn = PLAYERTURN;
		this.masterProjectileController = new MasterProjectileController();
		this.entities = new ArrayList<Entity>();
		this.bg = new Background();
		this.player = new Player(32, 32, 32);
		player.setMenuMode();
		this.ps = new PlaySpace(32, 500, 250, 1216, player);		//Text mode
		ps.centerPlayerPosition();
		this.SB = new SpongeBob(475, 150);
		//ps.setPlayerBounds();					//Set player bounds within the space
		
		// Create the in game menuUI
		this.menu = new Menu(player, SB);
		
		this.pMask = new ProjectileMask(0,0, ps);
		this.pMask.Deactivate();
		
		this.turnTimer = 20;
		
		
		
		// init
		initStartEntities();
		
		jf = new JellyFish(100, 100, 32, 0, 10);
		
		//entities.add(jf);
		
		this.turnCounter = 0;
		
		
		// Soundtrack Audio
		this.buffer = AudioMaster.loadSound("audio/soundtrack.wav");
		this.source = new Source();
		this.audioTimer = 0;					// Audio Timer, when reaches 0 replay soundtrack
		
		this.soulHitBuffer = AudioMaster.loadSound("audio/Soul Hit quiet.wav");
		this.playerHitSFX = new Source();
		this.hitTimer = HITDELAY;
		
		
		
		this.testMultiTyper = new MultiTyper(800, 150, 24, new ArrayList<String>());
	}
	
	public void update(){
		
		//background
		bg.update();
		bg.render();
		
		
		this.testMultiTyper.update();
		this.testMultiTyper.render();
		
		for (Entity entity:entities){
			entity.update();
			entity.render();
		}
		
		if (player.getHealth() <= 0){
			// Go to Game Over screen
			//System.out.println("Game Over");
			
			//Clear everything
			entities.clear();
			clearProjectileSpawners();
			this.pMask.Deactivate();
			
			// Add the player back in
			player.setGameOverMode();							// Player will do the death animation	
			entities.add(player);
			// stop the music
			source.stop(buffer);
			
			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
				entities.clear();
				initStartEntities();
				
				// relive the player
				player.relive();
				player.setHealth(92);
				this.turn = PLAYERTURN;
				
				// resume playing the music
				source.play(buffer);
				this.audioTimer = SOUNDTRACKLENGTH;
			}
			
			return;
		}
		
		if (turn == PLAYERTURN){
			// Switch to the Middle Turn
			if (menu.playerTurnCompleted()){
				this.turn = MIDDLE;
				this.player.disablePlayerMovement();
				this.ps.setPlayerBounds();
				this.menu.subMenu_options.get(0).hide();
				ps.centerPlayerPosition();
				ps.invokeTransition(DisplayManager.getWidth()/2 - 600/2, 550, 200, 600, 50);
				System.out.println("It is now the middle turn!");
															
				this.turnTimer = 50;		// default of 5 second for enemy turn (5 seconds of enemy talking)
				this.testMultiTyper.clearCurrentText();
				if (turnCounter == 0){
					this.testMultiTyper.addText("I forgive you...");
				} else if (turnCounter == 1){
					this.testMultiTyper.addText("I forgive you...");
					this.testMultiTyper.addText("Even if you don't");
				} else {
					this.testMultiTyper.addText("I need more lines");
				}
				this.testMultiTyper.show();
			} 
			
		} else if (turn == ENEMYTURN){
			// Here the player will be dodging the projectiles spawned
			
			turnTimer -= Clock.Delta();
			
			// change player gravity direction while in the middle of dodging projectiles
			if (turnCounter == 2){
				if (turnTimer < 75 && this.player.getGravityDirection() != this.player.UP){
					this.player.setGravityDirection(this.player.UP);
					this.player.gravity = 5;
					this.player.playSlamSFXOnNextChange();
					this.SB.playAnimationUp();
				}
			}
			
			// Switch back to the player turn
			if (Keyboard.isKeyDown(Keyboard.KEY_C) || turnTimer <= 0){
				gameSetPlayerTurn();
			} else {
				masterProjectileController.update();
				detectCollision(player, masterProjectileController);
				hitTimer -= Clock.Delta();
			}
			
		} else if (turn == MIDDLE){
			// Here Insert the dialogue that spongebob will have
			
			// render the image of the player
			this.player.show();
			
			turnTimer -= Clock.Delta();
			
			// If the player wants to skip typing out the dialogueo
			if (timeSinceLastPress > DELAY){
				if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
					if (this.testMultiTyper.finishedTyping()){
						turnTimer = 0;
					} else {
						this.testMultiTyper.skipTyping();
					}
				}
				timeSinceLastPress = 0;
			} else {
				timeSinceLastPress += Clock.Delta();
			}
			
			
			// Switch to the enemy turn (dodge projectiles)
			if (turnTimer <= 0){
				
				this.turn = ENEMYTURN;
				this.turnTimer = 150;
				this.player.setPlayMode();
				
				if (turnCounter == 0){
					masterProjectileController.addMulitSpiralProjectileSpawner(200, 675, 0f, 0f, 1f, 0, 0, 15f, 1);
					this.pMask.Activate();
				} else if (turnCounter == 1) {
					masterProjectileController.addMulitSpiralProjectileSpawner(200, 675, 0f, 0f, 1f, 0, 0, 15f, 1);
					masterProjectileController.addMulitSpiralProjectileSpawner(1000, 675, 0f, 0f, 1f, (float) Math.PI, 0, 15f, 1);
					this.pMask.Activate();
				} else if (turnCounter == 2){
					masterProjectileController.addMulitSpiralProjectileSpawner(200, 675, 0f, 0f, 1f, 0, 0, 15f, 1);
					masterProjectileController.addMulitSpiralProjectileSpawner(1000, 550, 0f, 0f, 1f, (float) Math.PI, 0, 15f, 1);
					this.pMask.Activate();
				} else if (turnCounter < 5) {
					
					this.player.setColor(new Vector3f(1,0,0));
					int i = 0;
					int limit = 20;
					while (i < limit){
						masterProjectileController.addExplosionProjectileSpawner((float) (Math.random() * 1200), (float) (Math.random() * 400), (float) (Math.random() * 50), 20, 32);
						i += 1;
					}
					this.turnTimer = 50;
					this.pMask.Deactivate();
				} else {
					masterProjectileController.addMulitSpiralProjectileSpawner(400, 400, 0f, 0f, 1f, 0f, 20, 1f, 2);
					this.pMask.Deactivate();
				}
				this.testMultiTyper.hide();
				this.testMultiTyper.clearText();
			} 
			
		}

		
		// render the mask for the play space
		// mask is to hide entities
		pMask.update();
		pMask.render();
		
		//clean_up_entities(entities);

		
		// audio handler
		if (audioTimer < 0){
			source.play(buffer);
			audioTimer = SOUNDTRACKLENGTH;
		} else {
			audioTimer -= Clock.Delta();
		}
	}
	
	public void gameSetPlayerTurn(){
		this.turnCounter++;
		this.turn = PLAYERTURN;
		this.player.setMenuMode();
		this.player.resetBounds();
		player.setGravityDirection(0); 						// Set gravity direction to down
		ps.TextMode();
		this.pMask.Deactivate();
		this.menu.subMenu_options.get(0).show();
		this.menu.subMenu_options.get(0).setText("You feel the Ocean weighting you down");
		menu.status = false;
		menu.mode = 0;
		//clearProjectiles(entities);
		clearProjectileSpawners();
		System.out.println("It is now the player turn!");
	}
	
	public void initStartEntities(){
		entities.add(ps);
		entities.add(SB);
		entities.add(menu);						// menu attack animation is above Spongebob
		entities.add(player);
	}
	
	//Respawns projectiles.
	public static void entity_controller(ArrayList<Entity> entities){
		if (entities.size() <= 100){
			Random r = new Random();
			Projectile p = new Projectile(320, 10, 1, (float) (r.nextFloat() * Math.PI), 32, 0);
			entities.add(p);
		}
	}
	
	
	
	public static void clean_up_entities(ArrayList<Entity> entities){
		for (int i = 1; i < entities.size(); i++){
			Entity entity = entities.get(i);
			if (entity instanceof Projectile && entity.isOutOfBounds()){
				entities.remove(i);
				entity = null;
			} 
		}
	}
	
	public static void clearProjectiles(ArrayList<Entity> entities){
		int i = 0;
		while (i < entities.size()){
			Entity entity = entities.get(i);
			if (entity instanceof Projectile){
				entities.remove(i);
				entity = null;
			} else {
				i += 1;
			}
		}
	}
	
	public void clearProjectileSpawners(){
		masterProjectileController.clearSpawners();
	}
	
	public void detectCollision(Player player, MasterProjectileController masterProjectileController){
		ArrayList<Projectile> projectiles = masterProjectileController.getAllProjectiles();
		for (int i = 0; i < projectiles.size(); i++){
			float d = distance(player, projectiles.get(i));
			if (2*d< projectiles.get(i).size + player.size){				// TODO fix collision (It's a bit weird)
				//System.out.println("hit");									// It seems to detect a collision when there isn't really one there.
				if (hitTimer < 0){
					playerHitSFX.play(soulHitBuffer);
					hitTimer = HITDELAY;
				}
				player.decrementHealth();
				return;
			}
		}
	}
	
	public float distance(Player player, Projectile projectile){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		//Shapes.draw_line(player.getX() + player.size/2, player.getY()+player.size/2, projectile.getX()+projectile.size/2, projectile.getY()+projectile.size/2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		float dx = player.getX() + player.size/2 - (projectile.getX()+projectile.size/2);
		float dy = player.getY() + player.size/2 - (projectile.getY()+projectile.size/2);
		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	
}
