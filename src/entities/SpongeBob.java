package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import shapes.Shapes;

public class SpongeBob extends Entity {

	public Texture faceTexture;
	public Texture bodyTexture;
	
	public float faceX, faceY;
	public int faceW, faceH;
	
	public float bodyX, bodyY;
	public int bodyW, bodyH;
	
	public float faceXOffset, bodyYOffset;
	
	public float xSpeed;
	
	public int counter;
	
	public SpongeBob(float x, float y) {
		super(x, y);
		
		int size = 4;
		
		this.faceXOffset = 11 * size;
		this.bodyYOffset = 27 * size;
		this.faceX = super.getX() + faceXOffset;
		this.faceY = super.getY();
		
		//this.faceW = 49 * size;
		//this.faceH = 44 * size;
		
		this.faceW = 49*size;
		this.faceH = 44*size;
		
		this.bodyX = super.getX();
		this.bodyY = super.getY() + bodyYOffset;
		
		this.bodyW = 72 * size;
		this.bodyH = 48 * size;
	
		this.faceTexture = Shapes.LoadTexture("res/SpongeBob/face_netural.png", "PNG");
		this.bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso.png", "PNG");
		
		this.xSpeed = 4;
		this.counter = 0;
	}

	@Override
	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		//GL11.glEnable(GL_TEXTURE_2D);
		//GL11.glDisable(GL_TEXTURE_2D);
		//GL11.glColor3f(1, 0, 0);
		//Shapes.draw_quad(300, 100, faceW, faceH);
		
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(faceTexture, faceX, faceY, faceW, faceH);
		
		//GL11.glDisable(GL_TEXTURE_2D);
		//GL11.glColor3f(1, 0, 0);
		//Shapes.draw_quad(300+faceW, 100, faceW, faceH);
		
		//GL11.glColor3f(1, 1, 1);
		//Shapes.draw_quad(0, 18*5, 100*5, 48*5);
		//GL11.glEnable(GL_TEXTURE_2D);
		Shapes.DrawQuadTex(bodyTexture, bodyX, bodyY, bodyW, bodyH);
		
		//GL11.glEnable(GL_TEXTURE_2D);
	}

	@Override
	public void update() {
		if (counter > 0){
			
			super.setX(super.getX() + xSpeed);
			counter -= 1;
		}
		
		if (counter == 0 && xSpeed < 0){
			xSpeed = 0;
			counter = 120;
		} else if (counter == 0 && xSpeed == 0){
			this.faceTexture = Shapes.LoadTexture("res/SpongeBob/face_netural.png", "PNG");
			xSpeed = 4;
			counter = 60;
		}
		
		
		this.faceX = super.getX() + faceXOffset;
		this.faceY = super.getY();
		
		this.bodyX = super.getX();
		this.bodyY = super.getY() + bodyYOffset;
		
	}
	
	public void dodge(){
		this.faceTexture = Shapes.LoadTexture("res/SpongeBob/face_wink_right.png", "PNG");
		this.xSpeed = -4;
		this.counter = 60;
	}

}
