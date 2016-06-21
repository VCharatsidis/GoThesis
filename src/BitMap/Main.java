package BitMap;



import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Builds UI and starts the game.
 *
 */
public class Main {

public static final String TITLE = "";
public static final int BORDER_SIZE = 25;

public static void main(String[] args) {
    new Main().init();
}

private void init() {
	
	
    JFrame f = new JFrame();
    f.setTitle(TITLE);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    JPanel container = new JPanel();
    container.setBackground(Color.GRAY);
    container.setLayout(new BorderLayout());
    f.add(container);
    container.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
    int height=7;
    int width=7;
    BitBoard startingboard = new BitBoard(height,width);


  
    Renderer board = new Renderer(startingboard);
    container.add(board);

    f.pack();
    f.setResizable(false);
    f.setLocationByPlatform(true);
    f.setVisible(true);
}}
