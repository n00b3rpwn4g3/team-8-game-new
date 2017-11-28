package model;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy{

    Coordinates coordinates;

    final MovingVector movingVector;

    private int enemyDiameter;
    private boolean isInvasive;
    private double movingAngle;

    public Enemy(Coordinates coordinates, MovingVector movingVector, int imageDiameter, boolean b) {
        this.coordinates = coordinates;
        this.movingVector = movingVector;
        initMovingAngle();
        isInvasive=b;
        enemyDiameter = imageDiameter;
    }

    public void moveByVector() {
        double xPos = coordinates.getxPos() + movingVector.getX();
        double yPos = coordinates.getyPos() + movingVector.getY();
        
        coordinates = new Coordinates(xPos, yPos);
    }
    
    public void initMovingAngle(){
        movingAngle = (float) Math.toDegrees(Math.atan2(movingVector.getY(),
                movingVector.getX()));

        if(movingAngle < 0){
            movingAngle += 360;
        }
    }
    
    public double getMovingAngle(){
    		return movingAngle;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
    
    public MovingVector getMovingVector() {
		return movingVector;
	}
    
    public int getEnemyDiameter(){
    		return enemyDiameter;
	}
    
    public boolean checkEnemyType(){
    		return isInvasive;
    }
}