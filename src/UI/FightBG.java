package UI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import entities.Entity;
import shapes.Shapes;

public class FightBG extends Entity {

	private float width, height, currentHeight;
	private float heightChangeSpeed;
	public Texture texture;
	
	public FightBG(float x, float y, float width, float height, String texture){
		super(x, y);
		this.width = width;
		this.height = height;
		this.texture = Shapes.LoadTexture("res/" + texture + ".png", "PNG");
	}

	@Override
	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);

		Shapes.DrawQuadTexRot(texture, super.getX(), super.getY(), width, this.height, 0);
	}

	@Override
	public void update() {

		
	}
	
}
