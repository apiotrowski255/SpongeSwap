package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import audio.AudioMaster;
import audio.Source;
import engineTester.Clock;
import shapes.Shapes;

public class JellyFish extends Entity{

	private final int WAIT = 0;
	private final int TRAVEL = 1;
	private final int PREPARE = 2;
	private final int SHOOT = 3;
	
	private Texture texture;
	private float size, targetDirection, currentDirection, timer;
	private int state;
	private float timeSinceLastShot;
	private float currentX, currentY;
	private float deltaX, deltaY, deltaAngle;
	private float travelTime;
	private float delta = 1;
	private float delay;
	private ArrayList<Projectile> projectiles;
	
	// sound effects for the jelly fish
	private int gasterBlasterChargeBuffer;
	private Source gasterBlasterChargeSFX;
	private boolean oncePlayed;
	
	private int gasterBlasterBlastBuffer;
	private Source gasterBlasterBlastSFX;
	
	
	private Player player;
	// direction is in degrees (not radians)

	
	public JellyFish(float x, float y, float delay, float size, float direction, float travelTime, float timer, Player player) {
		super(x, y); 	// x and y are treated as the target coordinates of the jellyfish.
		this.size = size;
		this.targetDirection = direction;
		this.currentDirection = direction - 360;
		this.texture = Shapes.LoadTexture("res/JellyFish_prepare.png", "PNG");
		this.timer = timer;
		this.state = WAIT;
		this.timeSinceLastShot = 0;
		this.currentX = 0;
		this.currentY = 0;
		this.deltaX = Math.abs(this.x - this.currentX)/travelTime;
		this.deltaY = Math.abs(this.y - this.currentY)/travelTime;
		this.deltaAngle = 360/travelTime;
		this.travelTime = travelTime;
		this.projectiles = new ArrayList<Projectile>();
		this.delay = delay;
		
		
		this.gasterBlasterChargeBuffer = AudioMaster.loadSound("audio/Gaster Blaster Charge.wav");
		this.gasterBlasterChargeSFX = new Source();
		this.oncePlayed = false;
		
		this.gasterBlasterBlastBuffer =  AudioMaster.loadSound("audio/Gaster Blaster Blast.wav");
		this.gasterBlasterBlastSFX = new Source();
		
		this.player = player;
	}
	
	
	public JellyFish(float x, float y, float delay, float size, float direction, float travelTime, float timer) {
		super(x, y); 	// x and y are treated as the target coordinates of the jellyfish.
		this.size = size;
		this.targetDirection = direction;
		this.currentDirection = direction - 360;
		this.texture = Shapes.LoadTexture("res/JellyFish_prepare.png", "PNG");
		this.timer = timer;
		this.state = WAIT;
		this.timeSinceLastShot = 0;
		this.currentX = 0;
		this.currentY = 0;
		this.deltaX = Math.abs(this.x - this.currentX)/travelTime;
		this.deltaY = Math.abs(this.y - this.currentY)/travelTime;
		this.deltaAngle = 360/travelTime;
		this.travelTime = travelTime;
		this.projectiles = new ArrayList<Projectile>();
		this.delay = delay;
		
		
		this.gasterBlasterChargeBuffer = AudioMaster.loadSound("audio/Gaster Blaster Charge.wav");
		this.gasterBlasterChargeSFX = new Source();
		this.oncePlayed = false;
		
		this.gasterBlasterBlastBuffer =  AudioMaster.loadSound("audio/Gaster Blaster Blast.wav");
		this.gasterBlasterBlastSFX = new Source();
		
		this.player = null;
	}

	@Override
	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1,1,1);
		Shapes.DrawQuadTexRot(texture, currentX, currentY, size, size, currentDirection);
		
	}

	@Override
	public void update() {
		if (state == WAIT){
			if (this.delay < 0){
				if (this.player != null){
					super.setX(this.player.getX() - 48);
					this.deltaX = Math.abs(this.x - this.currentX)/travelTime;
					this.deltaY = Math.abs(this.y - this.currentY)/travelTime;
				}
				state = TRAVEL;
			} else {
				this.delay -= Clock.Delta();
			}
			
		} else if (state == TRAVEL){
			if (currentX < super.getX()) {
				currentX += this.deltaX;
			} else if (currentX > super.getX()) {
				currentX -= this.deltaX;
			}
			
			if (currentY < super.getY()) {
				currentY += this.deltaY;
			} else if (currentY > super.getX()) {
				currentY -= this.deltaY;
			}
			
			if(currentDirection < targetDirection) {
				currentDirection += deltaAngle;
			} else if (currentDirection > targetDirection) {
				currentDirection -= deltaAngle;
			}
			
			// If close Enought to target then set current position to target position.
			closeEnoughToTarget();
			
			if (currentX == super.getX() && currentY == super.getY()){
				this.currentDirection = targetDirection;
				this.state = PREPARE;
			}
			
			if (oncePlayed == false){
				this.gasterBlasterChargeSFX.play(gasterBlasterChargeBuffer);
				oncePlayed = true;
			}
			
			
		} else if (state == PREPARE) {
			if (timeSinceLastShot >= timer){
				shoot();
				state = SHOOT;
				timeSinceLastShot = 0;
				this.deltaX = (float) (3 * Math.cos(currentDirection * Math.PI / 180));
				this.deltaY = (float) (3 * Math.sin(currentDirection * Math.PI / 180));
				this.gasterBlasterBlastSFX.play(gasterBlasterBlastBuffer);
			} else {
				timeSinceLastShot += Clock.Delta();
			}
		} else if (state == SHOOT) {
			// projectiles have already been spawned, the jelly fish needs to move back in the direction it's facing.
			
			currentX -= this.deltaX;
			currentY -= this.deltaY;
		}
		
		
		for (Projectile p : projectiles){
			p.update();
			p.render();
		}
	}
	
	public void shoot(){
		this.texture = Shapes.LoadTexture("res/JellyFish_shoot.png", "PNG");
		float radian_angle = (float) ((this.currentDirection) * Math.PI / 180);
		int projectileSize = 64;
		int projectileRadius = projectileSize/2;
		for (int i = 1; i < 7; i++){
			shootProjectile(super.getX() + projectileRadius, super.getY() + projectileRadius, i, radian_angle, projectileSize);
		}

	}
	
	public void shootProjectile(float x, float y, float projectileSpeed, float angle, int size){
		Projectile p = new Projectile(x, y, projectileSpeed, angle, size, (float) 0.0);
		projectiles.add(p);
	}

	public ArrayList<Projectile> getProjectiles(){
		return this.projectiles;
	}
	
	public void closeEnoughToTarget(){
		if (Math.abs(currentX - super.getX()) < delta){
			currentX = super.getX();
		}
		
		if (Math.abs(currentY - super.getY()) < delta){
			currentY = super.getY();
		}
	}
	
	public int getState(){
		return this.state;
	}
	
}
