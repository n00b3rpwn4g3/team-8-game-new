package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import model.GameTimer;
import model.HealthBar;

public class GameTimerView {
	
	private GameTimer gt;
	final private int width = 100;
	final private int height = 20;
	int maxTime;
	int timeRemaining;
	final private int drawPosX = 600;
	final private int drawPosY = 500;
	
    public GameTimerView(GameTimer gt){
    	this.gt=gt;
    	maxTime = gt.getMaxTime();
    	timeRemaining = gt.getTimeRemaining();
    }
    public void drawBar(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(drawPosX, drawPosY, width, height);
        g.setColor(Color.GREEN);
        g.fillRect(drawPosX, drawPosY, (int)((double)timeRemaining/maxTime * width), height);
    	g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.BOLD, 15));
        g.drawString(Integer.toString(timeRemaining), drawPosX+width+15, drawPosY+height/2+5);
    }
    
    public GameTimer getGameTimer(){
    	return gt;
    }

}
