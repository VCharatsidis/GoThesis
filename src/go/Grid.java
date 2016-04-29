package go;

import go.GameBoard.State;

import java.util.ArrayList;



/**
 * Provides game logic.
 *  
 *
 */
public class Grid {

private final int SIZE;
/**
 * [row][column]
 */
private Stone[][] stones;
private Stone[] neighbors;


public Grid(int size) {
    SIZE = size;
    stones = new Stone[SIZE][SIZE];
    
}

/**
 * Adds Stone to Grid.
 * 
 * @param row
 * @param col
 * @param black
 */
public void findNeighbors(int row ,int col ){
	int cnt=4;
    // finding how many liberties have this coordinates so we make slots for neighbors
    if (row > 0) {
    	cnt--;
        
    }
    if (row < SIZE - 1) {
    	cnt--;
        
    }
    if (col > 0) {
    	cnt--;
        
    }
    if (col < SIZE - 1) {
    	cnt--;
        
    }
    neighbors = new Stone[4-cnt];
    int i =0;
	if (row > 0) {
    	
        neighbors[i] = stones[row - 1][col];
        i=i+1;
    }
    if (row < SIZE - 1) {
    	
        neighbors[i] = stones[row + 1][col];
        i=i+1;
    }
    if (col > 0) {
    	
        neighbors[i] = stones[row][col - 1];
        i=i+1;
    }
    if (col < SIZE - 1) {
        neighbors[i] = stones[row][col + 1];
    }
}	
    
    


public void addStone(int row, int col, State state) {
    Stone newStone = new Stone(row, col, state);
    
    System.out.print(" newStone liberties = " + newStone.liberties);
    
    ArrayList<Stone> newChain = new ArrayList<Stone>();
    Chain finalChain = new Chain(newChain,newStone.state);
    stones[row][col]=newStone;
    findNeighbors(row,col);
    int cnt=0;
    for (int i=0;i<neighbors.length;i++){
    	if (neighbors[i] != null)
    		cnt++;
    }
    System.out.println(" neighbors are ="+cnt);
	// Prepare Chain for this new Stone
	
    for (Stone neighbor : neighbors) {
        // Do nothing if no adjacent Stone
        if (neighbor == null) {
            continue;
        }

        newStone.liberties--;
        neighbor.liberties--;

        // If it's different color than newStone check him
        if (neighbor.state != newStone.state) {
            checkStone(neighbor);
            
        }

        if ((neighbor.chain != null) && (neighbor.chain!=newStone.chain) && (neighbor.state==newStone.state)) {
        	
        		neighbor.chain.join(newStone.chain);
        		newStone.chain = neighbor.chain;
        	
        }
    }
    
}
/**
 * Check liberties of Stone
 * 
 * @param stone
 */

public void restoreLiberties(Stone stone){
	findNeighbors(stone.row,stone.col);
	for (Stone s : neighbors){
		if ((s!=null) && (s.state!=stone.state))
			s.liberties++;
	}
}
public void checkStone(Stone stone) {
    // Every Stone is part of a Chain so we check total liberties
	if (stone.chain!=null){
		System.out.println("stone in"+stone.row+" "+stone.col+".chain.getLiberties = " + stone.chain.getLiberties());
		System.out.println("size of chain = "+ stone.chain.stones.size());
	}
    if (stone.chain.getLiberties() == 0) {
        for (Stone s : stone.chain.stones) {
        	System.out.println("inside checkstone");
           // s.chain = null;
            restoreLiberties(s);
            stones[s.row][s.col] = null;
        }
        stone.chain = null;
    }
}

/**
 * Returns true if given position is occupied by any stone
 * 
 * @param row
 * @param col
 * @return true if given position is occupied
 */

public boolean isNotLegal (int row,int col,State state){
	
	int chainsum=0;
	for(Stone neighbor:neighbors){
		if ((neighbor==null) )
			return false;
		
		if  (neighbor.state==state) {
	     	chainsum = chainsum + neighbor.chain.getLiberties()-1;
        }
		if (neighbor.state!=state){
			if (neighbor.chain.getLiberties()-1==0)
				return false;
		}
			
		
	}
	if (chainsum==0){
		return true;
	}
	return false;
}

public boolean isOccupied(int row, int col) {
    return stones[row][col] != null;
}

/**
 * Returns State (black/white) of given position or null if it's unoccupied.
 * Needs valid row and column.
 * 
 * @param row
 * @param col
 * @return
 */
public State getState(int row, int col) {
    Stone stone = stones[row][col];
    if (stone == null) {
        return null;
    } else {
        // System.out.println("getState != null");
        return stone.state;
    }
}
public void test(){
	for (int i=0;i<SIZE;i++){
		System.out.println("");
		for(int j=0;j<SIZE;j++){
			if(stones[i][j]!=null)
				System.out.print("1");
			else
				System.out.print("0");
		}
	}
	
}
}