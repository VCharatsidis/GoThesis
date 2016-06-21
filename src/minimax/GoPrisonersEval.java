package minimax;

import ai.game_abstractions.EvaluationFucntion;

public class GoPrisonersEval extends EvaluationFucntion<GoState> {

	@Override
	public int evaluate(GoState state) {
			return state.getBoard().getWhiteCaptives()
					- state.getBoard().getBlackCaptives();
	
	}
}