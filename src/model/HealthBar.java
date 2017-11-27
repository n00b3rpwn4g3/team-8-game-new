package model;

import javax.swing.*;
import java.awt.*;

public class HealthBar {
	int currentPoints;
	int maxPoints;

	public HealthBar(int healthPoints) {
		currentPoints = maxPoints = healthPoints;
	}

	public void setCurrentPoints(int points) {
		currentPoints = points;
	}

	public int getCurrentPoints() {
		if (currentPoints < 0)
			return 0;
		else
			return currentPoints;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}

}
