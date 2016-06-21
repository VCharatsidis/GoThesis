package minimax;

import lombok.Getter;
import BitMap.BitBoard;
import ai.game_abstractions.GameState;

public class GoState extends GameState {

	@Getter
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoState other = (GoState) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		return true;
	}
	
	
}
