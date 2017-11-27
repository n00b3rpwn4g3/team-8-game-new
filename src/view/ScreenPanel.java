package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class ScreenPanel extends JPanel
{
private Screens type;

public enum Screens {TITLE, MAIN, L1Pre, L2Pre, L3Pre, L4Pre, L1, L2, L3, L4, TUTORIAL};   //used for cases and file names

public ArrayList<String> readTXTFile(String s){
    String fileName = "/resources/"+s+"INFO.txt";
    String line = null;
    ArrayList<String> parsedFile = new ArrayList<String>();

    try {
        InputStreamReader fileReader = new InputStreamReader(getClass().getResourceAsStream (fileName));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while((line = bufferedReader.readLine()) != null) {
            parsedFile.add(line);
        }   
        bufferedReader.close();         
    }
    catch(FileNotFoundException ex) {
        ex.printStackTrace();            
    }
    catch(IOException ex) {
        ex.printStackTrace();                
    }
    
    return parsedFile;
}

public Image uploadImage(String s){
	Image img = null;
	try {
		img = ImageIO.read(this.getClass().getResource("/images/"+s+".png"));
	} 
	catch (IOException e) {
		e.printStackTrace();
	}
	return img;
}


}