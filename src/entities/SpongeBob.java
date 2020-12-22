package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import audio.AudioMaster;
import audio.Source;
import engineTester.Clock;
import shapes.Shapes;

public class SpongeBob extends Entity {

	public Texture faceTexture;
	public Texture bodyTexture;
	public Texture bodyHitTexture, legsFallTexture;
	
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
	
	public ArrayList<Texture> animationDown, animationUp, animationLeft, animationRight;
	private int currentFrameCounter;
	private float animationTimer;
	private float frame_speed;
	private boolean animateDown, animateUp, animateLeft, animateRight, dead;
	
	private float targetX, targetY;
	
	// Hit sound effect
	private int hitSFXbuffer;
	private Source hitSFXsource;
	
	public SpongeBob(float x, float y) {
		super(x, y);
		this.targetX = super.getX();
		this.targetY = super.getY();
		int size = 4;
		
		this.faceXOffset = 11 * size;
		this.bodyYOffset = 6 * size;
		this.faceX = super.getX() + faceXOffset;
		this.faceY = super.getY();
		
		this.faceW = 49*size;
		this.faceH = 44*size;
		
		this.bodyX = super.getX();
		this.bodyY = super.getY() + bodyYOffset;
		
		this.bodyW = 109 * size;
		this.bodyH = 69 * size;
	
		this.faceTexture = Shapes.LoadTexture("res/SpongeBob/face_netural.png", "PNG");
		this.bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso_remake.png", "PNG");
		
		this.bodyHitTexture = Shapes.LoadTexture("res/SpongeBob/body_hit.png", "PNG");
		
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
		loadAnimationLeftTextures();
		loadAnimationRightTextures();
		
		this.hitSFXbuffer = AudioMaster.loadSound("audio/Hit.wav");
		this.hitSFXsource = new Source();
		this.dead = false;
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
		} else if (animateLeft){
			animateLeft();
		} else if (animateRight){
			animateRight();
		}	
	}
	
	// This animateRight function might be broken
	public void animateRight(){
		if (animationTimer > this.frame_speed){
			
			if (currentFrameCounter == animationRight.size()){
				// End the Animation
				animateLeft = false;
				currentFrameCounter = 0;
				bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso_remake.png", "PNG");
				System.out.println("Right Animation Finished");
				return;
			} else if (currentFrameCounter < animationRight.size()){
				bodyTexture = animationRight.get(currentFrameCounter);
				currentFrameCounter += 1;
			}

			animationTimer = 0;
		}
		animationTimer += Clock.Delta();
	}
	
	public void animateLeft(){
		if (animationTimer > this.frame_speed){
			
			if (currentFrameCounter == animationLeft.size()){
				// End the Animation
				animateLeft = false;
				currentFrameCounter = 0;
				bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso_remake.png", "PNG");
				System.out.println("Left Animation Finished");
				return;
			} else if (currentFrameCounter < animationLeft.size()){
				bodyTexture = animationLeft.get(currentFrameCounter);
				currentFrameCounter += 1;
			}

			animationTimer = 0;
		}
		animationTimer += Clock.Delta();
	}
	
	public void animateUp(){
		if (animationTimer > this.frame_speed){
			
			if (currentFrameCounter == animationUp.size()){
				// End the Animation
				animateUp = false;
				currentFrameCounter = 0;
				bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso_remake.png", "PNG");
				System.out.println("UP Animation Finished");
				return;
			} else if (currentFrameCounter < animationUp.size()){
				bodyTexture = animationUp.get(currentFrameCounter);
				currentFrameCounter += 1;
			}

			animationTimer = 0;
		}
		animationTimer += Clock.Delta();
	}
	
	public void animateDown(){
		if (animationTimer > this.frame_speed){
			
			if (currentFrameCounter == animationDown.size()){
				// End the Animation
				animateDown = false;
				currentFrameCounter = 0;
				bodyTexture = Shapes.LoadTexture("res/SpongeBob/torso_remake.png", "PNG");
				System.out.println("Down Animation Finished");
				return;
			} else if (currentFrameCounter < animationDown.size()){
				bodyTexture = animationDown.get(currentFrameCounter);
				currentFrameCounter += 1;
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
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down1_remake.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down1_remake.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down3_remake.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down4_remake.png", "PNG"));
		animationDown.add(Shapes.LoadTexture("res/SpongeBob/down4_remake.png", "PNG"));
	}
	
	public void loadAnimationUpTextures(){
		this.animationUp = new ArrayList<Texture>();
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down4_remake.png", "PNG"));
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down4_remake.png", "PNG"));
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down3_remake.png", "PNG"));
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down1_remake.png", "PNG"));
		animationUp.add(Shapes.LoadTexture("res/SpongeBob/down1_remake.png", "PNG"));
	}
	
	public void loadAnimationLeftTextures(){
		this.animationLeft = new ArrayList<Texture>();
		animationLeft.add(Shapes.LoadTexture("res/SpongeBob/side_1_remake.png", "PNG"));
		animationLeft.add(Shapes.LoadTexture("res/SpongeBob/side_1_remake.png", "PNG"));
		animationLeft.add(Shapes.LoadTexture("res/SpongeBob/side_3_remake.png", "PNG"));
		animationLeft.add(Shapes.LoadTexture("res/SpongeBob/side_4_remake.png", "PNG"));
		animationLeft.add(Shapes.LoadTexture("res/SpongeBob/side_4_remake.png", "PNG"));
	}
	
	public void loadAnimationRightTextures(){
		this.animationRight = new ArrayList<Texture>();
		animationRight.add(Shapes.LoadTexture("res/SpongeBob/side_4_remake.png", "PNG"));
		animationRight.add(Shapes.LoadTexture("res/SpongeBob/side_4_remake.png", "PNG"));
		animationRight.add(Shapes.LoadTexture("res/SpongeBob/side_3_remake.png", "PNG"));
		animationRight.add(Shapes.LoadTexture("res/SpongeBob/side_1_remake.png", "PNG"));
		animationRight.add(Shapes.LoadTexture("res/SpongeBob/side_1_remake.png", "PNG"));
	}
	
	public void playAnimationRight(){
		System.out.println("playing Right animation");
		this.animateRight = true;
		
		currentFrameCounter = 0;
		bodyTexture = animationRight.get(currentFrameCounter);
		currentFrameCounter += 1;
	}
	
	public void playAnimationLeft(){
		System.out.println("playing Left animation");
		this.animateLeft = true;
		
		currentFrameCounter = 0;
		bodyTexture = animationLeft.get(currentFrameCounter);
		currentFrameCounter += 1;
	}
	
	public void playAnimationUp(){
		System.out.println("playing Up animation");
		this.animateUp = true;
		
		currentFrameCounter = 0;
		bodyTexture = animationUp.get(currentFrameCounter);
		currentFrameCounter += 1;
	}
	
	public void playAnimationDown(){
		System.out.println("playing Down animation");
		this.animateDown = true;
		
		currentFrameCounter = 0;
		bodyTexture = animationDown.get(currentFrameCounter);
		currentFrameCounter += 1;
	}
	
	public void setBodyTexture(String texture){
		this.bodyTexture = Shapes.LoadTexture("res/SpongeBob/" + texture + ".png", "PNG");
	}

	public void die() {
		System.out.println("SpongeBob should be dead now, play animation and move him off screen");
		this.bodyTexture = Shapes.LoadTexture("res/SpongeBob/body_hit1.png", "PNG");
		this.hitSFXsource.play(hitSFXbuffer);
		this.dead = true;
	}

	public boolean isDead(){
		return this.dead;
	}
}
