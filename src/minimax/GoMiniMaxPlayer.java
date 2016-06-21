package minimax;

import lombok.Getter;

import org.apache.commons.lang3.tuple.Pair;

import utils.StringFormatting;
import ai.game_abstractions.EvaluationFucntion;
import ai.game_abstractions.MoveGenerator;
import ai.search_techniques.NegaMax;


public class GoMiniMaxPlayer {
	private double gametime;
	private double gametimeRemaining;

	@Getter
	private int currentMove = 0;

	/** Estimated length of game in moves */
	private final int estimatedGameLength = 30;

	NegaMax<GoState, GoMove, MoveGenerator<GoState, GoMove>, GoRules, EvaluationFucntion<GoState>> negaMax;

	private final int iterativeDeepeningStep = 1;

	private final int maxDepth = 21;

	public GoMiniMaxPlayer(double timeForGameInSeconds, EvaluationFucntion<GoState> evaluationFunction) {
	    this.gametime = timeForGameInSeconds;
	    this.gametimeRemaining = timeForGameInSeconds;
	    GoMoveGenerator moveGenerator = new GoMoveGenerator();
	    GoRules rules = new GoRules();
		negaMax = new NegaMax<GoState, GoMove, MoveGenerator<GoState, GoMove>, GoRules, EvaluationFucntion<GoState>>(
				moveGenerator, rules, evaluationFunction);
	  }
	
	public void reset() {
		gametimeRemaining = gametime ;
	    currentMove = 0;
	  }
	
	public Pair<Integer, GoMove> play(GoState state) {
	    int estimatedMovesLeft = Math.max(15, estimatedGameLength - currentMove);
	    System.out.println("rem " + gametimeRemaining + " move " + currentMove);
	    double timeForMove = gametimeRemaining / estimatedMovesLeft;
	    System.out.println("time allowed " + timeForMove);
	    Pair<Integer, GoMove> bestOption = runIterativeDeepening(state, timeForMove);
	    gametimeRemaining -= negaMax.getElapsedTimeInMs() / 1000.0;
	    currentMove++;
	    return bestOption;
	  }
	
	public Pair<Integer, GoMove> runIterativeDeepening(GoState state, double timeInSeconds) {
		Pair<Integer, GoMove> bestOption = negaMax
				.searchWithIterativeDeepening(state, iterativeDeepeningStep,
						timeInSeconds, maxDepth);
	    double milliNodesPerSeconds = negaMax.getMilliNodesPerSeconds();
	    System.out.println(StringFormatting.decimalFormat(milliNodesPerSeconds, 3) + "M nps " + " time: "
	                       + negaMax.getElapsedTimeInMs() + " ms" + "");
	    int numNodesPruned = negaMax.getNumNodesPruned();
	    int numNodesExpanded = negaMax.getNumNodesExpanded();
	    double percentagePruned = 100 * numNodesPruned / (numNodesExpanded + 1); // To avoid accidental division by 0
	    System.out.println("Nodes searched " + StringFormatting.decimalFormat(numNodesExpanded / 1000000., 2) + "M "
	                       + StringFormatting.decimalFormat(percentagePruned, 1) + "% pruned ");
	    return bestOption;
	  }

}
