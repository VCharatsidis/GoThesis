package go;

import go.GameBoard.State;

import java.util.ArrayList;

/**
 * A collection of adjacent Stone(s).
 *
 */
public class Chain {

public ArrayList<Stone> stones;
public State state;

public Chain(ArrayList<Stone> stones,State state) {
    this.stones = stones;
    this.state = state;
    
}

public int getLiberties() {
    int total = 0;
    for (Stone stone : stones) {
        total += stone.liberties;
    }
    return total;
}


public void join(Chain chain) {
    stones.addAll(chain.stones);
}

}