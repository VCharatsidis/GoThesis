package ai.search_techniques;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.tuple.Pair;

import utils.StringFormatting;
import ai.game_abstractions.EvaluationFucntion;
import ai.game_abstractions.GameMove;
import ai.game_abstractions.GameRules;
import ai.game_abstractions.GameRules.Player;
import ai.game_abstractions.GameState;
import ai.game_abstractions.MoveGenerator;

/**
 * Genertic implementation of negamax search with alpha-beta pruning. In order
 * to apply to a new game the only requirement is to implement {@link GameState}
 * , {@link GameMove} ,{@link MoveGenerator} ,{@link GameRules} and
 * {@link EvaluationFucntion} for that game
 */
// @formatter:off
public class NegaMax<State extends GameState, Move extends GameMove<State>, Generator extends MoveGenerator<State, Move>, Rules extends GameRules<State, Move>, EvalFunction extends EvaluationFucntion<State>> {
	// @formatter:on

	private Generator moveGenerator;
	private Rules rules;
	private EvalFunction evaluationFunction;

	@Getter
	private int numNodesExpanded;

	@Getter
	private int numNodesPruned;

	private long startTimeInMs;

	@Getter
	private long elapsedTimeInMs;

	private long allowedTimeInMs = Long.MAX_VALUE;

	@Setter
	private int captureMoveDepthReduction = 0;

	private HashMap<GameState, Pair<Integer, Move>> hashTable = new HashMap<>();
	final boolean useHashTable = true;

	public NegaMax(Generator moveGenerator, Rules rules,
			EvalFunction evaluationFunction) {
		super();
		this.moveGenerator = moveGenerator;
		this.rules = rules;
		this.evaluationFunction = evaluationFunction;
	}

	/**
	 * Returns the best move
	 * 
	 * @param state
	 *            The state to start the search from
	 * @param maxDepth
	 *            The maximum depth
	 * @return
	 */
	public Pair<Integer, Move> search(State state, int maxDepth) {
		reset();
		if (rules.isTerminal(state)) {
			return null;
		}
		Pair<Integer, Move> best = recursiveNegamax(state, 10 * maxDepth,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		elapsedTimeInMs = System.currentTimeMillis() - startTimeInMs;
		// System.out.println("hashTable " + hashTable.size());
		return best;
	}

	/**
	 * @param iterativeDeepeningStep
	 *            The depth increase after every iterative deepening step,
	 *            typically 1 or 2
	 */
	public Pair<Integer, Move> searchWithIterativeDeepening(State state,
			int iterativeDeepeningStep, double timeLimitInSeconds, int maxDepth) {
		boolean output = true;
		reset();
		allowedTimeInMs = (long) (1000 * timeLimitInSeconds);
		Pair<Integer, Move> best = null;
		for (int depth = 1; depth <= maxDepth; depth += iterativeDeepeningStep) {
			try {
				hashTable = new HashMap<>();
				long currentDepthStartTime = System.currentTimeMillis();
				best = recursiveNegamax(state, 10 * depth,
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
				long currentDepthElapsedTime = System.currentTimeMillis()
						- currentDepthStartTime;
				String eval = StringFormatting.decimalFormat(
						1.0 * best.getLeft(), 3);
				if (output)
					System.out.println("depth " + depth + " move "
							+ best.getRight() + " eval " + eval + " time "
							+ currentDepthElapsedTime + " ms");
			} catch (SearchOutOfTimeException e) {
				if (output)
					System.out.println("Out of time, depth finished "
							+ (depth - 1));
				break;
			}
		}
		elapsedTimeInMs = System.currentTimeMillis() - startTimeInMs;
		return best;
	}

	private void reset() {
		hashTable = new HashMap<>();
		numNodesExpanded = numNodesPruned = 0;
		startTimeInMs = System.currentTimeMillis();

	}

	private Pair<Integer, Move> recursiveNegamax(State state,
			int remainingDepth, double alpha, double beta) {
		numNodesExpanded++;

		if (useHashTable && hashTable.containsKey(state)) {
			return hashTable.get(state);
		}

		// Periodically check if we are out of time and terminate
		if (numNodesExpanded % 10000 == 0
				&& System.currentTimeMillis() - startTimeInMs > allowedTimeInMs) {
			throw new SearchOutOfTimeException();
		}

		// Whether we find the exact value of a position or an alpha/beta bound
		boolean exactBound = true;
		Player activePlayer = rules.getActivePlayer(state);
		int scoreMultiplier = activePlayer == Player.MAX ? 1 : -1;

		if (rules.isTerminal(state) || remainingDepth <= 0) {
			// TODO Constant for NO_MOVE
			int terminalValue = scoreMultiplier
					* evaluationFunction.evaluate(state);
			// hashTable.put(state, Pair.of(terminalValue, (Move) null));
			return Pair.of(terminalValue, null);
		}
		State clonedState = state.copy();
		// Instead of the typical negation of the opponent's score the score
		// multiplier is used to support games where
		// the players don't always alternate turns
		Move bestMove = null;
		int bestScore = Integer.MIN_VALUE;
		for (Move move : moveGenerator.getLegalMoves(state)) {
			State newState = rules.applyMove(state, move);
			Pair<Integer, Move> result;
			int value;
			// TODO Non-generic, abstract
			// Reduce depth by half if the move is a capture
			int newRemainingDepth = remainingDepth - 10;
			/*
			 * boolean moreDepth = false; if (newState instanceof GoState &&
			 * move instanceof GoMove) { GoState gostate = (GoState) newState;
			 * GoMove gomove = (GoMove) move; long checked = 0; long lastmove =
			 * gostate.getBoard().getLastMove();
			 * if(gostate.getBoard().getTurn()) if
			 * (gostate.getBoard().countGroupLiberties(
			 * gostate.getBoard().findWhiteGroups()) <= 1) { moreDepth = true;
			 * 
			 * }
			 * 
			 * checked = 0; int[] coords =
			 * gostate.getBoard().highestBitToCoords(lastmove); long adjacent =
			 * gostate.getBoard().getBite(
			 * gostate.getBoard().getSquare(coords[0] + 1, coords[1])); if
			 * (gostate.getBoard().isWithinBounds(coords[0] + 1, coords[1])) {
			 * if (gostate.getBoard().countGroupLiberties(
			 * gostate.getBoard().findAgroup(adjacent, checked)) <= 1) {
			 * moreDepth = true; } } checked = 0; coords =
			 * gostate.getBoard().highestBitToCoords(lastmove); adjacent =
			 * gostate.getBoard().getBite(
			 * gostate.getBoard().getSquare(coords[0] - 1, coords[1])); if
			 * (gostate.getBoard().isWithinBounds(coords[0] - 1, coords[1])) {
			 * if (gostate.getBoard().countGroupLiberties(
			 * gostate.getBoard().findAgroup(adjacent, checked)) <= 1) {
			 * moreDepth = true; } } checked = 0; coords =
			 * gostate.getBoard().highestBitToCoords(lastmove); adjacent =
			 * gostate.getBoard().getBite(
			 * gostate.getBoard().getSquare(coords[0], coords[1] + 1)); if
			 * (gostate.getBoard().isWithinBounds(coords[0], coords[1] + 1)) {
			 * if (gostate.getBoard().countGroupLiberties(
			 * gostate.getBoard().findAgroup(adjacent, checked)) <= 1) {
			 * moreDepth = true; } } checked = 0; coords =
			 * gostate.getBoard().highestBitToCoords(lastmove); adjacent =
			 * gostate.getBoard().getBite(
			 * gostate.getBoard().getSquare(coords[0], coords[1] - 1)); if
			 * (gostate.getBoard().isWithinBounds(coords[0], coords[1] - 1)) {
			 * if (gostate.getBoard().countGroupLiberties(
			 * gostate.getBoard().findAgroup(adjacent, checked)) <= 1) {
			 * moreDepth = true; } }
			 * 
			 * 
			 * if (moreDepth) newRemainingDepth += 3;
			 */
			if (rules.getActivePlayer(newState) != activePlayer) {
				// Alternating moves
				result = recursiveNegamax(newState, newRemainingDepth, -beta,
						-alpha);
				value = -result.getLeft();
				value *= 1;
			} else {
				result = recursiveNegamax(newState, newRemainingDepth, alpha,
						beta);
				value = result.getLeft();
			}
			alpha = Math.max(alpha, bestScore);

			if (value > bestScore) {
				bestScore = value;
				bestMove = move;
			}
			if (alpha >= beta) {
				numNodesPruned++;
				exactBound = false;
				break;
			}
			// Reset the state since we've applied a move
			state = clonedState;
		}
		Pair<Integer, Move> best = Pair.of(bestScore, bestMove);
		if (useHashTable && exactBound && remainingDepth > 1)
			hashTable.put(clonedState, best);
		return best;
	}

	/** Returns the nodes per second count for the last move calculation */
	public double getMilliNodesPerSeconds() {
		return elapsedTimeInMs != 0 ? numNodesExpanded
				/ (1000.0 * elapsedTimeInMs) : 0;
	}

}
