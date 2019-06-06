package UI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import engineTester.Clock;
import entities.Entity;
import shapes.Shapes;

public class Miss extends Entity{

	public int width, height;
	
	public Texture texture;
	
	public boolean active;
	
	public float timer;
	
	public float ySpeed, yAcceleration;
	
	public Miss(float x, float y, int width, int height) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.texture = Shapes.LoadTexture("res/"+"miss"+".png", "PNG");
		this.active = false;
		this.timer = 0;
		this.ySpeed = -2f;
		this.yAcceleration = 0.1f;
	}

	@Override
	public void render() {
		if (!active)
			return;
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(texture, super.getX(), super.getY(), width, height);
	}

	@Override
	public void update() {
		//System.out.println(timer);
		if (timer > 10){
			activate();
			timer = 0;
		} else {
			timer += Clock.Delta();
		}
		
		if (active){
			super.setY(super.getY() + ySpeed);
			this.ySpeed += this.yAcceleration;
			if (ySpeed > 2f){
				ySpeed = 0;
				yAcceleration = 0;
			}
		}
		
		
	}
	
	public void activate(){
		this.active = true;
	}
	
	public void deactivate(){
		this.active = false;
	}

}
