package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;

import renderEngine.DisplayManager;
import shapes.Shapes;

public class ProjectileMask extends Entity {

	private PlaySpace ps;
	private float x1;
	private float y1;
	private boolean active;
	private int offset = 5;
	
	public ProjectileMask(float x, float y, PlaySpace ps) {
		super(x, y);
		this.ps = ps;
		this.x1 = ps.getX();
		this.y1 = ps.getY();
		this.active = true;
	}

	public void render() {
		if (!active)
			return;
		GL11.glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(0, 0, 0);
		Shapes.draw_quad(0, (int) y1 - offset, (int) x1, ps.getHeight());
		Shapes.draw_quad((int) (x1 + ps.getWidth()), (int) y1 - offset, DisplayManager.getWidth() - ((int) x1 + ps.getWidth()), ps.getHeight());
		GL11.glEnable(GL_TEXTURE_2D);
	}

	public void update() {
		if (!active)
			return;
		this.x1 = ps.getX();
		this.y1 = ps.getY();
	}

	
	public void Activate(){
		active = true;
	}
	
	public void Deactivate(){
		active = false;
	}
}
