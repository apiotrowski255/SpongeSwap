package UI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import audio.AudioMaster;
import audio.Source;
import engineTester.Clock;
import entities.Entity;
import entities.MultiTyper;
import shapes.Shapes;

public class GameOverMenu extends Entity {

	public int selected;
	
	private Texture brokenHeartTexture, gameOverTitleTexture;
	
	ArrayList<Button> menu_options;

	private int gameOverBuffer, genericMenuSelectBuffer;
	private Source gameOverSource, genericMenuSource;
	
	private MultiTyper typer;
	
	private mode mode;
	
	enum mode {
		MENU, RETRY, TRANSISTION, RETURNTOMAINMENU, EXITPROGRAM
	}
	
	public float delay, timeSinceLastPress;
	
	public GameOverMenu(float x, float y) {
		super(x, y);
		this.selected = 0;
		this.menu_options = new ArrayList<Button>();
		this.menu_options.add(new Button(512, 300, "retry selected"));
		this.menu_options.add(new Button(512, 500, "return to main menu"));
		this.menu_options.add(new Button(512, 700, "quit"));
		
		this.mode = mode.MENU;
		
		this.gameOverBuffer = AudioMaster.loadSound("audio/Game Over Theme.wav");
		this.gameOverSource = new Source();
		this.gameOverSource.setLooping(true);
		
		this.typer = new MultiTyper(800, 150, 24, new ArrayList<String>());
		this.gameOverTitleTexture = Shapes.LoadTexture("res/gameOverTitle.png", "PNG");
		
		this.timeSinceLastPress = 0;
		this.delay = 2f;
		
		this.genericMenuSelectBuffer = AudioMaster.loadSound("audio/generic menu selection.wav");
		this.genericMenuSource = new Source();
	}

	@Override
	public void render() {
		// Display GameOver Title
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(gameOverTitleTexture, 240, 100, 800, 155);
		
		for (Entity button : menu_options) {
			button.render();
		}
	}

	@Override
	public void update() {
		if (this.mode == mode.MENU){
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				changeSelect(-1, menu_options.size());
				
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				changeSelect(1, menu_options.size());
			}
		}
		timeSinceLastPress += Clock.Delta();
	}

	public void startThemeMusic(){
		gameOverSource.play(gameOverBuffer);
	}
	
	public void stopThemeMusic(){
		gameOverSource.stop();
	}
	
	public void changeSelect(int direction, int size) {
		if (timeSinceLastPress > delay) {
			selected += direction;
			if (selected > size - 1) {
				selected -= size;
			}
			if (selected < 0) {
				selected += size;
			}
			timeSinceLastPress = 0;
			genericMenuSource.play(genericMenuSelectBuffer);

			if (selected == 0){
				menu_options.get(0).setTexture("retry selected");
				menu_options.get(1).setTexture("return to main menu");
				menu_options.get(2).setTexture("quit");
			} else if (selected == 1){
				menu_options.get(0).setTexture("retry");
				menu_options.get(1).setTexture("return to main menu selected");
				menu_options.get(2).setTexture("quit");
			} else if (selected == 2){
				menu_options.get(0).setTexture("retry");
				menu_options.get(1).setTexture("return to main menu");
				menu_options.get(2).setTexture("quit selected");
			}
		}

	}
	
}
