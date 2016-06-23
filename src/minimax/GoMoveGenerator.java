package minimax;

import java.util.ArrayList;
import java.util.List;

import ai.game_abstractions.MoveGenerator;

public class GoMoveGenerator extends MoveGenerator<GoState, GoMove> {

	private ArrayList<Integer[]> list = new ArrayList<Integer[]>();
	@Override
	public Iterable<GoMove> getLegalMoves(GoState state) {
		List<GoMove> moves = new ArrayList<GoMove>();
		
		
		long lastmove = state.getBoard().getLastMove();
		int[] coords = state.getBoard().highestBitToCoords(lastmove);
		// long neighbs =
		// state.getBoard().getNeighbors()[coords[0]*state.getBoard().getWidth()+coords[1]];
		
		
		/*
		 * if (state.getBoard().isLegal(coords[0]+1, coords[1])){
		 * list.add({coords[0]+1,coords[1]});
		 * 
		 * moves.add(new GoMove(coords[0]+1,coords[1])); } if
		 * (state.getBoard().isLegal(coords[0]-1, coords[1])){
		 * list.add(coords[0]-1); list.add(coords[1]); moves.add(new
		 * GoMove(coords[0]-1,coords[1])); } if
		 * (state.getBoard().isLegal(coords[0], coords[1]+1)){
		 * list.add(coords[0]); list.add(coords[1]+1); moves.add(new
		 * GoMove(coords[0],coords[1]+1)); } if
		 * (state.getBoard().isLegal(coords[0]+1, coords[1]-1)){
		 * list.add(coords[0]); list.add(coords[1]-1); moves.add(new
		 * GoMove(coords[0],coords[1]-1)); }
		 */
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
