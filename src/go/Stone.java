package go;

import go.GameBoard.State;





/**
 * Basic game element.
 *
 */
public class Stone {

public Chain chain;
public State state;
public int liberties;

// Row and col are need to remove (set to null) this Stone from Grid
public int row;
public int col;

private void checkLiberties(){
	if ((row == 0 ) || (row == (GameBoard.SIZE-1)))
		liberties--;
	if (( col == 0 ) || (col==(GameBoard.SIZE-1)))
		liberties--;
	
}

public Stone(int row, int col, State state) {
    chain = null;
    this.state = state;
    
    liberties = 4;
    
    this.row = row;
    this.col = col;
    checkLiberties();
}
}
