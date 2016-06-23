package BitMap;

import java.util.Arrays;

public class Influence {

	public Influence(BitBoard board) {
		this.board = board;
		this.width = board.getWidth();
		this.height = board.getHeight();
		modified = new boolean[width * height];
		Arrays.fill(modified, false);

		influence = new int[width * height];
		Arrays.fill(influence, 0);
		bonus = new boolean[width * height];
		Arrays.fill(bonus, true);

	}

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



		int full = 20;
		int inf = absInf(row, col);
		int adjacent = 0;
		int diagonal = 0;
		int cap = 0;

		if (checkIfFull(row, col, inf, full) == 3000) {
			return;
		} else {
			adjacent = checkIfFull(row, col, inf, full);
			diagonal = (int) (Math.round(adjacent * 0.75));

		}

		if (influence[row * width + col] < 0) {
			adjacent = -adjacent;
			diagonal = -diagonal;
		}


		transformPropagate(row - 1, col - 1, diagonal);
		transformPropagate(row - 1, col, adjacent);
		transformPropagate(row - 1, col + 1, diagonal);

		transformPropagate(row, col - 1, adjacent);
		transformPropagate(row, col + 1, adjacent);

		transformPropagate(row + 1, col - 1, diagonal);
		transformPropagate(row + 1, col, adjacent);
		transformPropagate(row + 1, col + 1, diagonal);

	}

	public void modify(int row, int col) {
		modified[row * width + col] = true;
	}

	public void transformPropagate(int row, int col, int increment) {
		if (!board.isWithinBounds(row, col)) {
			// System.out.println("out of bounds");
			return;
		}
		if (modified[row * width + col] == true) {
			// System.out.println("modified");
			return;
		}
		if (occupied(row, col) != 1) {
			// System.out.println("occupied");
			influence[row * width + col] = 0;
			return;
		}

		influence[row * width + col] = influence[row * width + col] + increment;
		// System.out.println("for row =  " + row + " for col = " + col
		// + " the influences is " + influence[row * width + col]);

	}

	public void transform(int row, int col, int increment) {

		int inf = influence[row * width + col];
		if (occupied(row, col) != 1) {
			influence[row * width + col] = 0;

		} else {

			influence[row * width + col] = inf + increment;
			modify(row, col);
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

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				long stone = board.getBite(getSquare(i, j));

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
		// System.out.println(board.getWhiteCaptives());
		// System.out.println(getAllInfluence());

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
		System.out.println("makle");
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
		// printer();
		// System.out.println();
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
				if (influence[row * width + col] != 0)
					modified[row * width + col] = true;
			}
		}
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