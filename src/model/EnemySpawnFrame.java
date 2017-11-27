package model;

public class EnemySpawnFrame {
    Coordinates startCoordinates;

    Coordinates endCoordinates;

    public EnemySpawnFrame(Coordinates startCoordinates, Coordinates endCoordinates) {
        this.startCoordinates = startCoordinates;
        this.endCoordinates = endCoordinates;
    }

    public double getXBeginning() {
        return startCoordinates.getxPos();
    }

    public double getXEnding() {
        return endCoordinates.getxPos();
    }

    public double getYEnding() {
        return endCoordinates.getyPos();
    }

    public double getYBeginning() {
        return startCoordinates.getyPos();
    }
}