package UI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.awt.Font;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import audio.AudioMaster;
import audio.Source;
import engineTester.Clock;
import entities.Entity;
import entities.Player;
import entities.Projectile;
import entities.SpongeBob;
import entities.Typer;
import renderEngine.DisplayManager;
import shapes.Shapes;

public class Menu extends Entity {

	public int selected;
	ArrayList<Button> menu_options;
	public Player player;
	public float delay;
	public float timeSinceLastPress;

	private final int MAIN = 0;
	private final int SUB = 1;
	public int mode;

	public boolean status;
	
	private TrueTypeFont font;

	private String name;
	public ArrayList<Typer> subMenu_options;
	public ArrayList<String> menuController;
	public ArrayList<Entity> menuComponent; 	// Component for the fight animation
	private float tmpTimer;
	
	private SpongeBob spongeBob;
	
	// Audio
	int Attackbuffer, genericMenuSelectBuffer, menuSelectBuffer, healSFXBuffer;
	public Source source;
	

	public Menu(Player player, SpongeBob spongeBob) {
		super(0, 0);
		this.selected = 0;
		this.menu_options = new ArrayList<Button>();
		this.menu_options.add(new Button(32, 825, "fight_selected"));
		this.menu_options.add(new Button(256 + 32 * 3, 825, "act"));
		this.menu_options.add(new Button(256 * 2 + 32 * 5, 825, "item"));
		this.menu_options.add(new Button(256 * 3 + 32 * 7, 825, "mercy"));
		this.subMenu_options = new ArrayList<Typer>();
		this.subMenu_options.add(new Typer(90, 525, "Are ya ready kids?"));
		this.menuController = new ArrayList<String>();
		this.player = player;
		this.timeSinceLastPress = 0;
		this.tmpTimer = 0; // For timing the components
		this.delay = 2f;
		// this.typer = new Typer(90, 525, "Are ya ready kids?");
		this.mode = MAIN;
		this.status = false; // false is player turn, when true it indicates
								// that the player has made their move

		

		Font awtFont = new Font(Font.SERIF, Font.PLAIN, 48); // name, style
																// (PLAIN, BOLD,
																// or ITALIC),
																// size
		this.font = new TrueTypeFont(awtFont, false); // base Font,
														// anti-aliasing
														// true/false

		this.name = "Plankton";
		this.menuComponent = new ArrayList<Entity>();
		this.spongeBob = spongeBob;
		
		// Audio
		this.Attackbuffer = AudioMaster.loadSound("audio/Swipe.wav");
		this.genericMenuSelectBuffer = AudioMaster.loadSound("audio/generic menu selection.wav");
		this.menuSelectBuffer = AudioMaster.loadSound("audio/Menu select.wav");
		this.healSFXBuffer = AudioMaster.loadSound("audio/Heal.wav");
		this.source = new Source();
	}

	public void render() {
		for (Entity button : menu_options) {
			button.render();
		}
		for (Typer typer : subMenu_options) {
			typer.render();
			typer.update();
		}
		font.drawString(50, 750, name, Color.white);
		font.drawString(300, 750, "LV 19", Color.white);
		font.drawString(500, 750, "HP", Color.white);
		GL11.glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 0, 0);
		Shapes.draw_quad(575, 760, 200, 50);
		GL11.glColor3f(1, 1, 0);
		Shapes.draw_quad(575, 760, 200 * player.getHealth() / 92, 50);
		GL11.glEnable(GL_TEXTURE_2D);
		font.drawString(800, 750, "KR", Color.white);
		font.drawString(900, 750, player.getHealth() + "/" + 92, Color.white);
	}

	public void update() {
		if (player.mode == player.PLAYMODE || player.mode == player.DISABLE) {
			return;
		}

		for (Entity p : menuComponent) {
			p.render();
			p.update();
		}

		if (mode == MAIN) {
			mainMenuControl();

		} else if (mode == SUB) {
			// System.out.println(this.menuController);
			// State Controls
			if (this.menuController.equals(new ArrayList<String>() {						// A small pause for the fight animation
				{
					add("SpongeBob");
					add("Enter");
				}})) {
				if (this.menuComponent.get(1).getX() > 1240){	// when the bar reaches the far right
					menuComponent.get(1).setSpeed(0);			// bar needs to stop
					this.menuController.add("Enter");
				}
				
			} else if (this.menuController.equals(new ArrayList<String>() {						// A small pause for the fight animation
				{
					add("SpongeBob");
					add("Enter");
					add("Enter");
				}})) {
				if (tmpTimer > 25) { // 2.5 second timer
					menuComponent.clear();
					tmpTimer = 0;
					setPlayerTurn();
				} else {
					tmpTimer += Clock.Delta();
				}
				//System.out.println(this.menuController);
				return;
			} else if (this.menuController.equals(new ArrayList<String>() {					// Checking Spongebob, should not be able to return to menu
				{
					add("Check");
					add("Enter");
					add("Enter");
			}})){
				if (tmpTimer > 50) { // 5 second timer
					menuComponent.clear();
					tmpTimer = 0;
					setPlayerTurn();
				} else {
					if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
						if (timeSinceLastPress > delay) {
							tmpTimer = 51;
							timeSinceLastPress = 0;
						}
					}
					tmpTimer += Clock.Delta();
					timeSinceLastPress += Clock.Delta();
				}
				//System.out.println(this.menuController);
				return;
			} else if (this.menuController.equals(new ArrayList<String>() {					// letting the typer finish its job
				{
					add("Check");
					add("Enter");
			}})){
				if (this.subMenu_options.get(0).getText().equals(this.subMenu_options.get(0).getCurrentText())){
					this.menuController.add("Enter");										// As if the player pressed enter
				}
			} 
			// Keyboard controls
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				changeSelect(-1, subMenu_options.size());
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				changeSelect(1, subMenu_options.size());
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				if (this.menuController.equals(new ArrayList<String>() {
					{
						add("SpongeBob");
						add("Enter");
					}})) {
					return;
				} 
				if (this.menuController.equals(new ArrayList<String>() {
					{
						add("Check");
						add("Enter");
					}})) {
					return;
				} 
				mode = MAIN;
				this.subMenu_options.clear();
				this.subMenu_options.add(new Typer(90, 525, "You feel..."));
				this.menuController.clear();
				selected = 0;
				source.play(genericMenuSelectBuffer);
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				//System.out.println(this.menuController);
				if (timeSinceLastPress > delay*2) {
					// checking the current menu state
					if (this.menuController.equals(new ArrayList<String>() {
						{
							add("Check");
						}
					})) {
						// Start typing the description for Spongebob
						this.subMenu_options.get(0).setText("SpongeBob, the only person to stand in your way");
					
					} else if (this.menuController.equals(new ArrayList<String>() {
						{
							add("Check");
							add("Enter");
						}
					})) {
						// if Description of Spongebob is finished, move on to Player turn
						if (this.subMenu_options.get(0).getCurrentText() == this.subMenu_options.get(0).getText()) {
							setPlayerTurn();
						// else finish the description of spongebob instantly
						} else {
							this.subMenu_options.get(0).setCurrentText(this.subMenu_options.get(0).getText());
						}

					} else if (this.menuController.equals(new ArrayList<String>() {		// Start the fight animation
						{
							add("SpongeBob");
						}
					})) {
						menuComponent.add(new FightBG(32, 510, 1216, 250, "battle_bar_bg"));
						menuComponent.add(new FightBar(16, 500, 20, 245, 4, 0));
						
						
						this.subMenu_options.get(0).hide();
						this.player.hide();
						
					
						
						System.out.println("Do the fight animation");
					} else if (this.menuController.equals(new ArrayList<String>() {
						{
							add("SpongeBob");
							add("Enter");
						}
					})) {
						
						System.out.println("Player has pressed enter : start the attack animation");
						menuComponent.get(1).setSpeed(0);
						float value = menuComponent.get(1).getX();
						ArrayList<Texture> frames = new ArrayList<Texture>();
						frames.add(Shapes.LoadTexture("res/" + "fight_animation_f" + 1 + ".png", "PNG"));
						frames.add(Shapes.LoadTexture("res/" + "fight_animation_f" + 2 + ".png", "PNG"));
						frames.add(Shapes.LoadTexture("res/" + "fight_animation_f" + 3 + ".png", "PNG"));
						frames.add(Shapes.LoadTexture("res/" + "fight_animation_f" + 4 + ".png", "PNG"));
						frames.add(Shapes.LoadTexture("res/" + "fight_animation_f" + 5 + ".png", "PNG"));
						frames.add(Shapes.LoadTexture("res/" + "fight_animation_f" + 6 + ".png", "PNG"));
						
						int size = 300;
						float yPos = 100;
						
						if (value < 420){
							size = 100;
							yPos = 250;
						} else if (value < 600){
							size = 200;
							yPos = 175;
						} else if (value < 700){
							size = 300;
							yPos = 100;
						} else if (value < 840){
							size = 200;
							yPos = 175;
						} else {
							size = 100;
							yPos = 250;
						} 
						
						float xPos = (DisplayManager.getWidth() - size)/2;
						menuComponent.add(new FightAnimation(xPos, yPos, size, size, 2, frames));
						
						menuComponent.add(new Miss(510, 80, 32*7, 9*7));								// Spawn the Miss UI
						
						this.spongeBob.dodge();
						this.source.play(this.Attackbuffer);

					} else if (this.menuController.equals(new ArrayList<String>() {
						{
							add("Item");
						}
					})) {
						System.out.println("use the Item");
						player.setHealth(player.getHealth()+42);
						source.play(healSFXBuffer);
						setPlayerTurn();
						return;
					} else {

						System.out.println("fuck, nothing matches the menu states");
						System.out.println(this.menuController);
						setPlayerTurn();
						return;
					}
					this.menuController.add("Enter");
					timeSinceLastPress = 0;
					source.play(menuSelectBuffer);
				}
			}
			if (selected == 0) {
				player.setY(543);
				player.setX(55);
			} else if (selected == 1) {
				player.setY(543);
				player.setX(565);
			} else if (selected == 2) {
				player.setY(592);
				player.setX(55);
			} else if (selected == 3) {
				player.setY(592);
				player.setX(565);
			}
		}
		timeSinceLastPress += Clock.Delta();
	}
	
	public void mainMenuControl(){
		// Update selected button
		updateSelectedButton();

		// Set the player position to be on the correct button
		setPlayerPositionOnButton();

		// Keyboard controls to change State
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			changeSelect(-1, menu_options.size());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			changeSelect(1, menu_options.size());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			if (selected == 0) {
				this.subMenu_options.get(0).setCurrentText("SpongeBob");
				this.menuController.add("SpongeBob");
			} else if (selected == 1) {
				this.subMenu_options.get(0).setCurrentText("Check");
				this.menuController.add("Check");
			} else if (selected == 2) {
				this.subMenu_options.get(0).setCurrentText("Crabby Patty");
				this.subMenu_options.add(new Typer(600, 525, ""));
				this.subMenu_options.get(1).setCurrentText("Crabby Patty");
				this.subMenu_options.add(new Typer(90, 575, ""));
				this.subMenu_options.get(2).setCurrentText("sandwich");
				this.subMenu_options.add(new Typer(600, 575, ""));
				this.subMenu_options.get(3).setCurrentText("cola");
				
				ArrayList<String> playerItems = player.getStats().getItems();
				for (int i = 0 ; i < playerItems.size(); i++){
					this.subMenu_options.get(i).setCurrentText(playerItems.get(i));
				}
				
				this.menuController.add("Item");
			} else if (selected == 3) {
				this.subMenu_options.get(0).setCurrentText("Spare");
				this.menuController.add("Spare");
			}
			mode = SUB;
			selected = 0;
			timeSinceLastPress = 0;
			resetButtonSelected();
			source.play(menuSelectBuffer);
		}
	}

	public void setPlayerTurn() {
		status = true;
		this.subMenu_options.clear();
		this.subMenu_options.add(new Typer(90, 525, ""));
		timeSinceLastPress = 0;
		resetButtonSelected();
		this.menuController.clear();
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

	public boolean playerTurnCompleted() {
		return status;
	}

	public void updateSelectedButton() {
		if (selected == 0) {
			menu_options.get(0).setTexture("fight_selected");
			menu_options.get(1).setTexture("act");
			menu_options.get(2).setTexture("item");
			menu_options.get(3).setTexture("mercy");
		} else if (selected == 1) {
			menu_options.get(0).setTexture("fight");
			menu_options.get(1).setTexture("act_selected");
			menu_options.get(2).setTexture("item");
			menu_options.get(3).setTexture("mercy");
		} else if (selected == 2) {
			menu_options.get(0).setTexture("fight");
			menu_options.get(1).setTexture("act");
			menu_options.get(2).setTexture("item_selected");
			menu_options.get(3).setTexture("mercy");
		} else if (selected == 3) {
			menu_options.get(0).setTexture("fight");
			menu_options.get(1).setTexture("act");
			menu_options.get(2).setTexture("item");
			menu_options.get(3).setTexture("mercy_selected");
		}

	}

	public void resetButtonSelected() {
		menu_options.get(0).setTexture("fight");
		menu_options.get(1).setTexture("act");
		menu_options.get(2).setTexture("item");
		menu_options.get(3).setTexture("mercy");
	}

	public void setPlayerPositionOnButton() {
		player.setY(855);
		if (selected == 0) {
			player.setX(54);
		} else if (selected == 1) {
			player.setX(378);
		} else if (selected == 2) {
			player.setX(695);
		} else if (selected == 3) {
			player.setX(1012);
		}
		// Previously it was: player.setX(selected * 325 + 53);
	}

}
