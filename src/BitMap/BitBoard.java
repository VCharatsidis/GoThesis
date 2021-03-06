package BitMap;

import java.util.ArrayList;

import lombok.Getter;

	
@Getter
public class BitBoard {

	private int moveNumber;
	private long checked;
	private boolean blackToplay;
	private int height;
	private int width;
	
	private long blackTwoMovesAgo = -1L;
	private long whiteTwoMovesAgo = -1L;
	private long lastMove;
	private long afterwhiteMove;
	private long afterblackMove;

	private long whitepieces;
	private long blackpieces;
	private int passCounter;
	private long[] neighbors;
	private int blackCaptives;
	private int whiteCaptives;
	
	private boolean Ai;

	public BitBoard(int height ,int width){
	this.height = height;
	this.width = width;
	blackToplay=true;
	moveNumber =0;
		whiteCaptives = 0;
		blackCaptives = 0;
		whitepieces = 0;
		blackpieces = 0;
	neighbors = new long[height*width];
	for(int i=0;i<height;i++){
		for(int j=0;j<width;j++){
			neighbors[i*width+j]=mask(i,j);
		}
		
	}
		AiMode();
	}

	public BitBoard(boolean blackToplay, long blackpieces, long whitepieces,
			long blackTwoMovesAgo, long whiteTwoMovesAgo, long lastMove,
			long[] neighbors, int height, int width, int passCounter,
			int blackcaptives, int whitecaptives, long afterwhiteMove,
			long afterblackMove, int moveNumber) {
		this.blackToplay = blackToplay;
		this.blackpieces = blackpieces;
		this.whitepieces = whitepieces;
		this.blackTwoMovesAgo = blackTwoMovesAgo;
		this.whiteTwoMovesAgo = whiteTwoMovesAgo;
		this.lastMove = lastMove;
		this.neighbors = neighbors;
		this.height = height;
		this.width = width;
		this.passCounter = passCounter;
		this.blackCaptives = blackcaptives;
		this.whiteCaptives = whitecaptives;
		this.moveNumber = moveNumber;
	}

	public BitBoard(BitBoard other) {
		this.blackToplay = other.blackToplay;
		this.blackpieces = other.blackpieces;
		this.whitepieces = other.whitepieces;
		this.blackTwoMovesAgo = other.blackTwoMovesAgo;
		this.whiteTwoMovesAgo = other.whiteTwoMovesAgo;
		this.lastMove = other.lastMove;
		this.neighbors = other.neighbors;
		this.height = other.height;
		this.width = other.width;
		this.passCounter = other.passCounter;
		this.blackCaptives = other.blackCaptives;
		this.whiteCaptives = other.whiteCaptives;
		this.afterwhiteMove = other.afterwhiteMove;
		this.afterblackMove = other.afterblackMove;
		this.moveNumber = other.moveNumber;
	}

	public boolean isGameOver() {
		return passCounter == 2;
	}

	public boolean getTurn(){
		return blackToplay;
	}

	public long getLastMove(){
		return lastMove;
	}
	public long[] getNeighbors(){
		return neighbors;
	}
	public long getBlackpieces(){
		return blackpieces;
	}
	public long getWhitepieces(){
		return whitepieces;
	}
	public long board(){
		 return whitepieces | blackpieces;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public int[] squareToRowCol(int square){
		int[] rowcol={0,0};
		rowcol[0] = (int)Math.floor(square/height);
		rowcol[1] = square % width;
		return rowcol;
	}
	
	public int getSquare(int row,int col){
		return row*width+col;
	}
	
	public long emptySquares(){
		return ~whitepieces & ~blackpieces;
	}
	
	public long getBite(int square){
		return 1L<<square;
	}
	public boolean liberties(int row , int col ){
		return (neighbors[row*width+col] & emptySquares()) !=0;
	}
	
	public long mask(int row ,int col){
		long north=0,south=0,east=0,west=0;
		if (row > 0) {
			south = getBite(getSquare(row-1,col));
	    }
	    if (row < height - 1) {
	    	north = getBite(getSquare(row+1,col));
	    }
	    if (col > 0) {
	    	west = getBite(getSquare(row,col-1));
	    }
	    if (col < width - 1) {
	    	east = getBite(getSquare(row,col+1));
	    }
	    return north |= south |= west |= east;
	}


	public boolean isWithinBounds(int row, int col) {
		if ((row >= 0 && row <= height - 1) && (col >= 0 && col <= width - 1)) {
			return true;
		}
		return false;
	}
	public int occupied(int row,int col){
		long intersection = getBite(getSquare(row,col));
		if ((intersection | emptySquares() )== emptySquares()){
			
			return 1;
		}
		else if((intersection | blackpieces)==blackpieces){
			
			return 2;
		}
		return 3;
	}
	
	public int longToSquare(long l){
		return Long.numberOfTrailingZeros(l);
	}
	
	boolean checkForWhiteChainLibs(int row , int col ,long stone){

		if (liberties(row,col))
			return true;
		checked|=stone;
		int [] coords;
		long highestBit;

		long whiteNeighbors = (neighbors[row*width+col] & whitepieces);
		whiteNeighbors= whiteNeighbors &~ checked;
		if(whiteNeighbors==0){
			return false;
		}
		

		while (whiteNeighbors !=0){
				
				//getting the first neighbor
				highestBit = Long.highestOneBit(whiteNeighbors);
				coords = highestBitToCoords(highestBit);
				whiteNeighbors= whiteNeighbors &~ highestBit;
				if(liberties(coords[0],coords[1])){
					return true;
				}
				if(	checkForWhiteChainLibs(coords[0],coords[1],highestBit)) {
					return true;
				}
							
		}
		
		return false;
	}
	public void checkForOpponentsLibs(int row,int col,long stone){

		long highestBit;
		long whiteneighbors = (neighbors[row*width+col] & whitepieces);
		long blackneighbors = (neighbors[row*width+col] & blackpieces);
		int [] coords;
		if (blackToplay){
			int neighbs = Long.bitCount(whiteneighbors);
			for (int i=0;i<=neighbs;i++){
				highestBit = Long.highestOneBit(whiteneighbors);
				if (whiteneighbors==0)
					continue;
				coords = highestBitToCoords(highestBit);

				whiteneighbors = whiteneighbors & ~highestBit;

				checked=0;
				if (!checkForWhiteChainLibs(coords[0],coords[1],highestBit)){

					removeStones(checked);
				}
				
			}
		}
		else {
			int neighbs = Long.bitCount(blackneighbors);
			for (int i=0;i<=neighbs;i++){
				highestBit = Long.highestOneBit(blackneighbors);
				if ( blackneighbors==0){
					continue;
				}
				coords = highestBitToCoords(highestBit);
				blackneighbors ^= highestBit;
				checked=0;
				if (!checkForBlackChainLibs(coords[0],coords[1],highestBit)){

					removeStones(checked);
				}
			}
		}
		
	}
	
	
	boolean checkForBlackChainLibs(int row,int col,long stone){

		if (liberties(row,col))
			return true;
		checked|=stone;
		// the two lines that follow gives black and white neighbs of that move.
		long blackNeighbors = (neighbors[row*width+col] & blackpieces);
		blackNeighbors = blackNeighbors &~checked;

		int [] coords;
		long highestBit;
		

		// we count liberties of black and white neighbors of that square
		
			//we isolate white neighbors .
		if (blackNeighbors==0){
			return false;
		}
		while (blackNeighbors !=0 ){
			highestBit = Long.highestOneBit(blackNeighbors);
			coords = highestBitToCoords(highestBit);
			blackNeighbors = blackNeighbors & ~highestBit;
			if(liberties(coords[0],coords[1])){
				return true;
			}
				
			if(	checkForBlackChainLibs(coords[0],coords[1],highestBit)) {
				return true;
			}
		}
		return false;	
	}
	
	public int[] highestBitToCoords(long highestBit){
		int square =longToSquare(highestBit);
		int[] coords = squareToRowCol(square);
		return coords;
	}

	public long cutNeighbs(long highestBit) {

		long northWest = 0, northEast = 0, southWest = 0, southEast = 0;

				northWest = highestBit << 8;

				southWest = highestBit >> 6;

				northEast = highestBit << 6;

				southEast = highestBit >> 8;

		return northWest |= southWest |= northEast |= southEast;
	}

	public boolean diagonalNeighbour(int row, int col) {
		long diagonal = cutNeighbs(getBite(getSquare(row, col)));
		if (blackToplay)
			if ((diagonal & blackpieces) == 0)
				return false;
			else
				return true;
		else if ((diagonal & whitepieces) == 0)
			return false;
		else
			return true;
	}

	public boolean neibWithLastMove(int row, int col) {
		long diagonal = cutNeighbs(getBite(getSquare(row, col)));
		long adjacent = neighbors[row * width + col];
		if (((diagonal & lastMove) == 0) && ((adjacent & lastMove) == 0))
			return false;

		return true;
	}

	public boolean adjacentNeighbour(int row, int col) {
		long adjacent = neighbors[row * width + col];
		if (blackToplay)
			if ((adjacent & blackpieces) == 0)
				return false;
			else
				return true;
		else if ((adjacent & whitepieces) == 0)
			return false;
		else
			return true;
	}

	public ArrayList<Long> findWhiteGroups() {
		long allchecked = 0;
		long board = board();
		ArrayList<Long> groups = new ArrayList<Long>();
		int groupsindex = 0;
		while (Long.bitCount(board & ~allchecked) != 0) {
			long highestBit = Long.highestOneBit(board);
			board ^= highestBit;
			if ((highestBit | whitepieces) == whitepieces) {

				allchecked = allchecked | highestBit;
				long neighborsChecked = 0;
				groups.add(groupsindex,
						findAgroup(highestBit, neighborsChecked));
				allchecked = allchecked
						| findAgroup(highestBit, neighborsChecked);
				groupsindex++;
			} else {
				continue;
			}

		}
		return groups;
	}

	public ArrayList<Long> findBlackGroups() {
		long allchecked = 0;
		long board = board();
		ArrayList<Long> groups = new ArrayList<Long>();
		int groupsindex = 0;
		while (Long.bitCount(board & ~allchecked) != 0) {
			long highestBit = Long.highestOneBit(board);
			board ^= highestBit;
			if ((highestBit | blackpieces) == blackpieces) {

				allchecked = allchecked | highestBit;
				long neighborsChecked = 0;
				groups.add(groupsindex,
						findAgroup(highestBit, neighborsChecked));
				allchecked = allchecked
						| findAgroup(highestBit, neighborsChecked);
				groupsindex++;
			} else {
				continue;
			}

		}
		return groups;
	}

	public long findAgroup(long highestBit, long neighborsChecked) {

		int[] coords;
		coords = highestBitToCoords(highestBit);

		neighborsChecked |= highestBit;

		long blackneighbors;
		long whiteneighbors;

		if ((highestBit | blackpieces) == blackpieces) {

			blackneighbors = neighbors[coords[0] * width + coords[1]]
					& blackpieces;

			blackneighbors = blackneighbors & ~neighborsChecked;

			while (blackneighbors != 0) {
				highestBit = Long.highestOneBit(blackneighbors);
				neighborsChecked |= highestBit;
				coords = highestBitToCoords(highestBit);
				blackneighbors = blackneighbors & ~highestBit;

				neighborsChecked |= findAgroup(highestBit, neighborsChecked);
			}

		} else {
			whiteneighbors = neighbors[coords[0] * width + coords[1]]
					& whitepieces;
			whiteneighbors = whiteneighbors & ~neighborsChecked;

			while (whiteneighbors != 0) {
				highestBit = Long.highestOneBit(whiteneighbors);
				neighborsChecked |= highestBit;
				coords = highestBitToCoords(highestBit);
				whiteneighbors = whiteneighbors & ~highestBit;

				neighborsChecked |= findAgroup(highestBit, neighborsChecked);
			}

		}
		return neighborsChecked;

	}

	public void AiMode() {
		Ai = true;
	}
	public boolean isLegal(int row ,int col){
		if (!isWithinBounds(row, col)) {
			// System.out.println("out of bounds");
			return false;
		}
		if (occupied(row, col) != 1) {
			// System.out.println("occupied");
			return false;

		}
		/*
		 * if (moveNumber < 5) { //if (cutNeighbs(getBite(getSquare(row,col))))
		 * if (row == 0 || col == 0 || row == 6 || col == 6) { return false; } }
		 * if (moveNumber < 3) { if (row == 1 || col == 1 || row == 5 || col ==
		 * 5) { return false; } }
		 */
		// no legal move if the move doest have neightbours
		long diagonal = cutNeighbs(getBite(getSquare(row, col)));
		long adjacent = neighbors[row * width + col];

		if (moveNumber > 1 && moveNumber < 25)
				if (!(diagonalNeighbour(row, col) | adjacentNeighbour(row, col)))
					// if (!neibWithLastMove(row, col))
					return false;

			
		int whiteCaptivesB = whiteCaptives;
		int blackCaptivesB = blackCaptives;
		long whitepiecesB = whitepieces;
		long blackpiecesB = blackpieces;
		long blackTwoMovesAgoB = blackTwoMovesAgo;
		long whiteTwoMovesAgoB = whiteTwoMovesAgo;

		long newStone = getBite(getSquare(row, col));
		if (blackToplay) {

			blackpieces |= newStone;
		} else {
			// System.out.println("i save board for white");

			whitepieces |= newStone;
		}

		checked = 0;
		boolean notLegal = true;
		boolean legalKo = true;
		checkForOpponentsLibs(row, col, getBite(getSquare(row, col)));
		// System.out.println("check if legal");
		
		// System.out.println("board is= "+Long.toBinaryString(board()));
		// System.out.println("board WAS= "+Long.toBinaryString(blackTwoMovesAgo));
		// System.out.println("whitetwomovesago= "+Long.toBinaryString(whiteTwoMovesAgo));
		//checking for ko
		if(blackToplay){
			if(whiteTwoMovesAgo==board()){
				// System.out.println("not legal ko for black");
				legalKo = false;
			}
			if (liberties(row,col)){
				notLegal = true;
			}
			else {
				notLegal = checkForBlackChainLibs(row, col,getBite(getSquare(row, col)));
			}
		}
		else{
			if ((blackTwoMovesAgo==board())){

				legalKo = false;
			}
			if (liberties(row,col)){
				notLegal = true;
			}
			else {
				notLegal = checkForWhiteChainLibs(row, col,getBite(getSquare(row, col)));
			}
		}

		blackTwoMovesAgo = blackTwoMovesAgoB;
		whiteTwoMovesAgo = whiteTwoMovesAgoB;
		whitepieces = whitepiecesB;
		blackpieces = blackpiecesB;
		blackCaptives = blackCaptivesB;
		whiteCaptives = whiteCaptivesB;
		board();

		return notLegal & legalKo;

	}
	
	public void removeStones(long checked){

		if (blackToplay){

			whiteCaptives += Long.bitCount(this.checked);
			whitepieces ^= this.checked;
			this.checked=0;

		}
		else{

			blackCaptives += Long.bitCount(this.checked);
			blackpieces ^= this.checked;
			this.checked=0;
		}
	}

	public int countGroupLiberties(long group) {
		long checked = 0;
		int liberties = 0;
		while (group != 0) {

			long highestBit = Long.highestOneBit(group);
			group ^= highestBit;
			int[] coords = highestBitToCoords(highestBit);
			int row = coords[0];
			int col = coords[1];
			liberties += countLiberties(row, col);
		}

		return liberties;
	}
	
	public int countLiberties(int row, int col) {

		int libs = Long.bitCount(neighbors[row * width + col] & emptySquares());
		return libs;

	}
	
	public void pass() {
		blackToplay = !blackToplay;
		passCounter++;
	}

	public boolean koCheck() {
		int coords[] = highestBitToCoords(lastMove);
		int row = coords[0];
		int col = coords[1];
		long copyblack = blackpieces;
		long copywhite = whitepieces;
		if (countLiberties(row, col) > 1)
			return false;
		long emptyneighb ;


		if (!blackToplay) {
			emptyneighb = neighbors[row * width + col] & ~whitepieces;
			copyblack ^= lastMove;

			if (((emptyneighb | whitepieces) | copyblack) == afterwhiteMove)
				return true;
		} else {
			emptyneighb = neighbors[row * width + col] & ~blackpieces;
			copywhite ^= lastMove;

			if (((emptyneighb | blackpieces) | copywhite) == afterblackMove)
				return true;
		}

		return false;
	}
	public void addStone(int row, int col){

		if (!isLegal(row, col)) {
			// System.out.println("ILEGAL MOVE");
			return;
		}
		
		// System.out.println("Time to add stone");
		long newStone = getBite(getSquare(row,col)) ;
		if (blackToplay){
			blackTwoMovesAgo = board();
			blackpieces |= newStone;
			checkForWhiteChainLibs(row, col,getBite(getSquare(row, col)));

		}
		else {
			// System.out.println("i save board for white");
			whiteTwoMovesAgo = board();
			whitepieces |= newStone ;
			checkForBlackChainLibs(row, col,getBite(getSquare(row, col)));
		}
		checkForOpponentsLibs(row,col,getBite(getSquare(row,col)));
		
		// System.out.println("coords of new move are= "+row+""+col);
		
		lastMove = newStone;

		passCounter = 0;
		board();
		if (blackToplay)
			afterblackMove = board();

		else
			afterwhiteMove = board();
		countLiberties(row,col);

		blackToplay = !blackToplay;
		moveNumber+=1;
		// System.out.println("MOVE "+moveNumber);
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// result = prime * result + blackCaptives;
		result = prime * result + (blackToplay ? 1231 : 1237);
		result = prime * result + (int) (blackpieces ^ (blackpieces >>> 32));
		result = prime * result + height;
		result = prime * result + passCounter;
		// result = prime * result + whiteCaptives;
		result = prime * result + (int) (whitepieces ^ (whitepieces >>> 32));
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BitBoard other = (BitBoard) obj;

		if (blackToplay != other.blackToplay)
			return false;
		if (blackpieces != other.blackpieces)
			return false;
		if (height != other.height)
			return false;
		if (passCounter != other.passCounter)
			return false;

		if (whitepieces != other.whitepieces)
			return false;
		if (width != other.width)
			return false;
		if ((blackCaptives - whiteCaptives) != other.blackCaptives
				- other.whiteCaptives)
			return false;

		if (this.koCheck() || other.koCheck()
				&& this.lastMove != other.lastMove)
			return false;
		return true;
	}
	
}

