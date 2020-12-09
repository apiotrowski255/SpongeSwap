package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import shapes.Shapes;

public class HitBox extends Entity{

	public float x, y, width, height, angle;
	public Texture texture;
	
	public HitBox(float x, float y, float width, float height, float angle) {
		super(x,y);
		this.width = width;
		this.height = height;
		this.angle = angle;
	}

	@Override
	public void render() {
		GL11.glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.draw_quad(10, 10, 10, 10);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	
}
