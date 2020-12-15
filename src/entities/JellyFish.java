package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import engineTester.Clock;
import shapes.Shapes;

public class JellyFish extends Entity{

	private final int TRAVEL = 0;
	private final int PREPARE = 1;
	private final int SHOOT = 2;
	
	private Texture texture;
	private float size, direction, timer;
	private int state;
	private float timeSinceLastShot;
	private float currentX, currentY;
	private float deltaX, deltaY;
	private float travelTime;
	private float delta = 1;
	private ArrayList<Projectile> projectiles;
	/*
	 * direction is in degrees (not radians)
	 * 
	 */
	
	public JellyFish(float x, float y, float size, float direction, float travelTime, float timer) {
		super(x, y); 	// x and y are treated as the target coordinates of the jellyfish.
		this.size = size;
		this.direction = direction;
		this.texture = Shapes.LoadTexture("res/JellyFish_prepare.png", "PNG");
		this.timer = timer;
		this.state = TRAVEL;
		this.timeSinceLastShot = 0;
		this.currentX = 0;
		this.currentY = 0;
		this.deltaX = Math.abs(this.x - this.currentX)/travelTime;
		this.deltaY = Math.abs(this.y - this.currentY)/travelTime;
		this.travelTime = travelTime;
		this.projectiles = new ArrayList<Projectile>();
	}

	@Override
	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1,1,1);
		Shapes.DrawQuadTexRot(texture, currentX, currentY, size, size, direction);
		
	}

	@Override
	public void update() {
		if (state == TRAVEL){
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
			
			if (Math.abs(currentX - super.getX()) < delta){
				currentX = super.getX();
			}
			
			if (Math.abs(currentY - super.getY()) < delta){
				currentY = super.getY();
			}
			
			if (currentX == super.getX() && currentY == super.getY()){
				this.state = PREPARE;
			}
		} else if (state == PREPARE) {
			if (timeSinceLastShot >= timer){
				shoot();
				state = SHOOT;
				timeSinceLastShot = 0;
			} else {
				timeSinceLastShot += Clock.Delta();
			}
		} else if (state == SHOOT) {
			currentX -=this.deltaX;
		}
		
		
		for (Projectile p : projectiles){
			p.update();
			p.render();
		}
	}
	
	public void shoot(){
		System.out.println("shooting!");
		this.texture = Shapes.LoadTexture("res/JellyFish_shoot.png", "PNG");
		shootProjectile(super.getX() + 32, super.getY() + 32, 1, (float) ((this.direction) * Math.PI / 180), 64);
		shootProjectile(super.getX() + 32, super.getY() + 32, 2, (float) ((this.direction) * Math.PI / 180), 64);
		shootProjectile(super.getX() + 32, super.getY() + 32, 3, (float) ((this.direction) * Math.PI / 180), 64);
		shootProjectile(super.getX() + 32, super.getY() + 32, 4, (float) ((this.direction)* Math.PI / 180), 64);
		shootProjectile(super.getX() + 32, super.getY() + 32, 5, (float) ((this.direction)* Math.PI / 180), 64);
		shootProjectile(super.getX() + 32, super.getY() + 32, 6, (float) ((this.direction)* Math.PI / 180), 64);
	}
	
	public void shootProjectile(float x, float y, float projectileSpeed, float angle, int size){
		Projectile p = new Projectile(x, y, projectileSpeed, angle, size, (float) 0.0);
		projectiles.add(p);
	}

	public ArrayList<Projectile> getProjectiles(){
		return this.projectiles;
	}
}
