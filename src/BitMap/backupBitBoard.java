package BitMap;

	
	
public class BitBoard {
	
	public BitBoard(int height ,int width){
	this.height = height;
	this.width = width;
	blackToplay=true;
	size = height*width;
	for(int i=0;i<height;i++){
		for(int j=0;j<width;j++){
			neighbors[i+j]=mask(i,j);
		}
		
	}
	}
	private boolean blackToplay;
	private int height;
	private int width;
	private int size;
	
	
	private long whitepieces;
	private long blackpieces;
	private long[] neighbors;
	
	public long board(){
		
		 return whitepieces |= blackpieces;
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
		return (neighbors[row+col] & emptySquares())!=0;
	}
	
	public long mask(int row ,int col){
		long north=1<<size,south=1<<size,east=1<<size,west=1<<size;
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
	
	public int longToSquare(long l){
		return Long.numberOfTrailingZeros(l)+1;
	}
	
	private boolean checkForChainLibs(int row,int col,boolean black){
		long checkedBNeighbors=0;
		long checkedWNeighbors=0;
		long blackNeighbors = (neighbors[row+col] & blackpieces);
		long whiteNeighbors = (neighbors[row+col] & whitepieces);
		
		return checkForNeighborLibs(row,col,black,checkedBNeighbors,checkedWNeighbors,blackNeighbors,whiteNeighbors);
		
	}
	public int[] highestBitToCoords(long highestBit){
		int square =longToSquare(highestBit);
		int[] coords = squareToRowCol(square);
		return coords;
	}
	public boolean checkForBlackNeighborLibs(int row, int col,boolean black,long checkedBlackNeighbors ,long checkedWhiteNeighbors, long blackNeighbors,long whiteNeighbors){
		int [] coords;
		long highestBit;
		if (black){
			
			
			while (blackNeighbors!=0){
				
				//getting the first neighbor
				highestBit = Long.highestOneBit(blackNeighbors);
				coords = highestBitToCoords(highestBit);
				//checking if neighbor black pieces have libs
				if(liberties(coords[0],coords[1]))
					return true;
				
				//cehck if neighbor black belongs to a chain with libs recursively if not checked
				// if checkedNeighbors |= highestBit == checkedNeighbors , means that highestBit has been explored
				if ((checkedBlackNeighbors |= highestBit)!=checkedBlackNeighbors){
						checkedBlackNeighbors = checkedBlackNeighbors |= highestBit;
						blackNeighbors = (neighbors[coords[0]+coords[1]] *blackpieces);
						whiteNeighbors = (neighbors[coords[0]+coords[1]]);
						checkForNeighborLibs(coords[0],coords[1],black,checkedBlackNeighbors,checkedWhiteNeighbors,blackNeighbors,whiteNeighbors);
					 
				}
				else{
					// if highestBit has been explored i remove it and call the method again without it on black neighbors
					blackNeighbors = blackNeighbors ^= highestBit;
					highestBit = Long.highestOneBit(blackNeighbors);
					coords = highestBitToCoords(highestBit);
					checkForNeighborLibs(coords[0],coords[1],black,checkedBlackNeighbors,checkedWhiteNeighbors,blackNeighbors,whiteNeighbors);
				}
				
			}
			// check if we kill a whit stone		
		    while(whiteNeighbors!=0){
				highestBit = Long.highestOneBit(whiteNeighbors);
				coords = highestBitToCoords(highestBit);
				if(!liberties(coords[0],coords[1]))
					return true;
				if ((checkedWhiteNeighbors |= highestBit)!=checkedWhiteNeighbors){
					checkedWhiteNeighbors = checkedWhiteNeighbors |= highestBit;
					whiteNeighbors = (neighbors[coords[0]+coords[1]] *whitepieces);
					blackNeighbors = (neighbors[coords[0]+coords[1]]);
					checkForNeighborLibs(coords[0],coords[1],black,checkedBlackNeighbors,checkedWhiteNeighbors,blackNeighbors,whiteNeighbors);
			}				
			
			
		return true;
		}
	return false;
		}
	}
	
	public boolean isLegal(int row ,int col, boolean black){
		if (liberties(row,col))
			return true;
		return checkForChainLibs(row,col,black);
	}
	public void removeStone(long newStone,boolean black){
		if (black){
			newStone ^= blackpieces;
		}
		else{
			newStone ^= whitepieces;
		}
	}
	
	public void addStone(int row, int col,boolean black){
		long newStone = getBite(getSquare(row,col)) ;
		if (black){
			newStone |= blackpieces;
		}
		else {
			newStone |= whitepieces;
		}
		
		if (!isLegal(row,col,black)){
			removeStone(newStone,black);
		}
		
	}
	
}

