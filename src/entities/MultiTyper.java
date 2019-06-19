package entities;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import engineTester.Clock;

public class MultiTyper extends Entity{

	public int fontsize;
	public ArrayList<String> text, currentText;
	private TrueTypeFont font;
	private boolean renderStar;
	private boolean hide;
	private float timeSinceLastLetter = 0;
	private float delay = 1.0f;
	
	public int xIndex=0, yIndex=0;
	
	
	public MultiTyper(float x, float y, int fontSize, ArrayList<String> String) {
		super(x, y);
		this.fontsize = fontSize;
		this.text = String;
		
		Font awtFont = new Font("consolas", Font.PLAIN, fontsize); //name, style (PLAIN, BOLD, or ITALIC), size
		this.font = new TrueTypeFont(awtFont, false); //base Font, anti-aliasing true/false
		
		this.hide = false;
		this.renderStar = false;
		
		this.currentText = new ArrayList<String>();
	}

	public void render() {
		if (hide)
			return;
		if (renderStar){
			font.drawString(super.getX()-30, super.getY()+10, "*", Color.white);		// magic numbers for alignment
		}
		for (int i = 0; i < currentText.size(); i++){
			font.drawString(super.getX(), (float) (super.getY()+i*this.fontsize), currentText.get(i), Color.white);
		}
		
	}

	public void update() {
		if (currentText.equals(text)){
			return;
		} else {
			if (timeSinceLastLetter > delay){
				if (yIndex == text.size()){
					return;
				}
				currentText.clear();
				if (xIndex == text.get(yIndex).length()){
					xIndex = 0;
					yIndex += 1;
				}
				xIndex += 1;
				
				
				int x = 0;
				int y = 0;
				
				while (y < yIndex){
					currentText.add(text.get(y));
					y += 1;
				}
				String tmp = text.get(yIndex).substring(0, xIndex);
				currentText.add(tmp);
				System.out.println(tmp);
				
				timeSinceLastLetter = 0;
			}
			
			timeSinceLastLetter  += Clock.Delta();
		}
		
	}

}
