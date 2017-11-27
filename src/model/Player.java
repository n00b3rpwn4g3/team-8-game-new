
package model;
import java.awt.*;

public class Player {

    Coordinates coordinates;
    double directionAngle;

    public Player(Coordinates coordinates) {
        this.coordinates = coordinates;

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
}
