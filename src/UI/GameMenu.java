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
import entities.Typer;
import shapes.Shapes;
import turnController.MasterTurnController;

public class GameMenu extends Entity {

	ArrayList<Button> menu_options;
	public int selected;

	public float creditsY;

	public float delay, timeSinceLastPress, timer;

	// Audio
	int genericMenuSelectBuffer, MenuMusicBuffer, sndNoiseBuffer, creditsBuffer;
	public Source source, MenuMusicSource, sndNoiseSource, creditsSource;
	public mode mode;

	private Texture titleTexture, heartTexture, creditsTexture;

	private Typer typer;

	enum mode {
		MENU, PLAY, TRANSISTION, CLOSE, CREDITS
	}

	public MasterTurnController turnController;
	
	public GameMenu(float x, float y, MasterTurnController turnController) {
		super(x, y);
		this.selected = 0;
		this.menu_options = new ArrayList<Button>();
		this.menu_options.add(new Button(512, 300, "play bar selected"));
		this.menu_options.add(new Button(512, 500, "credits bar"));
		this.menu_options.add(new Button(512, 700, "exit bar"));

		this.timeSinceLastPress = 0;
		this.delay = 2f;

		this.mode = mode.MENU;

		this.genericMenuSelectBuffer = AudioMaster.loadSound("audio/generic menu selection.wav");
		this.source = new Source();

		this.MenuMusicBuffer = AudioMaster.loadSound("audio/Back Once Again.wav");
		this.MenuMusicSource = new Source();

		this.sndNoiseBuffer = AudioMaster.loadSound("audio/snd_noise.wav");
		this.sndNoiseSource = new Source();

		this.MenuMusicSource.play(MenuMusicBuffer);
		this.MenuMusicSource.setLooping(true);
		this.MenuMusicSource.setVolume(0.3f);

		this.titleTexture = Shapes.LoadTexture("res/spongespin title.png", "PNG");
		this.heartTexture = Shapes.LoadTexture("res/heart.png", "PNG");

		this.timer = 10f;

		this.creditsTexture = Shapes.LoadTexture("res/credits.png", "PNG");
		this.creditsY = 900;

		this.creditsBuffer = AudioMaster.loadSound("audio/Fin.wav");
		this.creditsSource = new Source();
		this.typer = new Typer(25, 900, 18, "Press Escape to return back to main menu");
		this.typer.setCurrentText("Press Escape to return back to main menu");
		this.typer.setRenderStar(false);

		this.turnController = turnController;
	}

	@Override
	public void render() {

		// black background
		GL11.glColor3f(0, 0, 0);
		Shapes.draw_quad(0, 0, 1280, 960);

		if (this.mode == mode.MENU) {
			for (Entity button : menu_options) {
				button.render();
			}
			GL11.glEnable(GL_TEXTURE_2D);
			GL11.glColor3f(0, 0, 1);
			Shapes.DrawQuadTex(heartTexture, 520, 332 + selected * 200, 32, 32);

			// Spongetale title page
			GL11.glColor3f(1, 1, 1);
			Shapes.DrawQuadTex(titleTexture, 290, 0, 700, 300);

		} else if (this.mode == mode.CREDITS) {
			GL11.glEnable(GL_TEXTURE_2D);
			GL11.glColor3f(1, 1, 1);
			Shapes.DrawQuadTex(creditsTexture, 0, creditsY, 1280, 3840);
			creditsY -= 1;
			typer.render();
		} else {
			GL11.glColor3f(0, 0, 0);
			Shapes.draw_quad(0, 0, 1280, 960);
		}
	}

	public void update() {
		if (this.mode == mode.CREDITS) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				this.mode = mode.MENU;
				creditsSource.stop();
				creditsY = 900;
			}
		} else if (this.mode != mode.TRANSISTION) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				changeSelect(-1, menu_options.size());
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				changeSelect(1, menu_options.size());
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				if (timeSinceLastPress > delay) {
					if (selected == 0) {
						this.mode = mode.TRANSISTION;
						MenuMusicSource.stop();
						sndNoiseSource.play(sndNoiseBuffer);
					} else if (selected == 1) {
						MenuMusicSource.stop();
						this.mode = mode.TRANSISTION;
						sndNoiseSource.play(sndNoiseBuffer);
					} else if (selected == 2) {
						MenuMusicSource.delete();
						this.mode = mode.CLOSE;
					}
				}
			}

			timeSinceLastPress += Clock.Delta();
		} else {
			if (timer > 0) {
				timer -= Clock.Delta();
			} else {
				if (selected == 0) {
					turnController.reset();
					this.mode = mode.PLAY;
					sndNoiseSource.play(sndNoiseBuffer);
				} else if (selected == 1) {
					this.mode = mode.CREDITS;
					creditsSource.play(creditsBuffer);
					creditsSource.setVolume(0.4f);
				}
				timer = 10f;
			}
		}
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
			source.play(genericMenuSelectBuffer);

			if (selected == 0) {
				menu_options.get(0).setTexture("play bar selected");
				menu_options.get(1).setTexture("credits bar");
				menu_options.get(2).setTexture("exit bar");
			} else if (selected == 1) {
				menu_options.get(0).setTexture("play bar");
				menu_options.get(1).setTexture("credits bar selected");
				menu_options.get(2).setTexture("exit bar");
			} else if (selected == 2) {
				menu_options.get(0).setTexture("play bar");
				menu_options.get(1).setTexture("credits bar");
				menu_options.get(2).setTexture("exit bar selected");
			}
		}

	}

	public Boolean playerRequestToPlay() {
		return this.mode == mode.PLAY;
	}

	public Boolean playerRequestToClose() {
		return this.mode == mode.CLOSE;
	}

	public mode getMode() {
		return mode;
	}

	public void setMode(mode mode) {
		this.mode = mode;
	}

	public void close() {
		this.mode = mode.CLOSE;
	}

	public void mainMenu() {
		this.mode = mode.MENU;
	}

	public void tempFreeze() {
		timeSinceLastPress = -8f;
	}

}
