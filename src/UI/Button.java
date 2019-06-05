package UI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import entities.Entity;
import shapes.Shapes;

public class Button extends Entity{
	
	public int height, width;
	public boolean selected;
	public Texture texture;
	
	public Button(float x, float y, String texture) {
		super(x, y);
		this.height = 96;
		this.width = 256;
		this.selected = false;
		this.texture = Shapes.LoadTexture("res/"+texture+".png", "PNG");
	}

	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(texture, super.getX(), super.getY(), width, height);
	}
	
	public void update(){
		
	}
	
	public void setTexture(String texture){
		this.texture = Shapes.LoadTexture("res/"+texture+".png", "PNG");
	}
}
