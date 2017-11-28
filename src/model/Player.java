package model;

import java.awt.*;

public class Player {

    Coordinates coordinates;
    double directionAngle;
    private int playerDiameter;

    public Player(Coordinates coordinates, int imageDiameter) {
        this.coordinates = coordinates;
        playerDiameter = imageDiameter;
    }

    public double getDirectionAngle() {
		return directionAngle;
	}

	public void setDirectionAngle(double directionAngle) {
		this.directionAngle = directionAngle;
	}

	public Coordinates getCoordinates() {
        return coordinates;
    }

    public void moveByVector(MovingVector movingVector) {
        double xPos = coordinates.getxPos() + movingVector.getX();
        double yPos = coordinates.getyPos() + movingVector.getY();

        coordinates = new Coordinates(xPos, yPos);
    }
    
    public int getPlayerDiameter() {
    		return playerDiameter;
    }
}
