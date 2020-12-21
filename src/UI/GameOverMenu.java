package UI;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

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

	public enum mode {
		MENU, RETRY, TRANSISTION, RETURNTOMAINMENU, EXITPROGRAM
	}

	public float delay, timeSinceLastPress, alpha;

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

		this.brokenHeartTexture = Shapes.LoadTexture("res/heart_broken.png", "PNG");

		this.alpha = 0f;
	}

	@Override
	public void render() {
		// Display GameOver Title
		if (this.mode == mode.MENU) {
			GL11.glEnable(GL_TEXTURE_2D);
			GL11.glColor4f(1, 1, 1, alpha);
			Shapes.DrawQuadTex(gameOverTitleTexture, 240, 100, 800, 155);

			for (Entity button : menu_options) {
				((Button) button).renderAlpha(alpha);
			}

			GL11.glEnable(GL_TEXTURE_2D);
			GL11.glColor4f(0, 0, 1, alpha);
			Shapes.DrawQuadTex(brokenHeartTexture, 525, 332 + selected * 200, 40, 32);
		}
	}

	@Override
	public void update() {
		if (this.mode == mode.MENU) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				changeSelect(-1, menu_options.size());

			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				changeSelect(1, menu_options.size());
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				if (timeSinceLastPress > delay) {
					timeSinceLastPress = 0;

					if (selected == 0) {
						this.mode = mode.RETRY;
					} else if (selected == 1) {
						this.mode = mode.RETURNTOMAINMENU;
						GL11.glDisable(GL_TEXTURE_2D);
						GL11.glColor3f(0, 0, 0);
						Shapes.draw_square(0, 0, 1280);
					} else if (selected == 2) {
						this.mode = mode.EXITPROGRAM;
					}
				}
			}

		}
		timeSinceLastPress += Clock.Delta();

		if (alpha < 1) {
			alpha += 0.005f;
		}

	}

	public void startThemeMusic() {
		gameOverSource.play(gameOverBuffer);
	}

	public void stopThemeMusic() {
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

			if (selected == 0) {
				menu_options.get(0).setTexture("retry selected");
				menu_options.get(1).setTexture("return to main menu");
				menu_options.get(2).setTexture("quit");
			} else if (selected == 1) {
				menu_options.get(0).setTexture("retry");
				menu_options.get(1).setTexture("return to main menu selected");
				menu_options.get(2).setTexture("quit");
			} else if (selected == 2) {
				menu_options.get(0).setTexture("retry");
				menu_options.get(1).setTexture("return to main menu");
				menu_options.get(2).setTexture("quit selected");
			}
		}

	}

	public mode getMode() {
		return mode;
	}

	public void setMode(mode mode) {
		this.mode = mode;
	}

	
	public void reset(){
		this.alpha = 0f;
		this.mode = mode.MENU;
	}
}
