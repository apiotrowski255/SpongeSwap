package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import shapes.Shapes;

public class PlayerBrokenBit extends Entity {

	public Texture texture;
	public Vector3f color;
	public float xSpeed, ySpeed, yacceleration, currentRotation, rotationSpeed;
	public int size;
	
	
	public PlayerBrokenBit(float x, float y, String texture, float xSpeed, float ySpeed, float rotationSpeed, Vector3f color) {
		super(x, y);
		this.texture = Shapes.LoadTexture("res/" + texture + ".png", "PNG");
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.rotationSpeed = rotationSpeed;
		this.color = color;
		this.size = 8;
		this.yacceleration = 0.02f;
	}

	@Override
	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(color.x, color.y, color.z);

		Shapes.DrawQuadTexRot(texture, super.getX(), super.getY(), size, size, currentRotation);
	}

	@Override
	public void update() {
		this.currentRotation += rotationSpeed;
		super.setX(super.getX() + xSpeed);
		super.setY(super.getY() + ySpeed);
		
		this.ySpeed += yacceleration;
	}

}
