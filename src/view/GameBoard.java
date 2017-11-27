package view;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Game.DifficultyLVL;
import view.ScreenPanel.Screens;

public class GameBoard extends JFrame implements ActionListener {
	
	private JPanel currentScreen;
	private static JMenuBar customJMenuBar;
	private DifficultyLVL difficulty;
	private boolean isGame = false;
	
	public DifficultyLVL getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(DifficultyLVL difficulty) {
		this.difficulty = difficulty;
	}

	public GameBoard()
	{
		super("Estuary Survival");
	}
	
	public void setIsGame(boolean b){
		isGame = b;
	}
	
	public boolean getIsGame(){
		return isGame;
	}
	
	public void start()
	{
        setSize(800, 600);
        setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initMenuBar();
		setJMenuBar(customJMenuBar);
		currentScreen = new TitleScreen(Screens.TITLE, this);
		add(currentScreen);
		setVisible(true);
	}
	
	public void changeScreenTo(Screens s){
		currentScreen.setVisible(false);
		this.remove(currentScreen);
		switch(s){
		case MAIN:{
			currentScreen = new MainMenuScreen(Screens.MAIN, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			isGame = false;
			break;
		}
		case L1Pre:{
			currentScreen = new PreLevelScreen(Screens.L1Pre, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			isGame = false;
			break;
		}
		case L2Pre:{
			currentScreen = new PreLevelScreen(Screens.L2Pre, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			isGame = false;
			break;
		}
		case L3Pre:{
			currentScreen = new PreLevelScreen(Screens.L3Pre, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			isGame = false;
			break;
		}
		case L4Pre:{
			currentScreen = new PreLevelScreen(Screens.L4Pre, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			isGame = false;
			break;
		}
		case L1:{
			currentScreen = new LevelScreen(Screens.L1, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			((LevelScreen) currentScreen).startGame();
			isGame = true;
			break;
		}
		case L2:{
			currentScreen = new LevelScreen(Screens.L2, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			((LevelScreen) currentScreen).startGame();
			isGame = true;
			break;
		}
		case L3:{
			currentScreen = new LevelScreen(Screens.L3, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			((LevelScreen) currentScreen).startGame();
			isGame = true;
			break;
		}
		case L4:{
			currentScreen = new LevelScreen(Screens.L4, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			((LevelScreen) currentScreen).startGame();
			isGame = true;
			break;
		}
		case TUTORIAL:{
			currentScreen = new LevelScreen(Screens.TUTORIAL, this);
			currentScreen.setVisible(true);
			add(currentScreen);
			((LevelScreen) currentScreen).startGame();
			isGame = true;
			break;
		}
		
		}
	}

	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ("quit"): {
			if (isGame)
				((LevelScreen) currentScreen).pause();
			int choice = JOptionPane.showConfirmDialog(this,
					"This will close the program, are you sure you want to proceed?");
			if (choice == JOptionPane.YES_OPTION)
				System.exit(0);
			else{
				if (isGame)
					((LevelScreen) currentScreen).unpause();
			}
			break;
		}
		case ("exit"): {
			if (isGame)
				((LevelScreen) currentScreen).pause();
			int choice = JOptionPane.showConfirmDialog(this,
					"This will take you to the Main Menu, are you sure you want to proceed?");
			if (choice == JOptionPane.YES_OPTION)
				changeScreenTo(Screens.MAIN);
			else {
				if (isGame)
					((LevelScreen) currentScreen).unpause();
			}
			break;
		}
		case("restart"):{
			if (isGame){
				((LevelScreen) currentScreen).pause();
			int choice = JOptionPane.showConfirmDialog(this,
					"Are you sure you want to restart the level?");
			if (choice == JOptionPane.YES_OPTION)
				changeScreenTo(((LevelScreen) currentScreen).getScreenType());
			else {
					((LevelScreen) currentScreen).unpause();
			}
			}
			else
				JOptionPane.showMessageDialog(this, "You are not playing a game currently");
			break;
		
		}
		case ("changeDiff"): {
			if (isGame)
				((LevelScreen) currentScreen).pause();
			int choice = JOptionPane.showConfirmDialog(this,
					"This will exit you to the Main Menu, are you sure you want to proceed?");
			if (choice == JOptionPane.YES_OPTION)
				changeScreenTo(Screens.MAIN);
			else if (choice == JOptionPane.NO_OPTION) {
				if (isGame)
					((LevelScreen) currentScreen).unpause();
			} else {
				if (isGame)
					((LevelScreen) currentScreen).unpause();
			}
			break;
		}
		case ("showCredits"): {
			if (isGame)
				((LevelScreen) currentScreen).pause();
			buildCreditWindow();
			break;
		}
		case ("exit2Tutorial"): {
			if (isGame)
				((LevelScreen) currentScreen).pause();
			int choice = JOptionPane.showConfirmDialog(this,
					"This will begin the tutorial, are you sure you want to proceed?");
			if (choice == JOptionPane.YES_OPTION)
				changeScreenTo(Screens.TUTORIAL);
			else {
				if (isGame)
					((LevelScreen) currentScreen).unpause();
			}
			break;
		}
		case ("controls"): {
			if (isGame)
				((LevelScreen) currentScreen).pause();
			JOptionPane.showMessageDialog(this, "Slide the mouse in the direction you want to move to avoid enemies");
			if (isGame)
				((LevelScreen) currentScreen).unpause();
			break;
		}
		}
	}

	private void buildCreditWindow() {
		JFrame window = new JFrame("Credits");
		window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		ArrayList<String> listCredits = new ScreenPanel().readTXTFile("CREDITS");
		for (int i = 0; i < listCredits.size(); i++) {
			pane.add(new JLabel(listCredits.get(i)));
		}
		window.add(pane);
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isGame)
					((LevelScreen) currentScreen).unpause();
				window.setVisible(false);
			}
		});
		pane.add(ok);
		window.setVisible(true);
		window.pack();
		
		window.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if(isGame)
		    	((LevelScreen) currentScreen).unpause();
		        }
		    
		});
	}

	public void initMenuBar() {
		customJMenuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu options = new JMenu("Options");
		JMenu help = new JMenu("Help");
		JMenuItem quit = new JMenuItem("Quit game");
		quit.addActionListener(this);
		quit.setActionCommand("quit");
		JMenuItem exit = new JMenuItem("Exit to main menu");
		exit.addActionListener(this);
		exit.setActionCommand("exit");
		JMenuItem restart= new JMenuItem("Restart level");
		restart.addActionListener(this);
		restart.setActionCommand("restart");
		JMenuItem changeDiff = new JMenuItem("Change difficulty");
		changeDiff.addActionListener(this);
		changeDiff.setActionCommand("changeDiff");
		JMenuItem showCredits = new JMenuItem("Show Credits");
		showCredits.addActionListener(this);
		showCredits.setActionCommand("showCredits");
		JMenuItem exit2Tutorial = new JMenuItem("Exit to Tutorial");
		exit2Tutorial.addActionListener(this);
		exit2Tutorial.setActionCommand("exit2Tutorial");
		JMenuItem controls = new JMenuItem("Show controls");
		controls.addActionListener(this);
		controls.setActionCommand("controls");
		file.add(quit);
		file.add(exit);
		options.add(restart);
		options.add(changeDiff);
		options.add(showCredits);
		help.add(exit2Tutorial);
		help.add(controls);
		customJMenuBar.add(file);
		customJMenuBar.add(options);
		customJMenuBar.add(help);
	}
}
