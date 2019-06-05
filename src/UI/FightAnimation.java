package UI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import engineTester.Clock;
import entities.Entity;
import shapes.Shapes;

public class FightAnimation extends Entity{

	public ArrayList<Texture> frames;
	public int currentFrameCounter;
	public float currentTimer;
	
	public int width, height;
	public float frame_speed;
	
	public FightAnimation(float x, float y, int width, int height, float frame_speed, ArrayList<Texture> frames) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.frame_speed = frame_speed;
		this.frames = frames;
		this.currentFrameCounter = 0;
		this.currentTimer = 0;
	}

	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(frames.get(currentFrameCounter), super.getX(), super.getY(), width, height);
		
	}

	public void update() {
		if (currentTimer > this.frame_speed){
			if (currentFrameCounter < frames.size() - 1){
				currentFrameCounter += 1;
			}
			currentTimer = 0;
		}
		super.setY((float) (super.getY() + 0.1));				// Moves the image downwards as it is animating
		currentTimer += Clock.Delta();
	}

}
