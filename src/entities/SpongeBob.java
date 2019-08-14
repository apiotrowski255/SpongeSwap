package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import engineTester.Clock;
import shapes.Shapes;

public class SpongeBob extends Entity {

	public Texture faceTexture;
	public Texture bodyTexture;
	
	public float faceX, faceY, faceYIdleDisplacementCounter;
	public int faceW, faceH;
	
	public float bodyX, bodyY;
	public int bodyW, bodyH;
	
	public float faceXOffset, bodyYOffset;
	
	public float xSpeed;
	
	public int counter;
	
	private final float MAXDISPLACEMENT = 5;
	private final float IDLEMOVEMENTSPEED = 1f;
	private boolean upMovement = true;
	private float idleTimer = 0;
	private final float DELAY = 5;
	
	public ArrayList<Texture> animationDown, animationUp;
	private int currentFrameCounter;
	private float animationTimer;
	private float frame_speed;
	private boolean animateDown, animateUp;
	
	public SpongeBob(float x, float y) {
		super(x, y);
		
		int size = 4;
		
		this.faceXOffset = 11 * size;
		this.bodyYOffset = 27 * size;
		this.faceX = super.getX() + faceXOffset;
		this.faceY = super.getY();
		
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
		
		//Idle movement
		this.faceYIdleDisplacementCounter = 0;
		
		this.currentFrameCounter = 0;
		this.animationTimer = 0;
		this.frame_speed = 0.50f;
		this.animateDown = false;
		this.animateUp = false;
		
		loadAnimationDownTextures();
		loadAnimationUpTextures();
	}

	public void render() {
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(faceTexture, faceX, faceY + faceYIdleDisplacementCounter, faceW, faceH);
		Shapes.DrawQuadTex(bodyTexture, bodyX, bodyY, bodyW, bodyH);
	}

	public void update() {
	
		dodgeMovement();
		
		this.faceX = super.getX() + faceXOffset;
		this.faceY = super.getY();
		
		this.bodyX = super.getX();
		this.bodyY = super.getY() + bodyYOffset;
		
		idleMovement();
		
		if (animateDown){
			animateDown();
		} else if (animateUp){
			animateUp();
		}
	}
	
	public void animateUp(){
		int size = 4;
		if (this.bodyYOffset == 7 * size){
			this.bodyW = 77 * size;
			this.bodyH = 69 * size;
			bodyTexture = animationDown.get(0);
		} else if (this.bodyYOffset == 10 * size){
			this.bodyW = 78 * size;
			this.bodyH = 65 * size;
			bodyTexture = animationDown.get(2);
		} else if (this.bodyYOffset == 27 * size){
			this.bodyW = 75 * size;
			this.bodyH = 47 * size;
			bodyTexture = animationDown.get(3);
		}
		if (animationTimer > this.frame_speed){
			
			if (currentFrameCounter == animationDown.size() - 1){
				// End the Animation
				animateUp = false;
				currentFrameCounter = 0;
				bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso.png", "PNG");
				this.bodyX = super.getX();
				this.bodyY = super.getY() + bodyYOffset;
				
				// Dimensions of Body_full.png multiplied by size factor
				this.bodyW = 72 * size;
				this.bodyH = 48 * size;
				this.bodyYOffset = 27 * size;
			} else if (currentFrameCounter < animationDown.size() - 1){
				
				currentFrameCounter += 1;
				if (currentFrameCounter == 1){
					this.bodyYOffset = 27 * size;
				} else if (currentFrameCounter == 2){
					this.bodyYOffset = 10 * size;
				} else {
					this.bodyYOffset = 7 * size;
				}
				
			}

			animationTimer = 0;
		}
		animationTimer += Clock.Delta();
	}
	
	public void animateDown(){
		int size = 4;
		if (this.bodyYOffset == 7 * size){
			this.bodyW = 77 * size;
			this.bodyH = 69 * size;
			bodyTexture = animationDown.get(0);
		} else if (this.bodyYOffset == 10 * size){
			this.bodyW = 78 * size;
			this.bodyH = 65 * size;
			bodyTexture = animationDown.get(2);
		} else if (this.bodyYOffset == 27 * size){
			this.bodyW = 75 * size;
			this.bodyH = 47 * size;
			bodyTexture = animationDown.get(3);
		}
		if (animationTimer > this.frame_speed){
			
			if (currentFrameCounter == animationDown.size() - 1){
				// End the Animation
				animateDown = false;
				currentFrameCounter = 0;
				bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso.png", "PNG");
				this.bodyX = super.getX();
				this.bodyY = super.getY() + bodyYOffset;
				
				// Dimensions of Body_full.png multiplied by size factor
				this.bodyW = 72 * size;
				this.bodyH = 48 * size;
				this.bodyYOffset = 27 * size;
			} else if (currentFrameCounter < animationDown.size() - 1){
				
				currentFrameCounter += 1;
				if (currentFrameCounter == 1){
					this.bodyYOffset = 7 * size;
				} else if (currentFrameCounter == 2){
					this.bodyYOffset = 10 * size;
				} else {
					this.bodyYOffset = 27 * size;
				}
				
			}

			animationTimer = 0;
		}
		animationTimer += Clock.Delta();
	}
	
	public void idleMovement(){
		if (upMovement){
			if (idleTimer > DELAY){
				this.faceYIdleDisplacementCounter += 1;
				idleTimer = 0;
			}
			if (faceYIdleDisplacementCounter >= MAXDISPLACEMENT){
				upMovement = false;
			}
		} else {
			if (idleTimer > DELAY){
				this.faceYIdleDisplacementCounter -= 1;
				idleTimer = 0;
			}
			if (faceYIdleDisplacementCounter < 0){
				upMovement = true;
			}
		}
		this.idleTimer += Clock.Delta();
	}
	
	public void dodgeMovement(){
		// handles spongebob moving away and back into the original position
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
	}
	
	public void dodge(){
		// activates/starts the dodge movement
		this.faceTexture = Shapes.LoadTexture("res/SpongeBob/face_wink_right.png", "PNG");
		this.xSpeed = -4;
		this.counter = 60;
	}
	
	public void loadAnimationDownTextures(){
		this.animationDown = new ArrayList<Texture>();
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down1.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down2.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down3.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down4.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down5.png", "PNG"));
	}
	
	public void loadAnimationUpTextures(){
		this.animationUp = new ArrayList<Texture>();
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down5.png", "PNG"));
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down4.png", "PNG"));
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down3.png", "PNG"));
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down2.png", "PNG"));
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down1.png", "PNG"));
	}
	
	public void playAnimationUp(){
		System.out.println("playing animation");
		this.animateUp = true;
		
		int size = 4;
		this.bodyW = 77 * size;
		this.bodyH = 69 * size;
		this.bodyYOffset = 27 * size;
		bodyTexture = animationDown.get(4);
	}
	
	public void playAnimationDown(){
		System.out.println("playing animation");
		this.animateDown = true;
		
		int size = 4;
		this.bodyW = 77 * size;
		this.bodyH = 69 * size;
		this.bodyYOffset = 7 * size;
		bodyTexture = animationDown.get(0);
	}
	
	public void setBodyTexture(String texture){
		this.bodyTexture = Shapes.LoadTexture("res/SpongeBob/" + texture + ".png", "PNG");
	}

}
