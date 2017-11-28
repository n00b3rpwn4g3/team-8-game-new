package model;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import view.LevelScreen;
import view.ScreenPanel.Screens;
import view.GameBoard;

public class Game {

	public enum DifficultyLVL {EASY, MEDIUM, HARD, TUTORIAL};
	DifficultyLVL difficulty;
	Screens currentScreenType;
	int numEnemies;
	int gameObjSpeed;
	Player player;
    Enemy[] listEnemies;
    int windowWidth = 800;
    int windowHeight = 600;
    int gameObjectSize = 30;
    int playerCollisionRad;
    int enemyCollisionRad;
    int collisionRad;
    private HealthBar hb;
    JFrame currentGameView;
    LevelScreen currentLevelScreen;
    boolean isStarted = false;
    private GameTimer gt;
    private int timeRemaining;
    final EnemySpawnFrame enemySpawnFrame = new EnemySpawnFrame(new Coordinates(-50, -50), new Coordinates(windowWidth + 50, windowHeight + 50));
	private int numInvasiveEnemy;
	private int numPollutionEnemy;
	private boolean notQ=true;
	

	public Game(JFrame boardView, LevelScreen panel, DifficultyLVL d){
		super();
		currentLevelScreen = panel;
		currentScreenType = panel.getScreenType();
		this.currentGameView = boardView;
		difficulty = d;
		playerCollisionRad = panel.getPlayerDiameter()/2;
		enemyCollisionRad = panel.getEnemyDiameter()/2;
		initGameVariables(d);
		numInvasiveEnemy = (int) (.5*numEnemies);
		numPollutionEnemy = numEnemies-numInvasiveEnemy;
		listEnemies = new Enemy[numEnemies];
        player = new Player(new Coordinates(windowWidth/2, windowHeight/2),2*playerCollisionRad);
        initListEnemies();
		
	}
	

	public void initGameVariables(DifficultyLVL d){
		double difficultyWeight;
	
		switch(d){
		case EASY:{
			difficultyWeight = 1.0;
			break;
		}
		case MEDIUM:{
			difficultyWeight = 1.2;
			break;
		}
		case HARD:{
			difficultyWeight = 1.5;
			break;
		}
		default:
			difficultyWeight = 1.0;
		}
	
		switch(currentScreenType){
		case L1:{
			numEnemies = (int) (difficultyWeight*20);
			gameObjSpeed = (int) (difficultyWeight*5);
			gt = new GameTimer(30);
			hb = new HealthBar(200);
			break;
		}
		case L2:{
			numEnemies = (int) (difficultyWeight*30);
			gameObjSpeed = (int) (difficultyWeight*6);
			gt = new GameTimer(40);
			hb = new HealthBar(200);
			break;
		}
		case L3:{
			numEnemies = (int) (difficultyWeight*40);
			gameObjSpeed = (int) (difficultyWeight*7);
			gt = new GameTimer(50);
			hb = new HealthBar(200);
			break;
		}
		case L4:{
			numEnemies = (int) (difficultyWeight*20);
			gameObjSpeed = (int) (difficultyWeight*8);
			gt = new GameTimer(60);
			hb = new HealthBar(200);
			break;
		}
		case TUTORIAL:{
			numEnemies = 20;
			gameObjSpeed = 5;
			gt = new GameTimer(15);
			hb = new HealthBar(1000);
			break;
		}
		
		}
		
	}
	
	public boolean isNotQ() {
		return notQ;
	}


	public void setNotQ(boolean notQ) {
		this.notQ = notQ;
	}

	public void setTimeRemaining(int time){
		timeRemaining = time;
	}
	public int getNumEnemies() {
		return numEnemies;
	}
	
	public GameTimer getGameTimer(){
		return gt;
	}
	
	
	public int getNumInvasive(){
		return numInvasiveEnemy;
	}
	
	public int getNumPollution(){
		return numPollutionEnemy;
	}
	
	public void hasWon(){
		currentLevelScreen.pause();
		if(!currentScreenType.equals(Screens.L4))
		{
		int choice = JOptionPane.showConfirmDialog(currentLevelScreen, "You've survived the level! Would you like to continue to the next level?", 
		"You've won!", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
		if(choice==JOptionPane.YES_OPTION){
			switch(currentScreenType){
			case L1:{
				((GameBoard) currentGameView).changeScreenTo(Screens.L2Pre);
				break;
			}
			case L2:{
				((GameBoard) currentGameView).changeScreenTo(Screens.L3Pre);
				break;
			}
			case L4:{
				((GameBoard) currentGameView).changeScreenTo(Screens.L4Pre);
				break;}
			default:
				break;
			}
		}
		}
		else{
			JOptionPane.showMessageDialog(currentLevelScreen, "You've completed the last level, "
					+ "press ok to continue to the main menu!");
			((GameBoard) currentGameView).changeScreenTo(Screens.MAIN);
		}
	}

	public void setNumEnemies(int numEnemies) {
		this.numEnemies = numEnemies;
	}

	public int getGameObjSpeed() {
		return gameObjSpeed;
	}

	public void setGameObjSpeed(int enemySpeed) {
		this.gameObjSpeed = enemySpeed;
	}
	
    public void movePlayer(Point mousePoint) {

        if(mousePoint.x > player.getCoordinates().getxPos() - 5 && mousePoint.x < player.getCoordinates().getxPos() + 5
                && mousePoint.y > player.getCoordinates().getyPos() - 5 && mousePoint.y < player.getCoordinates().getyPos() + 5) return;

        float angle = (float) Math.toDegrees(Math.atan2(mousePoint.y - player.getCoordinates().getyPos(),
                mousePoint.x - player.getCoordinates().getxPos()));

        if(angle < 0){
            angle += 360;
        }
        
        player.setDirectionAngle(angle);

        double x = (gameObjSpeed * Math.cos(Math.toRadians(angle)));
        double y = (gameObjSpeed * Math.sin(Math.toRadians(angle)));

        player.moveByVector(new MovingVector(x, y));

    }
    
    public void moveEnemies() {

        for(int i = 0; i< listEnemies.length; i++) {
            Coordinates ballCoordinates = listEnemies[i].getCoordinates();
            if(ballCoordinates.getxPos() < enemySpawnFrame.getXBeginning()
                    || ballCoordinates.getxPos() > enemySpawnFrame.getXEnding()
                    || ballCoordinates.getyPos() > enemySpawnFrame.getYEnding()
                    || ballCoordinates.getyPos() < enemySpawnFrame.getYBeginning() ) {
                Coordinates newBallCoordinates = randomizeCoordinates();
                MovingVector movingVector = randomizeMovingVector();
                Enemy anEnemy = new Enemy(newBallCoordinates, movingVector, 
                		2*enemyCollisionRad, listEnemies[i].checkEnemyType());
                listEnemies[i] = anEnemy;
            }

            if(checkForCollision(listEnemies[i])) {
            	playSound("sounds/collide.wav");
                int currentHealthPoints = getHealthBar().getCurrentPoints();
                getHealthBar().setCurrentPoints(currentHealthPoints - 30);
                if(getHealthBar().getCurrentPoints()<=0 && notQ) {
                	currentLevelScreen.pause();
                	notQ=false;
                	if((currentLevelScreen.getNumQsRemaining()-1)<0)
                		currentLevelScreen.endGame();
                	else
                		currentLevelScreen.runDeathScenario();
                		
                }

                Coordinates newBallCoordinates = randomizeCoordinates();
                MovingVector movingVector = randomizeMovingVector();
                Enemy anEnemy = new Enemy(newBallCoordinates, movingVector, 
                		2*enemyCollisionRad, listEnemies[i].checkEnemyType());
                listEnemies[i] = anEnemy;
            }

            listEnemies[i].moveByVector();
        }

    }
    
    private Coordinates randomizeCoordinates() {
        
        Double random = Math.random();
        
        Coordinates randomizedCoordinates;
        
        int xPos;
        int yPos;

        if(random < 0.25) {
            xPos = (int) enemySpawnFrame.getXBeginning();
            yPos = (int)(Math.random()*enemySpawnFrame.getYEnding());
        } else if(random >= 0.25 && random < 0.50) {
            xPos = (int)(Math.random()*enemySpawnFrame.getXEnding());
            yPos = (int) enemySpawnFrame.getYBeginning();
        } else if(random >= 0.50 && random < 0.75) {
            xPos = (int) enemySpawnFrame.getXEnding();
            yPos = (int) (Math.random()*enemySpawnFrame.getYEnding());
        } else {
            xPos = (int) (Math.random()*enemySpawnFrame.getXEnding());
            yPos = (int) enemySpawnFrame.getYEnding();
        }

        randomizedCoordinates = new Coordinates(xPos, yPos);
        
        return randomizedCoordinates;
        
    }
    
    private MovingVector randomizeMovingVector() {
        double angle = Math.random() * 360;
        double x = (gameObjSpeed * Math.cos(Math.toRadians(angle)));
        double y = (gameObjSpeed * Math.sin(Math.toRadians(angle)));

        MovingVector movingVector = new MovingVector(x, y);
        return movingVector;
    }
    
    private boolean checkForCollision(Enemy anEnemy) {

        double enemyXCoordinates = anEnemy.getCoordinates().getxPos();
        double enemyYCoordinates = anEnemy.getCoordinates().getyPos();

        if(player.getCoordinates().getxPos() - playerCollisionRad < enemyXCoordinates + enemyCollisionRad
                && enemyXCoordinates - enemyCollisionRad < player.getCoordinates().getxPos() + playerCollisionRad
                && player.getCoordinates().getyPos() - playerCollisionRad < enemyYCoordinates + enemyCollisionRad
                && player.getCoordinates().getyPos() + playerCollisionRad > enemyYCoordinates - enemyCollisionRad) {
            return true;
        }
        return false;

    }
    private void initListEnemies() {
        for(int i = 0; i<numInvasiveEnemy; i++){
            Coordinates ballCoordinates = randomizeCoordinates();
            MovingVector movingVector = randomizeMovingVector();
            Enemy anEnemy = new Enemy(ballCoordinates, movingVector, 2*enemyCollisionRad, true);
            listEnemies[i] = anEnemy;
        }
        for(int i = 0; i< numPollutionEnemy; i++){
            Coordinates ballCoordinates = randomizeCoordinates();
            MovingVector movingVector = randomizeMovingVector();
            Enemy anEnemy = new Enemy(ballCoordinates, movingVector, 2*enemyCollisionRad, false);
            listEnemies[numInvasiveEnemy+i] = anEnemy;
        }
    }
    void playSound(String soundFile) {
        File f = new File("./" + soundFile);
        AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioIn);
	        clip.start();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

    }
    
    public Enemy[] getListEnemies() {
        return listEnemies;
    }
    
    public Player getPlayer() {
        return player;
    }

	public HealthBar getHealthBar() {
		// TODO Auto-generated method stub
		return hb;
	}
	
	
}
