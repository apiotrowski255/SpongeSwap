package UI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import engineTester.Clock;
import entities.Entity;
import shapes.Shapes;

public class FightBar extends Entity {

	private float width, height, speed, direction;
	public Texture texture;
	private float timer;
	private int Delay;
	private boolean draw;

	public FightBar(float x, float y, float width, float height, float speed, float direction) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.direction = direction;
		this.timer = 0;
		this.Delay = 1;
		this.draw = true;
	}

	public void render() {
		timer += Clock.Delta();
		if (timer > Delay) {
			draw = !draw;
			timer = 0;
		}
		if (draw) {
			GL11.glDisable(GL_TEXTURE_2D);
			GL11.glColor3f(1, 1, 1);
			Shapes.draw_quad(super.getX(), super.getY(), (int) width, (int) height);
			GL11.glEnable(GL_TEXTURE_2D);
		} else {
			GL11.glDisable(GL_TEXTURE_2D);
			GL11.glColor3f(1, 1, 1);
			Shapes.drawBox(super.getX(), super.getY(), (int) width, (int) height, 5);
			GL11.glEnable(GL_TEXTURE_2D);
		}
	}

	public void update() {
		super.setX(super.getX() + (float) (speed * Math.cos(direction)));
		super.setY(super.getY() + (float) (speed * Math.sin(direction)));

	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
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

	public void setDirection(float direction) {
		this.direction = direction;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

}
