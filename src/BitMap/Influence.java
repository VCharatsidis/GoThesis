package BitMap;

import java.util.Arrays;

public class Influence {

	public Influence(BitBoard board){
		this.board = board;
		this.width = board.getWidth();
		this.height = board.getHeight();
		
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

	private int width ;
	private int height ;
	
	public int getAllInfluence() {
		everyInfluence();
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

		int full = 40;
		int inf = absInf(row, col);
		int extra = 0;
		if (checkIfFull(row, col, inf, full) == 3000) {
			return;
		} else {
			extra = checkIfFull(row, col, inf, full);
		}
		int diagonal = (int) (Math.round(extra * 0.75));
		if (influence[row * width + col] < 0) {
			extra = -extra;
			diagonal = -diagonal;
		}
		if (row > 0) {

			transformPropagate(row - 1, col, extra);
			if (col > 0) {

				transformPropagate(row, col - 1, extra);
				transformPropagate(row - 1, col - 1, diagonal);
			}

		}
		if (row > 0) {
			if (col < width - 1) {
				transformPropagate(row, col + 1, extra);
				transformPropagate(row - 1, col + 1, diagonal);
			}
		}
		if (row < height - 1) {
			transformPropagate(row + 1, col, extra);
			if (col > 0) {
				if (row == 0) {
					transformPropagate(row, col - 1, extra);
				}
				transformPropagate(row + 1, col - 1, diagonal);
			}
		}
		if (row < height - 1) {
			if (col < width - 1) {
				if (row == 0) {
					transformPropagate(row, col + 1, extra);
				}
				transformPropagate(row + 1, col + 1, diagonal);
			}
		}

	}

	public void transformPropagate(int row, int col, int increment) {
		if (absInf(row, col) < 40)
			return;
		if (occupied(row, col) != 1) {
			influence[row * width + col] = 0;
		} else {
			influence[row * width + col] = influence[row * width + col]
					+ increment;
			if ((influence[row * width + col] = influence[row * width + col]
					+ increment) > 60)
				influence[row * width + col] = 60;
			else if ((influence[row * width + col] = influence[row * width
					+ col]
					+ increment) < -60)
				influence[row * width + col] = -60;
		}
	}
	
	public void transform(int row, int col, int increment) {

		if (occupied(row, col) != 1) {
			influence[row * width + col] = 0;

		} else {

			influence[row * width + col] = influence[row * width + col]
					+ increment;
			if ((influence[row * width + col] = influence[row * width + col]
					+ increment) > 60)
				influence[row * width + col] = 60;
			else if ((influence[row * width + col] = influence[row * width
					+ col]
					+ increment) < -60)
				influence[row * width + col] = -60;

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
		if (occupied(row, col) == 2)
			if (board.getTurn())
				bonus = 10;
		if (occupied(row, col) == 3)
			if (!board.getTurn())
				bonus = 10;

		int compass = comp.getComp().getMask()[row * width + col];
		int adjacent = 5 + compass + bonus;
		int diagonal = 0 + compass + bonus;
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
	
	public void everyInfluence(){
		makeInfluence();
		for (int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				everyInfluence += influence[i * width + j];
			}
		}
		everyInfluence += ((board.getWhiteCaptives() - board.getBlackCaptives()) * 300);

	}

	/** updates the int array influence when a stone is played */
	public void makeInfluence(){

		everyInfluence = 0;
		influence = new int[width * height];
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
				if (board.occupied(row, col) != 1) {
					continue;
				}
				propagateInf(row, col);
			}
		}
	}
	
	public void printer(){

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(influence[i * width + j] + " ");
			}
			System.out.println();

		}
		// System.out.println(board.getWhiteCaptives());
		System.out.println(getAllInfluence());

	}
	
	public int occupied(int row,int col){
		
		long intersection = getBite(getSquare(row,col));
		if ((intersection | emptySquares()) == emptySquares()){
			return 1;
		}
		else if((intersection | board.getBlackpieces())==board.getBlackpieces()){
			return 2;
		}
		return 3;
	}
	public int[] highestBitToCoords(long highestBit){
		int square =longToSquare(highestBit);
		int[] coords = squareToRowCol(square);
		return coords;
	}
	public int getSquare(int row,int col){
		return row*width+col;
	}
	
	public long emptySquares(){
		return ~board.getWhitepieces() & ~board.getBlackpieces();
	}
	
	public long getBite(int square){
		return 1L<<square;
	}
	public int[] squareToRowCol(int square){
		int[] rowcol={0,0};
		rowcol[0] = (int)Math.floor(square/height);
		rowcol[1] = square % width;
		return rowcol;
	}
	public int longToSquare(long l){
		return Long.numberOfTrailingZeros(l);
	}
}