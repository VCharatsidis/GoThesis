package minimax;

import ai.game_abstractions.GameRules;

public class GoRules extends GameRules<GoState, GoMove> {

	@Override
	public ai.game_abstractions.GameRules.Player getActivePlayer(GoState state) {
		return state.getBoard().isBlackToplay() ? Player.MAX : Player.MIN;

	}

	@Override
	public boolean isTerminal(GoState state) {
		// TODO Auto-generated method stub
		return state.getBoard().isGameOver();
	}

	@Override
	public GoState applyMove(GoState state, GoMove move) {
		GoState newState = state.copy();
		if (move.isPass()) {
			newState.getBoard().pass();
		} else {
			newState.getBoard().addStone(move.getRow(), move.getCol());
		}
		return newState;
	}

}
