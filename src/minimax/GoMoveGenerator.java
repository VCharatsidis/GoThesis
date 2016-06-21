package minimax;

import java.util.ArrayList;
import java.util.List;

import ai.game_abstractions.MoveGenerator;

public class GoMoveGenerator extends MoveGenerator<GoState, GoMove> {

	@Override
	public Iterable<GoMove> getLegalMoves(GoState state) {
		List<GoMove> moves = new ArrayList<GoMove>();
		
		for (int row =0; row<state.getBoard().getHeight(); row++){
			for(int col=0; col<state.getBoard().getWidth(); col++){
				if (state.getBoard().isLegal(row, col))
					moves.add(new GoMove(row, col));
			}
		}
		moves.add(new GoMove(-1, -1));
		
		return moves;
	}


}
