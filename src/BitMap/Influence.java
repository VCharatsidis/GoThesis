package BitMap;

import java.util.Arrays;

public class Influence {

	public Influence(BitBoard board) {
		this.board = board;
		this.width = board.getWidth();
		this.height = board.getHeight();
		modified = new boolean[width * height];
		Arrays.fill(modified, false);
		blackInf = new int[width * height];
		whiteInf = new int[width * height];
		influence = new int[width * height];
		Arrays.fill(influence, 0);
		Arrays.fill(blackInf, 0);
		Arrays.fill(whiteInf, 0);
		bonus = new boolean[width * height];
		Arrays.fill(bonus, true);

	}

	private int[] blackInf;
	private int[] whiteInf;
	private boolean[] bonus;
	private Compass comp;
	private BitBoard board;
	private int[] influence;
	private int everyInfluence;

	private int width;
	private int height;
	private boolean[] modified;

	public int getAllInfluence() {

		return everyInfluence;
	}

	public BitBoard getBoard() {
		return board;
	}

	public int[] getInfluence() {

		return influence;
	}

	public int checkIfFull(int row, int col, int inf, int full) {
		if (inf > full)
			return inf - full;
		else
			return 3000;
	}

	public int absInf(int row, int col) {
		int inf = (int) (Math.abs(influence[row * width + col]));
		return inf;
	}

	public void propagateInf(int row, int col) {

		if (modified[row * width + col])
			return;

		int full = 20;
		int inf = absInf(row, col);
		int adjacent = 0;
		int diagonal = 0;


		if (checkIfFull(row, col, inf, full) == 3000) {
			return;
		} else {
			adjacent = (int) (Math.round(checkIfFull(row, col, inf, full) / 2));
			diagonal = (int) (Math.round(adjacent * 0.75));

		}

		if (influence[row * width + col] < 0) {
			adjacent = -adjacent;
			diagonal = -diagonal;
		}

		transformPropagate(row - 1, col - 1, diagonal, row, col);
		transformPropagate(row - 1, col, adjacent, row, col);
		transformPropagate(row - 1, col + 1, diagonal, row, col);

		transformPropagate(row, col - 1, adjacent, row, col);
		transformPropagate(row, col + 1, adjacent, row, col);

		transformPropagate(row + 1, col - 1, diagonal, row, col);
		transformPropagate(row + 1, col, adjacent, row, col);
		transformPropagate(row + 1, col + 1, diagonal, row, col);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				influence[i * width + j] += blackInf[i * width + j]
						+ whiteInf[i * width + j];
			}
		}

	}

	public void modify(int row, int col) {
		modified[row * width + col] = !modified[row * width + col];
	}

	public void transformPropagate(int row, int col, int increment,
			int rootRow, int rootCol) {


		if (!board.isWithinBounds(row, col)) {
			// System.out.println("out of bounds");
			return;
		}
		if (influence[row * width + col] > 0
				&& influence[rootRow * width + rootCol] > 0) {
			if (influence[row * width + col] > influence[rootRow * width
					+ rootCol])
				return;
		}
		if (influence[row * width + col] < 0
				&& influence[rootRow * width + rootCol] < 0)
			if (influence[row * width + col] < influence[rootRow * width
					+ rootCol])
				return;

		if (occupied(row, col) != 1) {
			// System.out.println("occupied");
			influence[row * width + col] = 0;
			return;
		}
		if (influence[row * width + col] < 0)
			whiteInf[row * width + col] = whiteInf[row * width + col]
					+ increment;
		else
			blackInf[row * width + col] = blackInf[row * width + col]
					+ increment;

		// System.out.println("for row =  " + row + " for col = " + col
		// + " the influences is " + influence[row * width + col]);
	}

	public void transform(int row, int col, int increment) {

		int inf = influence[row * width + col];
		if (occupied(row, col) != 1) {
			influence[row * width + col] = 0;

		} else {

			influence[row * width + col] = inf + increment;

			/*
			 * check if someone territory if ((influence[row * width + col] =
			 * influence[row * width + col] ) > 60)
			 * 
			 * else if ((influence[row * width + col] = influence[row * width +
			 * col] + increment) < -60)
			 */
		}
	}

	public boolean isBlack(int row, int col) {
		long stone = getBite(getSquare(row, col));
		if ((stone & board.getBlackpieces()) == stone)
			return true;
		return false;
	}

	public boolean isWhite(int row, int col) {
		long stone = getBite(getSquare(row, col));
		if ((stone & board.getWhitepieces()) == stone)
			return true;
		return false;
	}

	public void mask(int row, int col) {
		int bonus = 0;
		/*
		 * if (occupied(row, col) == 2) if (board.getTurn()) bonus = 10; if
		 * (occupied(row, col) == 3) if (!board.getTurn()) bonus = 10;
		 */

		int compass = 0;
		if (board.getMoveNumber() < 8)
			compass = comp.getComp().getMask()[row * width + col];
		int adjacent = 10 + compass + bonus;
		int diagonal = 7 + compass + bonus;
		// int lastmoveRow = highestBitToCoords(board.getLastMove())[0];
		// int lastmoveCol = highestBitToCoords(board.getLastMove())[1];

		if (isBlack(row, col)) {

			influence[row * width + col] = 0;

		} else if (isWhite(row, col)) {

			influence[row * width + col] = 0;
			adjacent = -adjacent;
			diagonal = -diagonal;
		}

		if (row > 0) {

			transform(row - 1, col, adjacent);
			if (col > 0) {
				transform(row, col - 1, adjacent);
				transform(row - 1, col - 1, diagonal);
			}
		}
		if (row > 0) {
			if (col < width - 1) {
				transform(row, col + 1, adjacent);
				transform(row - 1, col + 1, diagonal);
			}
		}
		if (row < height - 1) {
			transform(row + 1, col, adjacent);
			if (col > 0) {
				if (row == 0) {
					transform(row, col - 1, adjacent);
				}
				transform(row + 1, col - 1, diagonal);
			}
		}
		if (row < height - 1) {
			if (col < width - 1) {
				if (row == 0) {
					transform(row, col + 1, adjacent);
				}
				transform(row + 1, col + 1, diagonal);
			}
		}
	}

	public void everyInfluence() {

		makeInfluence();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				everyInfluence += influence[i * width + j];
			}
		}
		everyInfluence += ((board.getWhiteCaptives() - board.getBlackCaptives()) * 600);

	}

	public void printer() {
		long stone;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				stone = board.getBite(getSquare(i, j));

				if ((stone & board.getBlackpieces()) == stone)
					System.out.print("B  ");
				else if ((stone & board.getWhitepieces()) == stone)
					System.out.print("W  ");
				else
					System.out.print("-  ");

			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				stone = board.getBite(getSquare(i, j));

				if ((stone & board.getBlackpieces()) == stone)
					System.out.print("B  ");
				else if ((stone & board.getWhitepieces()) == stone)
					System.out.print("W  ");
				else {
					System.out.print(influence[i * width + j] + " ");
					if (influence[i * width + j] < 10)
						System.out.print(" ");
				}
			}
			System.out.println();

		}
		System.out.println();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {

				if (modified[row * width + col] == true)
					System.out.print("----- ");
				else
					System.out.print("False ");

			}
			System.out.println();

		}
		// System.out.println(board.getWhiteCaptives());
		System.out.println("all inf " + getAllInfluence());

	}

	public int occupied(int row, int col) {

		long intersection = getBite(getSquare(row, col));
		if ((intersection | emptySquares()) == emptySquares()) {
			return 1;
		} else if ((intersection | board.getBlackpieces()) == board
				.getBlackpieces()) {
			return 2;
		}
		return 3;
	}

	public int[] highestBitToCoords(long highestBit) {
		int square = longToSquare(highestBit);
		int[] coords = squareToRowCol(square);
		return coords;
	}

	public int getSquare(int row, int col) {
		return row * width + col;
	}

	public long emptySquares() {
		return ~board.getWhitepieces() & ~board.getBlackpieces();
	}

	public long getBite(int square) {
		return 1L << square;
	}

	public int[] squareToRowCol(int square) {
		int[] rowcol = { 0, 0 };
		rowcol[0] = (int) Math.floor(square / height);
		rowcol[1] = square % width;
		return rowcol;
	}

	public int longToSquare(long l) {
		return Long.numberOfTrailingZeros(l);
	}

	/** updates the int array influence when a stone is played */
	public void makeInfluence() {

		everyInfluence = 0;

		Arrays.fill(influence, 0);
		for (int row = 0; row < board.getHeight(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {
				if (board.occupied(row, col) == 1) {
					continue;
				}
				mask(row, col);
			}
		}
		for (int row = 0; row < board.getHeight(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {
				if (influence[row * width + col] == 0)
					modify(row, col);
			}
		}
		// printer();
		// System.out.println();
		Arrays.fill(blackInf, 0);
		Arrays.fill(whiteInf, 0);
		for (int row = 0; row < board.getHeight(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {
				if (board.occupied(row, col) != 1) {
					continue;
				}
				propagateInf(row, col);
			}
		}
		for (int row = 0; row < board.getHeight(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {
				modified[row * width + col] = !modified[row * width + col];

			}
		}
		for (int row = 0; row < board.getHeight(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {
				if (influence[row * width + col] == 0)
					modify(row, col);
			}
		}
		Arrays.fill(blackInf, 0);
		Arrays.fill(whiteInf, 0);
		for (int row = 0; row < board.getHeight(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {

				if (board.occupied(row, col) != 1) {
					continue;
				}
				propagateInf(row, col);
			}
		}
	}
}