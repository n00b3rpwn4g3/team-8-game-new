package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Controller;
import model.Enemy;
import model.Game;
import model.HealthBar;
import model.Player;
import model.Game.DifficultyLVL;
import model.GameTimer;
import view.ScreenPanel.Screens;

public class LevelScreen extends ScreenPanel {

	private Image bgImage;
	private int DEFAULT_WIDTH = 800;
	private int DEFAULT_HEIGHT = 600;
	private int numLinesPreLevel = 8;
	private int numQs = 4;
	private int numQsRemaining=4;
	private int numCorrectAns = 4;
	private int numIncorrectAnsPerQ = 3;
	private Screens currentScreen;
	private Image playerIMG;
	private Image invasiveIMG;
	private Image pollutionIMG;
	private int correctIndex;
	private ArrayList<String> listQuestions = new ArrayList<String>();
	private ArrayList<String> listCorrectAns = new ArrayList<String>();
	private ArrayList<String> listIncorrectAns = new ArrayList<String>();
	private ArrayList<Integer> possibleIndices = new ArrayList<Integer>();
	private ArrayList<Integer> setSizeQminus1 = new ArrayList<Integer>();
	private Game currentGame;
	private static int[] invalidIndices = new int[4];
	private JFrame parent;
	int windowWidth = 800;
	int windowHeight = 600;
	int gameObjectSize = 10;
	private LevelScreen thisLevel;
	private boolean isStarted = false;
	private DifficultyLVL difficulty;
	private Controller currentGameController;

	@Override
	public void addNotify() {
		super.addNotify();
		requestFocus();
	}

	public LevelScreen(Screens s, JFrame parent) {
		super();
		this.setLayout(new BorderLayout());
		this.validate();
		this.setOpaque(true);
		this.parent = parent;
		currentScreen = s;
		if(currentScreen!=Screens.TUTORIAL){
			difficulty = ((GameBoard) parent).getDifficulty();
			initIndexingSets();
			cnfgFieldsFromTXT(s);
		}
		else
			difficulty = DifficultyLVL.TUTORIAL;
		thisLevel = this;
		initImageIcons();
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		repaint();
		this.setVisible(true);
		initGame(difficulty);
	}

	public Game getGame() {
		return currentGame;
	}
	
	public void initGame(DifficultyLVL d){
		currentGame = new Game(parent, this, d);
	}

	void startGame() {
		isStarted = true;
		((GameBoard) parent).setIsGame(true);
		currentGameController = new Controller(40, this);
		currentGameController.tick();
		pause();
		JOptionPane.showMessageDialog(parent, "Click ok to start");
		unpause();
		
	}
	
	private void initIndexingSets(){
		
		for (int i=0; i<numQs; i++){
			possibleIndices.add(i);
			setSizeQminus1.add(i);
			invalidIndices[i]=0;
		}
	}
	
	public void unpause(){
		currentGameController.tick();
	}

	public void refreshView() {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		
	    super.paintComponent(g);
	    if (bgImage != null)
	    {
	        g.drawImage(bgImage,0,0,this);
	    }

		if (isStarted) {

			paintPlayer(g);

			if (currentGame.getListEnemies() != null) {
				paintBalls(g);
			}

			paintHealthBar(g);
			paintGameTimer(g);
		}
	}
	
	public void pause(){
		currentGameController.pause();
	}

	private void paintHealthBar(Graphics g) {
		new HealthBarView(currentGame.getHealthBar()).draw(g);
	}
	
	private void paintGameTimer(Graphics g){
		GameTimerView gtv = new GameTimerView(currentGame.getGameTimer());
		gtv.drawBar(g);
	}

	private void paintPlayer(Graphics g) {
		new PlayerView(currentGame.getPlayer(), playerIMG).draw(g);
	}

	private void paintBalls(Graphics g) {
		Enemy[] listEnemies = currentGame.getListEnemies();

		for (int i = 0; i < currentGame.getNumInvasive(); i++) 
			new EnemyView(listEnemies[i], invasiveIMG).draw(g);
		
		for (int i = 0; i<currentGame.getNumPollution(); i++)
			new EnemyView(listEnemies[currentGame.getNumInvasive()+i], pollutionIMG).draw(g);
	}


	public void initImageIcons() {
		bgImage = uploadImage(currentScreen.name());
		playerIMG = uploadImage(currentScreen.name() + "PLAYER");
		invasiveIMG = uploadImage(currentScreen.name() + "INVASIVE");
		pollutionIMG = uploadImage(currentScreen.name() + "POLLUTION");
	}
	
//	public int getImageDiameter(Image img){
//		return Math.max(playerIMG.getHeight(null), playerIMG.getWidth(null));
//	}
//	
	public int getPlayerDiameter(){
		return Math.max(playerIMG.getHeight(null), playerIMG.getWidth(null));
	}
	
	public int getEnemyDiameter(){
		return Math.max(Math.max(invasiveIMG.getHeight(null), invasiveIMG.getWidth(null))
				,Math.max(pollutionIMG.getHeight(null), pollutionIMG.getWidth(null)));
	}
	
	public void runDeathScenario(){
		generateRandomQ();
	}

	private void cnfgFieldsFromTXT(Screens currentScreen) {
		ArrayList<String> allText = readTXTFile(currentScreen.name());  
		for (int i = 0; i < numQs; i++)
			listQuestions.add(allText.get(numLinesPreLevel + i));
		int numLinesPrior = numLinesPreLevel+numQs;
		for (int i = 0; i < numCorrectAns; i++)
			listCorrectAns.add(allText.get(numLinesPrior + i));
		numLinesPrior+=numCorrectAns;
		int numIncorrect = numIncorrectAnsPerQ*numQs;
		for (int i = 0; i<numIncorrect; i++)
			listIncorrectAns.add(allText.get(numLinesPrior+i));
		
	}
	
	// a linear search on the list, not biased since it either decrements or increments, if just kept checking randomly could possibly never find valid index
	public int getUnusedIndex(int i){
		int randomSign = (Math.random()<.5) ? -1 : 1;
		boolean foundValid=false;
		int index = i;
		if(randomSign<0){
			while((index-1)>=0 && !foundValid){
				foundValid=(invalidIndices[--index]>=0);
			}
			if(foundValid)
				return index;
			index = i;
			while((index+1)<possibleIndices.size() && !foundValid){
				foundValid=(invalidIndices[++index]>=0);
			}
			if(foundValid)
				return index;
		}
		else{
			while((index+1)<possibleIndices.size() && !foundValid){
				foundValid=(invalidIndices[++index]>=0);
			}
			if(foundValid)
				return index;
			index = i;
			while((index-1)>=0 && !foundValid){
				foundValid=(invalidIndices[--index]>=0);
			}
			if(foundValid)
				return index;
			
		}
		return -1;
	}
	
	public void generateRandomQ(){
		int rand = (int) Math.floor(Math.random()*numQs);
		if(invalidIndices[rand]<0){
			rand=getUnusedIndex(rand);
			System.out.println("New random: "+rand);
		}
		possibleIndices.get(rand);
		invalidIndices = updateInvalidIndices(invalidIndices, rand);
		int incorrectStartInd = numIncorrectAnsPerQ*rand;
		numQsRemaining--;
		
		
		JButton ans1 = new JButton(listCorrectAns.get(rand));
	    JButton ans2 = new JButton(listIncorrectAns.get(incorrectStartInd));
	    JButton ans3 = new JButton(listIncorrectAns.get(incorrectStartInd+1));
	    JButton ans4 = new JButton(listIncorrectAns.get(incorrectStartInd+2));
	    JLabel instructionsLabel = new JLabel(listQuestions.get(rand));
    	
    	JFrame window = new JFrame("A chance at a new life");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        panel.add(instructionsLabel, c);
        setSizeQminus1.remove(0);    
        Collections.shuffle(setSizeQminus1);
        correctIndex = generateRandomRangeQ();
        for(int i=0; i<numQs; i++){
    		c.gridy = (i+1);
        	if(correctIndex==i)
        		panel.add(ans1, c);
        	else{
        		int anotherAns = setSizeQminus1.remove(0);
        		switch(anotherAns){
        		case(1):{
        			panel.add(ans2,c);
        			break;}
        		case(2):{
        			panel.add(ans3, c);
        			break;
        		}
        		case(3):{
        			panel.add(ans4, c);
        			break;
        		}
        		}
        	}
        }
        reinitializeIndexingSets();
        window.add(panel);
        window.setSize(250, 270);
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        ans1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(parent, "That is correct! Press ok to resume");
            	currentGame.getHealthBar().setCurrentPoints(200);
            	refreshView();
            	window.setVisible(false);
            	currentGame.setNotQ(true);
            	unpause();
            }
        });
        
        ans2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(parent, "That is incorrect!");
            	int choice = JOptionPane.showConfirmDialog(thisLevel, "Would you like to restart this level?", "End game", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
            	if (choice==JOptionPane.YES_OPTION)
            		((GameBoard) parent).changeScreenTo(currentScreen);
            	else
            		((GameBoard) parent).changeScreenTo(Screens.MAIN);
            	window.setVisible(false);
            }
        });
        
        ans3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(parent, "That is incorrect!");
            	int choice = JOptionPane.showConfirmDialog(thisLevel, "Would you like to restart this level?", "End game", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
            	if (choice==JOptionPane.YES_OPTION)
            		((GameBoard) parent).changeScreenTo(currentScreen);
            	else
            		((GameBoard) parent).changeScreenTo(Screens.MAIN);
            	window.setVisible(false);
            }
        });
        
        ans4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(parent, "That is incorrect!");
            	int choice = JOptionPane.showConfirmDialog(thisLevel, "Would you like to restart this level?", "End game", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
            	if (choice==JOptionPane.YES_OPTION)
            		((GameBoard) parent).changeScreenTo(currentScreen);
            	else
            		((GameBoard) parent).changeScreenTo(Screens.MAIN);
            	window.setVisible(false);
            }
        });
        
		window.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	JOptionPane.showMessageDialog(parent, "You will now be exited to the main menu, click OK to continue");
		    	((GameBoard) parent).changeScreenTo(Screens.MAIN);
		        }
		    
		});
	}
	
	private int[] updateInvalidIndices(int[] invalidIndicesOrig, int rand) {
		invalidIndicesOrig[rand]=-1;
		return invalidIndicesOrig;
	}

	public int getNumQsRemaining(){
		return numQsRemaining;
	}
	public int generateRandomRangeQ(){
		return (int) Math.floor(Math.random()*numQs);
	}
	public Screens getScreenType(){
		return currentScreen;
	}
	
	public void endGame(){
    	int choice = JOptionPane.showConfirmDialog(thisLevel, "You are out of lives, select yes to restart the level or no to exit to the Main Menu", "End game", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
    	if (choice==JOptionPane.YES_OPTION)
    		((GameBoard) parent).changeScreenTo(currentScreen);
    	else
    		((GameBoard) parent).changeScreenTo(Screens.MAIN);
	}
	
    public void reinitializeIndexingSets()
    {
    	setSizeQminus1.clear();
    	for(int i=0; i<numQs; i++){
    		if(invalidIndices[i]>=0)
    			invalidIndices[i]=0;
    		setSizeQminus1.add(i);
    	}
    }

	public void runTutorialMessage(int seconds) {
		if(seconds==1){
			pause();
        	JOptionPane.showMessageDialog(parent, "Slide the mouse to move the player");
			unpause();
		}
		if(seconds==3){
			pause();
        	JOptionPane.showMessageDialog(parent, "Try to avoid the invasive species and pollution objects, each collision decreases your health. The health bar is in the upper left corner");
        	unpause();
		}
		if(seconds==5){
			pause();
        	JOptionPane.showMessageDialog(parent, "In order to win the level, you must survive until the time reaches 0. The time bar is the green rectangle in the lower right corner");
        	unpause();
		}
		if(seconds==8){
        	pause();
        	currentGame.getHealthBar().setCurrentPoints(0);
        	refreshView();
        	JOptionPane.showMessageDialog(parent, "If your health goes to zero, you can answer a question correctly to get a new life, but there are only 4 questions per level");
    	    
        	JLabel instructionsLabel = new JLabel("Select the correct answer to restore all health");
        	JButton ans1 = new JButton("Sample Correct Answer (CLICK ME)");
    	    JButton ans2 = new JButton("Sample Incorrect Answer");
    	    JButton ans3 = new JButton("Sample Incorrect Answer");
    	    JButton ans4 = new JButton("Sample Incorrect Answer");
        	
        	JFrame window = new JFrame("A sample question");
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            
            c.gridx = 0;
            c.gridy = 0;
            c.weighty = 0.1;
            panel.add(instructionsLabel, c);
            c.gridy = 1;
        	panel.add(ans1, c);
            c.gridy = 2;
        	panel.add(ans2, c);
            c.gridy = 3;
        	panel.add(ans3, c);
            c.gridy = 4;
        	panel.add(ans4, c);;
            window.add(panel);
            window.setSize(250, 270);
            window.setResizable(false);
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            ans1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	JOptionPane.showMessageDialog(parent, "That is correct! Press ok to resume");
                	currentGame.getHealthBar().setCurrentPoints(1000);
                	refreshView();
                	window.setVisible(false);
                	unpause();
                }
            });
            
            ans2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	JOptionPane.showMessageDialog(parent, "That is incorrect! But this is only a tutorial so press ok to continue");
                	int choice = JOptionPane.showConfirmDialog(thisLevel, "Would you like to continue this tutorial? Yes will continue the tutorial and no will take you to the menu", "End game", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
                	if (choice==JOptionPane.YES_OPTION)
                	{
                    	currentGame.getHealthBar().setCurrentPoints(1000);
                    	refreshView();
                		unpause();
                	}
                	else{
                		JOptionPane.showMessageDialog(parent, "Press OK to exit to the main menu");
                		((GameBoard) parent).changeScreenTo(Screens.MAIN);
                	}
                	window.setVisible(false);
                }
            });
            
            ans3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	JOptionPane.showMessageDialog(parent, "That is incorrect! But this is only a tutorial so press ok to continue");
                	int choice = JOptionPane.showConfirmDialog(thisLevel, "Would you like to continue this tutorial? Yes will continue the tutorial and no will take you to the menu", "End game", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
                	if (choice==JOptionPane.YES_OPTION)
                	{
                    	currentGame.getHealthBar().setCurrentPoints(1000);
                    	refreshView();
                		unpause();
                	}
                	else{
                		JOptionPane.showMessageDialog(parent, "Press OK to exit to the main menu");
                		((GameBoard) parent).changeScreenTo(Screens.MAIN);
                	}
                	window.setVisible(false);
                }
            });
            
            ans4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	JOptionPane.showMessageDialog(parent, "That is incorrect! But this is only a tutorial so press ok for options");
                	int choice = JOptionPane.showConfirmDialog(thisLevel, "Would you like to continue this tutorial? Yes will continue the tutorial and no will take you to the menu", "End game", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
                	if (choice==JOptionPane.YES_OPTION)
                	{
                    	currentGame.getHealthBar().setCurrentPoints(1000);
                    	refreshView();
                		unpause();
                	}
                	else{
                		JOptionPane.showMessageDialog(parent, "Press OK to exit to the main menu");
                		((GameBoard) parent).changeScreenTo(Screens.MAIN);
                	}
                	window.setVisible(false);
                }
            });
            
    		window.addWindowListener(new java.awt.event.WindowAdapter() {
    		    @Override
    		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
    		    	unpause();
    		        }
    		    
    		});
    	}
		if(seconds==11){
			pause();
			JOptionPane.showMessageDialog(parent, "Other than that, you can choose campaign to play each level or just choose a particular level");
			unpause();
		}
		if(seconds==15){
			pause();
			JOptionPane.showMessageDialog(parent, "That's pretty much all there is to it, click OK to go to the main menu");
    		((GameBoard) parent).changeScreenTo(Screens.MAIN);
		}
			
		
	}

}
