package turnController;


import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import UI.GameOverMenu;
import UI.Menu;
import UI.Text;
import UI.GameOverMenu.mode;
import audio.AudioMaster;
import audio.Source;
import controllers.MasterProjectileController;
import engineTester.Clock;
import entities.Background;
import entities.Entity;
import entities.HitBox;
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
	private static final int FINALWORDS = 3;
	
	public int playerAction;

	public int turn;
	
	private ArrayList<Entity> entities;

	
	public static float timeSinceLastShot;
	
	private MasterProjectileController masterProjectileController;
	private Background bg;
	private Player player;
	private PlaySpace playSpace;
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
	private float hitSoundTimer, hitTimer;
	private final float HITDELAY = 0.4f;
	
	
	// middle state keyboard controls
	private float timeSinceLastPress = 0;
	private final float DELAY = 1f;
	private MultiTyper testMultiTyper;
	
	public GameOverMenu gameOverMenu;
	private float gameOverTimer;
	
	private float finalTimer;
	private boolean finalFlag;
	
	public MasterTurnController(){
		this.turn = PLAYERTURN;
		this.masterProjectileController = new MasterProjectileController();
		this.entities = new ArrayList<Entity>();
		this.bg = new Background();
		this.player = new Player(32, 32, 32);
		player.setMenuMode();
		this.playSpace = new PlaySpace(32, 500, 250, 1216, player);		//Text mode
		playSpace.centerPlayerPosition();
		this.SB = new SpongeBob(475, 150);
		//ps.setPlayerBounds();					//Set player bounds within the space
		
		// Create the in game menuUI
		this.menu = new Menu(player, SB);
		
		this.pMask = new ProjectileMask(0,0, playSpace);
		this.pMask.Deactivate();
		
		this.turnTimer = 20;
		
		// init
		initStartEntities();
		
		//entities.add(jf);
		
		this.turnCounter = 0;
		
		
		// Soundtrack Audio
		this.buffer = AudioMaster.loadSound("audio/soundtrack.wav");
		this.source = new Source();
		this.audioTimer = 0;					// Audio Timer, when reaches 0 replay soundtrack
		
		this.soulHitBuffer = AudioMaster.loadSound("audio/Soul Hit quiet.wav");
		this.playerHitSFX = new Source();
		this.hitSoundTimer = 2;
		this.hitTimer = HITDELAY;
		
		this.testMultiTyper = new MultiTyper(800, 150, 24, new ArrayList<String>());
		
		this.gameOverMenu = new GameOverMenu(0, 0);
		this.gameOverTimer = 50f;
		
		this.finalTimer = 0;
		this.finalFlag = false;
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
			
			//Clear everything
			clearScreen();
			
			// Add the player back in
			player.setGameOverMode();							// Player will do the death animation and will be unable to move
			entities.add(player);
			// stop the music
			source.stop();
			
			
			if (gameOverTimer < 0){
				gameOverMenu.update();
				gameOverMenu.render();				
			} else {
				gameOverTimer -= Clock.Delta();
			}
			
			if (gameOverMenu.getMode() == mode.RETRY){
				entities.clear();
				initStartEntities();
				
				// relive the player
				player.reset();
				player.setHealth(92);
				this.turn = PLAYERTURN;
				
				// resume playing the music
				source.play(buffer);
				this.audioTimer = SOUNDTRACKLENGTH;
				
				this.testMultiTyper.setX(800);
				this.testMultiTyper.setY(150);
				this.testMultiTyper.clearText();
				this.testMultiTyper.hide();
				gameOverTimer = 60f;
				gameOverMenu.reset();
				this.turnCounter = 0;
				reset();
			} else if (gameOverMenu.getMode() == mode.RETURNTOMAINMENU){
				
			} else if (gameOverMenu.getMode() == mode.EXITPROGRAM){
				// nothing needs to happen here.
			}
			
			return;
		}
		
		if (turn == PLAYERTURN){
			// Switch to the Middle Turn
			if (turnCounter >= 17){
				menu.setSpongeBobDeathFlag(true);
			}
			
			if (menu.playerTurnCompleted()){
				this.turn = MIDDLE;
				this.player.disablePlayerMovement();
				this.playSpace.setPlayerBounds();
				this.menu.subMenu_options.get(0).hide();
				playSpace.centerPlayerPosition();
				playSpace.invokeTransition(DisplayManager.getWidth()/2 - 600/2, 550, 200, 600, 50);
				System.out.println("It is now the middle turn!");
															
				this.turnTimer = 50;		// default of 5 second for enemy turn (5 seconds of enemy talking)
				this.testMultiTyper.clearCurrentText();
				if (turnCounter == 0){
					this.testMultiTyper.addText("I may be a sponge");
					this.testMultiTyper.addText("but i can't absorb");
					this.testMultiTyper.addText("that.");
				} else if (turnCounter == 1){
					this.testMultiTyper.addText("Our reports show");
					this.testMultiTyper.addText("a distortion in the");
					this.testMultiTyper.addText("space time continuum");
					playSpace.invokeTransition(DisplayManager.getWidth()/2 - 600/2, 300, 400, 600, 50);
				} else if (turnCounter == 2) {
					this.testMultiTyper.addText("But I won't bore you");
					this.testMultiTyper.addText("with the details");
				} else if (turnCounter == 3){
					this.testMultiTyper.addText("Since we both know");
					this.testMultiTyper.addText("You're the one behind");
					this.testMultiTyper.addText("it");
				} else if (turnCounter == 4){
					this.testMultiTyper.addText("I remember a time");
					this.testMultiTyper.addText("when i called");
					this.testMultiTyper.addText("you friend...");
					
				} else if (turnCounter == 5){
					this.testMultiTyper.addText("Even if you don't");
				} else if (turnCounter == 6){ 
					this.testMultiTyper.addText("But i guess that's just");
					this.testMultiTyper.addText("what that is, a memory.");
				} else if (turnCounter == 7){
					this.testMultiTyper.addText("do you know what it is like?");
				} else if (turnCounter == 8){
					this.testMultiTyper.addText("To live your life in");
					this.testMultiTyper.addText("fear knowing its all");
					this.testMultiTyper.addText("just going to be erased.");
					playSpace.invokeTransition(DisplayManager.getWidth()/2 - 300/2, 400, 300, 350, 50);
				} else if (turnCounter == 9){
					this.testMultiTyper.addText("Eventually I just");
					this.testMultiTyper.addText("stopped caring.");
				} else if (turnCounter == 10){
					this.testMultiTyper.addText("But after knowing");
					this.testMultiTyper.addText("what happens next...");
				} else if (turnCounter == 11){
					this.testMultiTyper.addText("Well let's just say i'm");
					this.testMultiTyper.addText("only doing this for your");
					this.testMultiTyper.addText("sake and mine at this point.");
				} else if (turnCounter == 12){
					this.testMultiTyper.addText("Okay lets do it");
					this.testMultiTyper.addText("for real");
				} else if (turnCounter == 13){
					this.testMultiTyper.addText("As i said before all this.");
					this.testMultiTyper.addText("I was hoping we could be");
					this.testMultiTyper.addText("friends");
				} else if (turnCounter == 14){
					this.testMultiTyper.addText("I assumed you would have");
					this.testMultiTyper.addText("stop once you had what you");
					this.testMultiTyper.addText("wanted.");
					playSpace.invokeTransition(DisplayManager.getWidth()/2 - 600/2, 300, 400, 600, 50);
				} else if (turnCounter == 15){
					this.testMultiTyper.addText("You're just gonna make us");
					this.testMultiTyper.addText("live on repeast like we're");
					this.testMultiTyper.addText("in a sort of whirl pool.");
				} else {
					this.testMultiTyper.addText("This is it");
					this.testMultiTyper.addText("Hell or High Water!");
				}
				this.testMultiTyper.show();
			} 
			
		} else if (turn == ENEMYTURN){
			// Here the player will be dodging the projectiles spawned
			enemyTurnHandler();

		} else if (turn == MIDDLE){
			// Here Insert the dialogue that spongebob will have
			
			// render the image of the player
			this.player.show();
			
			turnTimer -= Clock.Delta();
			
			// If the player wants to skip typing out the dialogueo
			if (timeSinceLastPress > DELAY*2){
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
					this.player.setColor(new Vector3f(0,0,1));
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()-400, 706, 0f, 0f, 2f, 0, 0, 10f, 1, 32);
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()+400, 706, 0f, 0f, 2f, (float) Math.PI, 0, 10f, 1, 32);
					this.pMask.Activate();
				}else if (turnCounter == 1){
					this.player.setColor(new Vector3f(1,0,0));
					int i = 0;
					while (i < 12){
						masterProjectileController.addJellyFishProjectile(10 * i, 128, 90, 10, player, 256, (float) (Math.random() * 360));
						i++;
					}
					this.turnTimer = 150;
				} else if (turnCounter == 2){
					this.player.setColor(new Vector3f(0,0,1));
					masterProjectileController.addJellyFishProjectile(200, 570, 40, 128, 0, 150, 10);
					masterProjectileController.addMulitSpiralProjectileSpawner(200, 706, 0f, 0f, 1f, 0, 0, 15f, 1, 32);
					this.turnTimer = 100;
					this.pMask.Activate();
				} else if (turnCounter == 3) {
					int i = 0;
					while (i < 10){
						if (i % 2 == 0){
							masterProjectileController.addExplosionProjectileSpawner(640, 300, 3 * i, 0, 20, 32);
						} else {
							masterProjectileController.addExplosionProjectileSpawner(640, 300, 3 * i, (float) (Math.PI/20), 20, 32);
						}
						i++;
					}
					this.turnTimer = 70;
					this.pMask.Deactivate();
				} else if (turnCounter == 4){
					// This one has gravity reverse
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()-400-16, 706, 0f, 0f, 1f, 0, 0, 15f, 1, 32);
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()+400-16, 550, 0f, 0f, 1f, (float) Math.PI, 0, 15f, 1, 32);
					this.pMask.Activate();
				} else if (turnCounter == 5){
					this.player.setColor(new Vector3f(0,0,1));
					masterProjectileController.addMulitSpiralProjectileSpawner(200, 675, 0f, 0f, 1f, 0, 0, 15f, 1, 64);
					masterProjectileController.addJellyFishProjectile(30, 128, 90, 10, player, 256, 90);
					masterProjectileController.addJellyFishProjectile(60, 128, 90, 10, player, 256, 90);
					masterProjectileController.addJellyFishProjectile(90, 128, 90, 10, player, 256, 90);
					this.pMask.Activate();
				} else if (turnCounter == 6){
					// This one has gravity reverse
					this.player.setColor(new Vector3f(0,0,1));
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()-400-16, 675, 0f, 0f, 1f, 0, 0, 15f, 1, 64);
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()+400-16, 550, 0f, 0f, 1f, (float) Math.PI, 0, 15f, 1, 64);
					this.pMask.Activate();
				} else if (turnCounter == 7) {
					this.player.setColor(new Vector3f(0,0,1));
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()-400-16, 675, 0f, 0f, 1f, 0, 0, 15f, 1, 64);
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()+400-16, 675, 0f, 0f, 1f, (float) Math.PI, 0, 15f, 1, 64);
					this.pMask.Activate();
				} else if (turnCounter == 8){
					this.player.setColor(new Vector3f(0,0,1));
					this.turnTimer = 200;
					masterProjectileController.addMulitSpiralProjectileSpawner(500, 300, 0f, 0f, 1f, (float) (Math.PI/2), 0, 15f, 1, 64);			
					masterProjectileController.addMulitSpiralProjectileSpawner(850, 400, 0f, 0f, 1f, (float) Math.PI, 0, 15f, 1, 64); 	
					masterProjectileController.addMulitSpiralProjectileSpawner(700+32, 750, 0f, 0f, 1f, (float) (3*Math.PI/2), 0, 15f, 1, 64);
					masterProjectileController.addMulitSpiralProjectileSpawner(400, 692-64, 0f, 0f, 1f, 0, 0, 15f, 1, 64);	
				} else if (turnCounter == 9){
					this.player.setColor(new Vector3f(0,0,1));
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()-400-16, 675, 0f, 0f, 1f, 0, 0, 15f, 1, 64);
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()+400-16, 675, 0f, 0f, 1f, (float) Math.PI, 0, 15f, 1, 64);
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()+400-16, 550, 0f, 0f, 1f, (float) Math.PI, 0, 15f, 1, 64);
					masterProjectileController.addMulitSpiralProjectileSpawner(player.getX()-400-16, 550, 0f, 0f, 1f, 0, 0, 15f, 1, 64);
					this.pMask.Activate();
				} else if (turnCounter == 10) {		
					this.player.setColor(new Vector3f(1,0,0));
					int i = 0;
					int limit = 20;
					while (i < limit){
						masterProjectileController.addExplosionProjectileSpawner((float) (Math.random() * 1200), (float) (Math.random() * 400), (float) (Math.random() * 50), 0, 20, 32);
						i += 1;
					}
					this.turnTimer = 80;
					this.pMask.Deactivate();
				} else if (turnCounter == 11){
					this.player.setColor(new Vector3f(1,0,0));
					masterProjectileController.addMulitSpiralProjectileSpawner(0, 75, 0.5f, 0f, 1f, 0f, 17, 1f, 2, 32);
					masterProjectileController.addMulitSpiralProjectileSpawner(1280, 75, 0.5f, (float) Math.PI, 1f, 0f, 17, 1f, 2, 32);
					this.pMask.Deactivate();
				} else if (turnCounter == 12){
					this.player.setColor(new Vector3f(1,0,0));
					masterProjectileController.addMulitSpiralProjectileSpawner(0, 75, 1f, 0f, 1f, 0f, 17, 1f, 2, 64);
					masterProjectileController.addMulitSpiralProjectileSpawner(1280, 75, 1f, (float) Math.PI, 1f, 0f, 17, 1f, 2, 64);
					this.pMask.Deactivate();
				} else if (turnCounter == 13){
					this.player.setColor(new Vector3f(1,0,0));
					masterProjectileController.addMulitSpiralProjectileSpawner(0, 75, 0.5f, 0f, 1f, 0f, 17, 1f, 3, 32);
					masterProjectileController.addMulitSpiralProjectileSpawner(1280, 75, 0.5f, (float) Math.PI, 1f, 0f, 17, 1f, 3, 32);
					this.pMask.Deactivate();
				} else if (turnCounter == 14){
					this.player.setColor(new Vector3f(1,0,0));
					int i = 0;
					while (i < 5){
						int j = 0;
						while (j < 8){
							masterProjectileController.addJellyFishProjectile(20 * i, 64, 90, 10, player, 256, 45 * j);
							j++;
						}
						i++;
					}
					this.turnTimer = 120;
				} else if (turnCounter == 15){
					this.player.setColor(new Vector3f(1,0,0));
					int i = 0;
					while (i < 12){
						masterProjectileController.addJellyFishProjectile(10 * i, 128, 90, 10, player, 256, (float) (Math.random() * 360));
						i++;
					}
					this.turnTimer = 150;
				} else {
					this.player.setColor(new Vector3f(1,0,0));
					int i = 0;
					int limit = 20;
					while (i < limit){
						masterProjectileController.addExplosionProjectileSpawner((float) (Math.random() * 1200), (float) (Math.random() * 400), (float) (Math.random() * 50), 0, 20, 32);
						i += 1;
					}
					this.turnTimer = 50;
					this.pMask.Deactivate();
				}
				
				this.testMultiTyper.hide();
				this.testMultiTyper.clearText();
			} 
			
		} else if (turn == FINALWORDS){
			if (finalTimer == 0){
				this.testMultiTyper.clearCurrentText();
				this.testMultiTyper.addText("Welp, this is it.");
				this.testMultiTyper.show();
			} else if (finalTimer > 50 && finalFlag == false){
				this.testMultiTyper.clearText();
				this.testMultiTyper.addText("Just don't say i didn't warn ya");
				this.testMultiTyper.show();
				finalFlag = true;
			} else if (finalTimer > 80 && finalFlag == true){
				this.testMultiTyper.clearText();
				this.player.setHealth(0);
				finalFlag = false;
			}
			
			this.finalTimer += Clock.Delta();
		}

		
		// render the mask for the play space
		// mask is to hide entities
		pMask.update();
		pMask.render();
		
		// Jelly Fish need to be above the mask
		masterProjectileController.updateJellyFish();

		
		if (SB.isDead()){
			source.stop();
			this.turn = FINALWORDS;
			return;
		}
		
		
		// audio handler to replay/loop the music
		if (audioTimer < 0){
			source.play(buffer);
			audioTimer = SOUNDTRACKLENGTH;
		} else {
			audioTimer -= Clock.Delta();
		}
		
		
	}
	
	public void clearScreen(){
		entities.clear();
		clearProjectileSpawners();
		this.pMask.Deactivate();
	}
	
	public void gameSetPlayerTurn(){
		this.turnCounter++;
		this.turn = PLAYERTURN;
		this.player.setMenuMode();
		this.player.resetBounds();
		player.setGravityDirection(0); 						// Set gravity direction to down
		playSpace.TextMode();
		this.pMask.Deactivate();
		this.menu.subMenu_options.get(0).show();
		this.menu.subMenu_options.get(0).setText("You feel the Ocean weighting you down");
		menu.status = false;
		menu.mode = 0;
		clearProjectileSpawners();
		System.out.println("It is now the player turn!");
	}
	
	public void initStartEntities(){
		entities.add(SB);						// SB is SpongeBob
		entities.add(playSpace);
		entities.add(menu);						// menu attack animation is above Spongebob
		entities.add(player);
	}
	
	// Respawns projectiles.
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
			if (2*d< projectiles.get(i).size + player.size){					// TODO fix collision (It's a bit weird)
				//System.out.println("hit");									// It seems to detect a collision when there isn't really one there.
				if (hitSoundTimer < 0){
					playerHitSFX.play(soulHitBuffer);
					hitSoundTimer = 2;
				}
				if (hitTimer < 0){
					player.decrementHealth();
					hitTimer = HITDELAY;
				}
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

	public void reset() {
		entities.clear();
		initStartEntities();
		
		// relive the player
		player.reset();
		player.setHealth(92);
		this.turn = PLAYERTURN;
		
		// resume playing the music
		source.play(buffer);
		this.audioTimer = SOUNDTRACKLENGTH;
		
		this.testMultiTyper.setX(800);
		this.testMultiTyper.setY(150);
		this.testMultiTyper.clearText();
		this.testMultiTyper.hide();
		gameOverTimer = 60f;
		gameOverMenu.reset();
		this.turnCounter = 0;
		this.player.stats.reset();
		this.turn = PLAYERTURN;
	}
	
	public void enemyTurnHandler(){
		turnTimer -= Clock.Delta();
		
		// change player gravity direction while in the middle of dodging projectiles
		if (turnCounter == 4 || turnCounter == 6){
			if (turnTimer < 75 && this.player.getGravityDirection() != this.player.UP){
				this.player.setGravityDirection(this.player.UP);
				this.player.gravity = 5;
				this.player.playSlamSFXOnNextChange();
				this.SB.playAnimationUp();
			}
		}
		
		if (turnCounter == 8){
			
			if (turnTimer < 50 && this.player.getGravityDirection() == this.player.LEFT){
				this.player.setGravityDirection(this.player.RIGHT);
				this.player.gravity = 5;
				this.player.playSlamSFXOnNextChange();
				this.SB.playAnimationLeft();
			} else if (turnTimer < 100 && this.player.getGravityDirection() == this.player.UP){
				this.player.setGravityDirection(this.player.LEFT);
				this.player.gravity = 5;
				this.player.playSlamSFXOnNextChange();
				this.SB.playAnimationLeft();
			} else if (turnTimer < 150 && this.player.getGravityDirection() == this.player.DOWN){
				this.player.setGravityDirection(this.player.UP);
				this.player.gravity = 5;
				this.player.playSlamSFXOnNextChange();
				this.SB.playAnimationUp();
			}
		}
		
		// Switch back to the player turn
		// REMOVE C KEY
		if (Keyboard.isKeyDown(Keyboard.KEY_C) || turnTimer <= 0){
			gameSetPlayerTurn();
		} else {
			masterProjectileController.update();
			detectCollision(player, masterProjectileController);
			hitSoundTimer -= Clock.Delta();
			hitTimer -= Clock.Delta();
		}
	}
	
}
