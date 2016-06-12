package BitMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
  * Provides I/O.
  * 
  *
   */
 public class Renderer extends JPanel {

private static final long serialVersionUID = -494530433694385328L;

/**
 * Number of rows/columns.
 */
public static final int SIZE=7;

/**
 * Number of tiles in row/column. (Size - 1)
 */
public static final int N_OF_TILES = SIZE - 1;
public static final int TILE_SIZE = 40;
public static final int BORDER_SIZE = TILE_SIZE;

/**
 * Black/white player/stone
 * 
 *
 */
public enum State {
    BLACK, WHITE
}

private int height=7;
private int width=7;
private boolean current_player;
private BitBoard board;
private Point lastMove;
private Influence inf;


public Renderer(BitBoard board) {
	this.board = board;
    this.setBackground(Color.ORANGE);
    inf =  new Influence(board);
    // Black always starts
    current_player = this.board.getTurn();
    

    addListener(board);
}

private class Mouse extends MouseAdapter{
	@Override
    
    public void mouseReleased(MouseEvent e) {
		
        // Converts to float for float division and then rounds to
        // provide nearest intersection.
        int row = Math.round((float) (e.getY() - BORDER_SIZE)
                / TILE_SIZE);
        int col = Math.round((float) (e.getX() - BORDER_SIZE)
                / TILE_SIZE);

        // DEBUG INFO
        // System.out.println(String.format("y: %d, x: %d", row, col));

        // Check wherever it's valid
        if (row >= height || col >= width || row < 0 || col < 0) {
            return;
        }
           
        //lastMove was after board.addStone
       
        board.addStone(row, col);
        inf.printer();
        lastMove = new Point(col, row);
        current_player =board.getTurn();
        // Switch current player
        repaint();

        
        
        
    }
}
public static void drawBoard(BitBoard board){
	JFrame f = new JFrame();
    
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    JPanel container = new JPanel();
    container.setBackground(Color.GRAY);
    container.setLayout(new BorderLayout());
    f.add(container);
    container.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
    int height=7;
    int width=7;
   
    Renderer drawboard = new Renderer(board);
    container.add(drawboard);

    f.pack();
    f.setResizable(false);
    f.setLocationByPlatform(true);
    f.setVisible(true);
}

private void addListener(BitBoard board) {
	addMouseListener(new Mouse());
}

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(Color.BLACK);
    // Draw rows.
    for (int i = 0; i < SIZE; i++) {
        g2.drawLine(BORDER_SIZE, i * TILE_SIZE + BORDER_SIZE, TILE_SIZE
                * N_OF_TILES + BORDER_SIZE, i * TILE_SIZE + BORDER_SIZE);
    }
    // Draw columns.
    for (int i = 0; i < SIZE; i++) {
        g2.drawLine(i * TILE_SIZE + BORDER_SIZE, BORDER_SIZE, i * TILE_SIZE
                + BORDER_SIZE, TILE_SIZE * N_OF_TILES + BORDER_SIZE);
    }
    // Iterate over intersections
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            
            if (board.occupied(row, col)!=1) {
                if (board.occupied(row,col) == 2) {
                    g2.setColor(Color.BLACK);
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillOval(col * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                        row * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                        TILE_SIZE, TILE_SIZE);
            }
        }
    }
    // Highlight last move
    if (lastMove != null) {
        g2.setColor(Color.RED);
        g2.drawOval(lastMove.x * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                lastMove.y * TILE_SIZE + BORDER_SIZE - TILE_SIZE / 2,
                TILE_SIZE, TILE_SIZE);
    }
}

@Override
public Dimension getPreferredSize() {
    return new Dimension(N_OF_TILES * TILE_SIZE + BORDER_SIZE * 2,
            N_OF_TILES * TILE_SIZE + BORDER_SIZE * 2);
}

}