package minimax;

import BitMap.BitBoard;
import ai.game_abstractions.GameState;

public class GoState extends GameState {
	
	private BitBoard board ;
	
	public GoState (BitBoard board){
		this.board = board;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GoState  copy() {
		BitBoard copy = new BitBoard(board);
		return new GoState(copy);
	}
	
	
}
