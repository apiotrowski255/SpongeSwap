package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import engineTester.Clock;
import shapes.Shapes;

public class JellyFish extends Entity{

	private final int PREPARE = 0;
	private final int SHOOT = 1;
	
	private Texture texture;
	private float size, direction, timer;
	private int state;
	private float timeSinceLastShot;
	private float currentX, currentY;
	/*
	 * direction is in degrees (not radians)
	 * 
	 */
	
	public JellyFish(float x, float y, float size, float direction, float timer) {
		super(x, y); 	// x and y are treated as the target coordinates of the jellyfish.
		this.size = size;
		this.direction = direction;
		this.texture = Shapes.LoadTexture("res/JellyFish_prepare.png", "PNG");
		this.timer = timer;
		this.state = PREPARE;
		this.timeSinceLastShot = 0;
		this.currentX = 0;
		this.currentY = 0;
	}

	@Override
	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1,1,1);
		Shapes.DrawQuadTexRot(texture, currentX, currentY, size, size, direction);
		
	}

	@Override
	public void update() {
		if (state == PREPARE){
			if (currentX < super.getX()) {
				currentX++;
			} else if (currentX > super.getX()) {
				currentX--;
			}
			
			if (currentY < super.getY()) {
				currentY++;
			} else if (currentY > super.getX()) {
				currentY--;
			}
		}
		
		if (timeSinceLastShot >= timer){
			shoot();
			state = SHOOT;
			timeSinceLastShot = 0;
		}

		timeSinceLastShot += Clock.Delta();
	}
	
	public void shoot(){
		System.out.println("shooting!");
		this.texture = Shapes.LoadTexture("res/JellyFish_shoot.png", "PNG");
	}
	
	

}
