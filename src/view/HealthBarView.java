package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import model.HealthBar;

public class HealthBarView {
	
    int width = 180;
	int height = 20;

    int currentPoints;
    int maxPoints;
    
    private HealthBar hb;
    
    public HealthBarView(HealthBar hb){
    	this.hb=hb;
    	currentPoints = hb.getCurrentPoints();
    	maxPoints = hb.getMaxPoints();
    }
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(5, 5, width, height);
        g.setColor(Color.RED);
        g.fillRect(5, 5, (int)(((double)currentPoints/maxPoints) * width), height);
        g.setFont(new Font("TimesRoman", Font.BOLD, 15));
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(currentPoints), width + 15, height);
    }
    
    public HealthBar getHealthBar(){
    	return hb;
    }

}
