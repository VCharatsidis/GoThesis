package BitMap;

	
	
public class BitBoard {
	
	public BitBoard(int height ,int width){
	this.height = height;
	this.width = width;
	blackToplay=true;
	moveNumber =0;
	size = height*width;
	neighbors = new long[height*width];
	for(int i=0;i<height;i++){
		for(int j=0;j<width;j++){
			neighbors[i*width+j]=mask(i,j);
		}
		
	}
	}
	public boolean getTurn(){
		return blackToplay;
	}
	private int moveNumber;
	private long checked;
	private boolean blackToplay;
	private int height;
	private int width;
	private int size;
	private long blackTwoMovesAgo=-1L;
	private long whiteTwoMovesAgo=-1L;
	
	
	private long whitepieces=0;
	private long blackpieces=0;
	private long[] neighbors;
	
	public long board(){
		 return whitepieces | blackpieces;
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
		System.out.println("i check for white chains liberties " + row + " " + col);
		if (liberties(row,col))
			return true;
		checked|=stone;
		int [] coords;
		long highestBit;
		System.out.println("checked inside checkForWhiteLibs= "+Long.toBinaryString(checked));
		long whiteNeighbors = (neighbors[row*width+col] & whitepieces);
		if(whiteNeighbors==0){
			
			return false;
		}
		whiteNeighbors = whiteNeighbors & ~checked;
		System.out.println(Long.toBinaryString(whiteNeighbors));
		long testneighbors=whiteNeighbors;
		while (testneighbors !=0){
			long testBit = Long.highestOneBit(testneighbors);
			coords = highestBitToCoords(testBit);
			testneighbors= testneighbors & ~ testBit;
			System.out.println("not checked neighbors of stone= "+ coords[0]+ " "+coords[1]);
		}
		while (whiteNeighbors !=0){
				
				//getting the first neighbor
				highestBit = Long.highestOneBit(whiteNeighbors);
				coords = highestBitToCoords(highestBit);
				whiteNeighbors= whiteNeighbors & ~highestBit;
				if(liberties(coords[0],coords[1])){
					return true;
				}
				if(	checkForWhiteChainLibs(coords[0],coords[1],highestBit)) {
					return true;
				}
							
		}
		checked |=stone;
		return false;
	}
	public void checkForOpponentsLibs(int row,int col,long stone){
		System.out.println("i check for OPPONENTS LIBS");
		long highestBit;
		long whiteneighbors = (neighbors[row*width+col] & whitepieces);
		long blackneighbors = (neighbors[row*width+col] & blackpieces);
		int [] coords;
		if (blackToplay){
			for (int i=0;i<4;i++){
				highestBit = Long.highestOneBit(whiteneighbors);
				if (whiteneighbors==0)
					continue;
				coords = highestBitToCoords(highestBit);
				System.out.println("highestBit= "+Long.toBinaryString(highestBit));
				whiteneighbors ^= highestBit;
				System.out.println("whiteneighbors= "+Long.toBinaryString(whiteneighbors));
				checked=0;
				if (!checkForWhiteChainLibs(coords[0],coords[1],highestBit)){
					System.out.println("BEFORE REMOVING white checked stones");
					removeStones(checked);
				}
				
			}
		}
		else {
			for (int i=0;i<4;i++){
				highestBit = Long.highestOneBit(blackneighbors);
				if ( blackneighbors==0){
					continue;
				}
				coords = highestBitToCoords(highestBit);
				blackneighbors ^= highestBit;
				checked=0;
				if (!checkForBlackChainLibs(coords[0],coords[1],highestBit)){
					System.out.println("before removing black checked stones");
					removeStones(checked);
				}
			}
		}
		
	}
	
	
	boolean checkForBlackChainLibs(int row,int col,long stone){
		System.out.println("i check for black chains liberties " + row +" "+ col) ;
		if (liberties(row,col))
			return true;
		// the two lines that follow gives black and white neighbs of that move.
		long blackNeighbors = (neighbors[row*width+col] & blackpieces);
		System.out.println("blackNeighbors= "+Long.toBinaryString(blackNeighbors));
		System.out.println("checked= "+Long.toBinaryString(checked));
		int [] coords;
		long highestBit;
		// we count liberties of black and white neighbors of that square
		
			//we isolate white neighbors .
			if (blackNeighbors==0){
				checked |=stone;
				return false;
			}
			while ((blackNeighbors | checked) != checked){
				highestBit = Long.highestOneBit(blackNeighbors);
				coords = highestBitToCoords(highestBit);
				
				if(liberties(coords[0],coords[1])){
					return true;
				}
				checked |= highestBit;
				checkForBlackChainLibs(coords[0],coords[1],highestBit);
			}
		
			return false;	
	}
	
	public int[] highestBitToCoords(long highestBit){
		int square =longToSquare(highestBit);
		int[] coords = squareToRowCol(square);
		return coords;
	}

	public boolean isLegal(int row ,int col){
		checked = 0;
		checkForOpponentsLibs(row,col,getBite(getSquare(row,col)));
		System.out.println("check if legal");
		
		if (liberties(row,col)){
			return true;
		}
			
		//checking for ko
		if(blackToplay){
			if(blackTwoMovesAgo==board()){
				System.out.println("not legal ko for black");
				return false;
			}
			return checkForBlackChainLibs(row,col,getBite(getSquare(row,col)));
		}
		else
			if ((whiteTwoMovesAgo==board())){
				System.out.println("not legal ko for white");
				return false;
			}
			return checkForWhiteChainLibs(row,col,getBite(getSquare(row,col)));

	}
	
	public void removeStones(long checked){
		System.out.println("remove stones");
		if (blackToplay){
			System.out.println("whitepiece before removal= "+Long.toBinaryString(whitepieces));
			System.out.println("checked= "+Long.toBinaryString(this.checked));
			whitepieces ^= this.checked;
			this.checked=0;
			System.out.println("whitepieces after removal= "+Long.toBinaryString(whitepieces));
		}
		else{
			blackpieces ^= this.checked;
			this.checked=0;
		}
	}
	
	public void countLiberties(int row ,int col){
		System.out.println("liberties of the played stone are=  "+ Long.toBinaryString(neighbors[row*width+col] & emptySquares()));
		int [] coords = highestBitToCoords(neighbors[row*width+col] & emptySquares());
		System.out.println(coords[0]+""+coords[1]);
	}
	
	public void addStone(int row, int col){
		if (occupied(row,col)!=1){
			System.out.println("occupied");
			return ;	
		}
		long whitepiecesB = whitepieces;
		long blackpiecesB = blackpieces;
		long blackTwoMovesAgoB = blackTwoMovesAgo;
		long whiteTwoMovesAgoB = whiteTwoMovesAgo;
		
		System.out.println("Time to add stone");
		long newStone = getBite(getSquare(row,col)) ;
		if (blackToplay){
			blackTwoMovesAgo = board();
			blackpieces |= newStone;	
		}
		else {
			System.out.println("i save board for white");
			whiteTwoMovesAgo = board();
			whitepieces |= newStone ;
		}
		if (!isLegal(row,col)){
			System.out.println("ILEGAL MOVE");
			removeStones(newStone);
			blackTwoMovesAgo = blackTwoMovesAgoB;
			whiteTwoMovesAgo = whiteTwoMovesAgoB;
			whitepieces=whitepiecesB;
			blackpieces=blackpiecesB;
			return;
		}
		System.out.println("coords of new move are= "+row+""+col);
	
		board();
		countLiberties(row,col);
		System.out.println("blackpieces are= "+Long.toBinaryString(blackpieces));
		System.out.println("whitepieces are= "+Long.toBinaryString(whitepieces));
		System.out.println("board is= "+Long.toBinaryString(board()));
		blackToplay = !blackToplay;
		moveNumber+=1;
		System.out.println("MOVE "+moveNumber);	
		
	}
	
}

