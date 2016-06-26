package minimax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.game_abstractions.MoveGenerator;

public class GoMoveGenerator extends MoveGenerator<GoState, GoMove> {

	private ArrayList<Integer[]> list = new ArrayList<Integer[]>();

	public int[][] neighbCoords(int row, int col) {
		int[][] coords = new int[][] { { row, col - 1 }, { row, col + 1 },
				{ row + 1, col }, { row - 1, col }, { row + 1, col + 1 },
				{ row + 1, col - 1 }, { row - 1, col + 1 },
				{ row - 1, col - 1 } };

		return coords;
	}

	@Override
	public Iterable<GoMove> getLegalMoves(GoState state) {
		List<GoMove> moves = new ArrayList<GoMove>();
		boolean[] checked = new boolean[state.getBoard().getWidth()
				* state.getBoard().getHeight()];
		Arrays.fill(checked, false);
		
		long lastmove = state.getBoard().getLastMove();
		int[] coords = state.getBoard().highestBitToCoords(lastmove);
		int row = coords[0];
		int col = coords[1];
		int width = state.getBoard().getWidth();

		if (state.getBoard().isLegal(coords[0] + 1, coords[1])) {
			moves.add(new GoMove(coords[0] + 1, coords[1]));
			checked[(row + 1) * width + col] = true;
		}
		if (state.getBoard().isLegal(coords[0] - 1, coords[1])) {
			moves.add(new GoMove(coords[0] - 1, coords[1]));
			checked[(row - 1) * width + col] = true;
		}
		if (state.getBoard().isLegal(coords[0], coords[1] + 1)) {
			moves.add(new GoMove(coords[0], coords[1] + 1));
			checked[(row) * width + col + 1] = true;
		}
		if (state.getBoard().isLegal(coords[0], coords[1] - 1)) {
			moves.add(new GoMove(coords[0], coords[1] - 1));
			checked[(row) * width + col - 1] = true;
		}
		if (state.getBoard().isLegal(coords[0] - 1, coords[1] - 1)) {
			moves.add(new GoMove(coords[0] - 1, coords[1] - 1));
			checked[(row - 1) * width + col - 1] = true;
		}
		if (state.getBoard().isLegal(coords[0] - 1, coords[1] + 1)) {
			moves.add(new GoMove(coords[0] - 1, coords[1] + 1));
			checked[(row - 1) * width + col + 1] = true;
		}
		if (state.getBoard().isLegal(coords[0] + 1, coords[1] - 1)) {
			moves.add(new GoMove(coords[0] + 1, coords[1] - 1));
			checked[(row + 1) * width + col - 1] = true;
		}
		if (state.getBoard().isLegal(coords[0] - 1, coords[1] + 1)) {
			moves.add(new GoMove(coords[0] - 1, coords[1] + 1));
			checked[(row - 1) * width + col + 1] = true;
		}
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

		for (row = 0; row < state.getBoard().getHeight(); row++) {
			
			for (col = 0; col < state.getBoard().getWidth(); col++) {
				if (checked[row * width + col])
					continue;
				if (state.getBoard().isLegal(row, col))
					moves.add(new GoMove(row, col));
			}
		}
		moves.add(new GoMove(-1, -1));
		
		return moves;
	}




}
