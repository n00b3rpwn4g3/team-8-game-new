package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import model.Coordinates;
import model.Enemy;
import model.MovingVector;

public class EnemyView {
    Coordinates coordinates;

    final MovingVector movingVector;
//
//    private final Color bodyColor;

    private final int ballDiameter;
    
    private Image enemyGraphic;
    
    private Enemy enemyData;

    public EnemyView(Enemy enemy, Image enemyGraphic) {
    	enemyData = enemy;
    	this.enemyGraphic = enemyGraphic;
        this.coordinates = enemy.getCoordinates();
        this.movingVector = enemy.getMovingVector();
        this.ballDiameter = enemy.getEnemyDiameter();
    }
    
//    public void setEnemyGraphic(Image img){
//    	enemyGraphic = img;
//    }

    public void draw(Graphics g) {
    	
//    	Graphics2D g2 = (Graphics2D) g;
//        AffineTransform at = AffineTransform.getTranslateInstance((int)(coordinates.getxPos()- ballDiameter/2),
//        		(int)(coordinates.getyPos() - ballDiameter/2));
//        at.rotate(Math.toRadians(enemyData.getMovingAngle()+90))
//        g2.drawImage(enemyGraphic,at, null);
    g.drawImage(enemyGraphic, (int)(coordinates.getxPos()- ballDiameter/2), (int)(coordinates.getyPos() - ballDiameter/2), null);
    }

}
