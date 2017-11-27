package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import view.ScreenPanel.Screens;

public class TitleScreen extends ScreenPanel implements ActionListener{
	
	private Image bgImage;
	private int DEFAULT_WIDTH = 800;
	private int DEFAULT_HEIGHT = 600;
	private JFrame parentBoard;
	
	public TitleScreen(Screens s, JFrame parent){
		super();
		parentBoard = parent;
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT ));
		this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		bgImage = uploadImage(s.name());
		buildTitleScreen();
		setVisible(true);
		}
	
	public void buildTitleScreen(){
		BoxLayout col = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(col);
		JLabel title = new JLabel("Estuary Survival");
		title.setFont(new Font("Calibri", Font.BOLD, 100));
		add(Box.createRigidArea(new Dimension(0,(int)DEFAULT_HEIGHT/5)));
		this.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
//		title.setVerticalAlignment(SwingConstants.TOP);
		
		JButton start = new JButton("START");
		start.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.setFont(new Font("Calibri", Font.BOLD, 40));
		start.addActionListener(this);
		add(Box.createRigidArea(new Dimension(0,(int)DEFAULT_HEIGHT/8)));
		this.add(start);
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
	    super.paintComponent(g);
	    if (bgImage != null)
	    {
	        g.drawImage(bgImage,0,0,this);
	    }
	}
	
	public void actionPerformed(ActionEvent e){

		((GameBoard) parentBoard).changeScreenTo(Screens.MAIN);
	}
}
