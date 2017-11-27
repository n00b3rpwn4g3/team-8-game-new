package model;

public class GameTimer {

	private int timeRemaining;
	private int maxTime;

	public GameTimer(int totalTime) {
		timeRemaining = maxTime = totalTime;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public int getTimeRemaining() {
		if (timeRemaining < 0)
			return 0;
		else
			return timeRemaining;
	}

	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

}
