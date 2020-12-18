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
import shapes.Shapes;

public class GameMenu extends Entity {

	ArrayList<Button> menu_options;
	public int selected;

	public float delay, timeSinceLastPress, timer;

	// Audio
	int genericMenuSelectBuffer, MenuMusicBuffer, sndNoiseBuffer;
	public Source source, MenuMusicSource, sndNoiseSource;
	private mode mode;

	private Texture texture, heartTexture;

	enum mode {
		MENU, PLAY, TRANSISTION, CLOSE
	}

	public GameMenu(float x, float y) {
		super(x, y);
		this.selected = 0;
		this.menu_options = new ArrayList<Button>();
		this.menu_options.add(new Button(512, 300, "play bar"));
		this.menu_options.add(new Button(512, 500, "exit bar"));

		this.timeSinceLastPress = 0;
		this.delay = 2f;

		this.mode = mode.MENU;

		this.genericMenuSelectBuffer = AudioMaster.loadSound("audio/generic menu selection.wav");
		this.source = new Source();

		this.MenuMusicBuffer = AudioMaster.loadSound("audio/Back Once Again.wav");
		this.MenuMusicSource = new Source();

		this.sndNoiseBuffer = AudioMaster.loadSound("audio/snd_noise.wav");
		this.sndNoiseSource = new Source();

		MenuMusicSource.play(MenuMusicBuffer);
		MenuMusicSource.setLooping(true);
		MenuMusicSource.setVolume(0.3f);

		this.texture = Shapes.LoadTexture("res/spongespin title.png", "PNG");
		this.heartTexture = Shapes.LoadTexture("res/heart.png", "PNG");

		this.timer = 10f;
	}

	@Override
	public void render() {
		if (this.mode == mode.MENU) {
			for (Entity button : menu_options) {
				button.render();
			}

			if (selected == 0) {
				GL11.glColor3f(0, 0, 1);
				Shapes.DrawQuadTex(heartTexture, 512, 300, 32, 32);
			} else if (selected == 1) {
				GL11.glColor3f(0, 0, 1);
				Shapes.DrawQuadTex(heartTexture, 512, 500, 32, 32);
			}

			GL11.glEnable(GL_TEXTURE_2D);
			GL11.glColor3f(1, 1, 1);
			Shapes.DrawQuadTex(texture, 100, 100, 500, 100);
		} else {
			GL11.glColor3f(0, 0, 0);
			Shapes.draw_quad(0, 0, 960, 1280);
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (this.mode != mode.TRANSISTION) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				changeSelect(-1, menu_options.size());
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				changeSelect(1, menu_options.size());
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				if (selected == 0) {
					this.mode = mode.TRANSISTION;
					MenuMusicSource.stop();
					sndNoiseSource.play(sndNoiseBuffer);
				} else if (selected == 1) {
					MenuMusicSource.delete();
					this.mode = mode.CLOSE;
				}

			}

			timeSinceLastPress += Clock.Delta();
		} else {
			if (timer > 0){
				timer -= Clock.Delta();
			} else {
				sndNoiseSource.play(sndNoiseBuffer);
				this.mode = mode.PLAY;
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
		}

	}

	public Boolean playerRequestToPlay() {
		return this.mode == mode.PLAY;
	}

	public Boolean playerRequestToClose() {
		return this.mode == mode.CLOSE;
	}
}
