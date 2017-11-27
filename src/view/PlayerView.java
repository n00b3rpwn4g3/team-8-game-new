package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import model.Coordinates;
import model.Player;

public class PlayerView {
	
	private Player p;
	private Image playerGraphic;
    Coordinates coordinates;
    private double movementAngle;

    public PlayerView(Player p, Image playerGraphic){
    	this.playerGraphic = playerGraphic;
    	coordinates = p.getCoordinates();
    	movementAngle = p.getDirectionAngle()+90;
    }
	
    public void draw(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = AffineTransform.getTranslateInstance((int)coordinates.getxPos() - 5,
        		(int)coordinates.getyPos()- 5 );
        at.rotate(Math.toRadians(movementAngle));
        g2.drawImage(playerGraphic,at, null);
//    	g.drawImage(playerGraphic, (int)coordinates.getxPos() - 5, (int)coordinates.getyPos()- 5, null);
    }

}
