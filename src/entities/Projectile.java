package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import shapes.Shapes;

public class Projectile extends Entity {

	public float speed, direction;
	public float xSpeed, ySpeed;
	public Texture texture;
	public int size;
	
	public float yAcceleration; 					// This was added because of Upasna
	
	public Projectile(float x, float y, float speed, float angle, int size, float yAcceleration) {
		super(x, y);
		this.speed = speed;
		this.direction = angle;
		this.size = size;
		this.texture = Shapes.LoadTexture("res/bubble2.png", "PNG");
		
		this.xSpeed = (float) (speed * Math.cos(direction));
		this.ySpeed = (float) (speed * Math.sin(direction));
		
		this.yAcceleration = yAcceleration;
	}

	public void render() {

		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(texture, super.getX(), super.getY(), size, size);
	}

	public void update() {
		super.setX(super.getX() + this.xSpeed);
		super.setY(super.getY() + this.ySpeed);
		
		this.ySpeed += yAcceleration;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getDirection() {
		return direction;
	}

	// direction is in degrees, within the class, it is converted to radians
	public void setDirection(float direction) {
		this.direction = (float) (direction % (2 * Math.PI));
	}
	
}
