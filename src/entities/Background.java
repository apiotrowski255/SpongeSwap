package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;

import shapes.Shapes;

public class Background extends Entity{

	int size;
	
	public Background() {
		super(-1280,-1280);
		this.size = 2560;
	}
	
	public void render(){
		GL11.glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(0, 0, 0);
		Shapes.draw_square(super.getX(), super.getY(), size);
	}

	public void update() {

	}

}
