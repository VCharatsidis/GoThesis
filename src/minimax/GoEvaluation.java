package minimax;

import BitMap.Influence;
import ai.game_abstractions.EvaluationFucntion;

public class GoEvaluation extends EvaluationFucntion<GoState> {



	@Override
	public int evaluate(GoState state) {
		Influence inf = new Influence(state.getBoard());
		return (int) (inf.getAllInfluence());
	}

}
