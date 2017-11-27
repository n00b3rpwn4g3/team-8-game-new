package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Game.DifficultyLVL;
import view.ScreenPanel.Screens;

public class PreLevelScreen extends ScreenPanel implements ActionListener{
	
	private Image bgImage;
	private int DEFAULT_WIDTH = 800;
	private int DEFAULT_HEIGHT = 600;
	private Screens currentScreen;
	private Screens associatedLevel;
	private ImageIcon playerIMG;
	private ImageIcon invasiveIMG;
	private ImageIcon pollutionIMG;
	private String levelSetting;
	private String settingINFO;
	private String playerINFO;
	private String invasiveINFO;
	private String pollutionINFO;
	private String playerSpecies;
	private String invasiveSpecies;
	private String pollutionType;
	private JFrame parentBoard;
	private DifficultyLVL difficulty;

//	public PreLevelScreen()
//	{
//		super();
//		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT ));
//		this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
//		this.setVisible(true);
//	}
//	
	public PreLevelScreen(Screens s, JFrame parent){
		super();
		currentScreen = s;
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT ));
		this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		parentBoard = parent;
		bgImage = uploadImage(s.name());
		initAssociatedLevel(s);
		cnfgFieldsFromTXT(s);
		initImageIcons();
		initDisplay();
		repaint();
		this.setVisible(true);
		}
	


	public void initAssociatedLevel(Screens s){
		switch(s){
		case L1Pre:
			associatedLevel=Screens.L1;
			break;
		case L2Pre:
			associatedLevel=Screens.L2;
			break;
		case L3Pre:
			associatedLevel=Screens.L3;
			break;
		case L4Pre:
			associatedLevel=Screens.L4;
			break;
		}
		
	}
	
	public void initImageIcons(){
		playerIMG = new ImageIcon(uploadImage(associatedLevel.name()+"PLAYER"));
		invasiveIMG = new  ImageIcon(uploadImage(associatedLevel.name()+"INVASIVE"));
		pollutionIMG = new ImageIcon(uploadImage(associatedLevel.name()+"POLLUTION"));
	}
	
	public void initDisplay(){
//		BoxLayout theCols = new BoxLayout(this, BoxLayout.X_AXIS);
		BoxLayout col1 = new BoxLayout(this, BoxLayout.Y_AXIS);
/*		BoxLayout col2 = new BoxLayout(this, BoxLayout.Y_AXIS);*/
		JLabel levelLabel = new JLabel(levelSetting +" "+ settingINFO);
		initLabelAppearance(levelLabel);
		levelLabel.setFont(new Font("Calibri", Font.PLAIN, 30));
		JLabel playerLabel = new JLabel("Player Species: "+ playerINFO, playerIMG, JLabel.RIGHT);
		initLabelAppearance(playerLabel);
		playerLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		JLabel invasiveLabel = new JLabel("Invasive Species: "+ invasiveINFO, invasiveIMG, JLabel.RIGHT);
		invasiveLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		initLabelAppearance(invasiveLabel);
		JLabel pollutionLabel = new JLabel("Pollution Type: "+ pollutionINFO, pollutionIMG, JLabel.RIGHT);
		pollutionLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		initLabelAppearance(pollutionLabel);
		JButton start = new JButton("Start Level");
		start.setFont(new Font("Calibri", Font.PLAIN, 20));
		this.setLayout(col1);
		start.addActionListener(this);
		start.setActionCommand("start");
		add(Box.createRigidArea(new Dimension(20,30)));
		this.add(levelLabel);
		add(Box.createRigidArea(new Dimension(20,70)));
		this.add(playerLabel);
		add(Box.createRigidArea(new Dimension(20,70)));
		this.add(invasiveLabel);
		add(Box.createRigidArea(new Dimension(20,70)));
		this.add(pollutionLabel);
		add(Box.createRigidArea(new Dimension(20,50)));
		this.add(start);
		
	}
	
	public void initLabelAppearance(JComponent component){
		component.setForeground(Color.blue);
		component.setBackground(Color.lightGray);
		component.setOpaque(true);
	}
	public void paintComponent(Graphics g)
	{
	    super.paintComponent(g);
	    if (bgImage != null)
	    {
	        g.drawImage(bgImage,0,0,this);
	    }
	}
	
	private void cnfgFieldsFromTXT(Screens currentScreen){
		ArrayList<String> allText = readTXTFile(associatedLevel.name());   // definitely hardcoded, but unified structure and relatively few fields
		levelSetting = allText.get(0);
		settingINFO = allText.get(1);
		playerSpecies = allText.get(2);
		playerINFO = allText.get(3);
		invasiveSpecies= allText.get(4);
		invasiveINFO = allText.get(5);
		pollutionType = allText.get(6);
		pollutionINFO = allText.get(7);
	}
	
	public void actionPerformed(ActionEvent e){
		switch(currentScreen){
		case L1Pre :{
			((GameBoard) parentBoard).changeScreenTo(Screens.L1);
			break;
		}
		case L2Pre:{
			((GameBoard) parentBoard).changeScreenTo(Screens.L2);
			break;
		}
		case L3Pre :{
			((GameBoard) parentBoard).changeScreenTo(Screens.L3);
			break;
		}
		case L4Pre :{
			((GameBoard) parentBoard).changeScreenTo(Screens.L4);
			break;
		}
		}
	}
	
	public DifficultyLVL getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(DifficultyLVL difficulty) {
		this.difficulty = difficulty;
	}
	
	
	
}
