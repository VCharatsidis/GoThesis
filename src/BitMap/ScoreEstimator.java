package BitMap;

public class ScoreEstimator {

	public ScoreEstimator(BitBoard board){
		this.board = board;
		this.width = board.getWidth();
		this.height = board.getHeight();
		goboard = board.board();
		whitepieces = board.getWhitepieces();
		blackpieces = board.getBlackpieces();
		allchecked =0;
		allneighbors = new long[width*height];
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				allneighbors[i*width+j] = diagonalMask(i,j) | board.getNeighbors()[i*width+j];
			}
			
		}
	}
	private long whitepieces;
	private long blackpieces;
	private long goboard ;
	private long allchecked;
	private BitBoard board;
	private int width ;
	private int height ;
	private long[] allneighbors;
	
	public int[] highestBitToCoords(long highestBit){
		int square =longToSquare(highestBit);
		int[] coords = squareToRowCol(square);
		return coords;
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
	public int[] squareToRowCol(int square){
		int[] rowcol={0,0};
		rowcol[0] = (int)Math.floor(square/height);
		rowcol[1] = square % width;
		return rowcol;
	}
	public int longToSquare(long l){
		return Long.numberOfTrailingZeros(l);
	}
	
	public long[] findGroups(BitBoard board){
		long[] groups={0};
		int groupsindex = 0;
		while (Long.bitCount(goboard & ~allchecked)!=0){
			groupsindex++;
			
			long highestBit = Long.highestOneBit(goboard);
			allchecked = allchecked | highestBit;
			long neighborsChecked =0;
			if (((highestBit & blackpieces) | (highestBit & whitepieces))==0)
				groups[groupsindex] = 0;
			else
				groups[groupsindex] = findAgroup(highestBit,neighborsChecked);
			
		}
		return groups;
	}
	
	public long cutNeighbs(long highestBit){
		long opponent;
		long northWest=0,northEast=0,southWest=0,southEast=0;
		if ((highestBit | blackpieces)==blackpieces)
			opponent = whitepieces;
		else
			opponent = blackpieces;
		
		if (((highestBit<<1) | opponent) == opponent){
			if(((highestBit<<7 ) | opponent) == opponent){
				northWest = highestBit<<8;
			}
			if (((highestBit>>7) | opponent ) == opponent){
				southWest = highestBit>>6;
		    }
		}
		if (((highestBit>>1) | opponent) == opponent){
			if (((highestBit<<7) | opponent) == opponent){
				northEast = highestBit<<6;
			}
			if (((highestBit>>7) | opponent) == opponent){
				southEast = highestBit >>8;
			}
		}
		return northWest|=southWest|=northEast|=southEast;
				
	}
	
	public long findAgroup(long highestBit,long neighborsChecked){
		
		int []  coords;
		coords = highestBitToCoords(highestBit);
		
		neighborsChecked |= highestBit;
		
		long blackneighbors;
		long whiteneighbors;
		System.out.println("blackpieces = "+Long.toBinaryString(blackpieces));
		System.out.println("whitepieces = "+Long.toBinaryString(whitepieces));
		System.out.println("highestBit= "+Long.toBinaryString(highestBit));
		System.out.println("highestBit and blackpieces= "+Long.toBinaryString(highestBit & blackpieces));
		System.out.println("highestBit and whitepieces= "+Long.toBinaryString(highestBit & whitepieces));
		if ((highestBit | blackpieces) == blackpieces){
			
			blackneighbors = allneighbors[coords[0]*width+coords[1]] & blackpieces & ~cutNeighbs(highestBit);
			System.out.println("blackneighbors inside while of findAgroup= "+Long.toBinaryString(blackneighbors));
			blackneighbors = blackneighbors & ~neighborsChecked;
			System.out.println("blackneighbors inside while of findAgroup= "+Long.toBinaryString(blackneighbors));
		
			while (blackneighbors != 0){
				highestBit = Long.highestOneBit(blackneighbors);
				neighborsChecked |= highestBit;
				coords = highestBitToCoords(highestBit);
				blackneighbors = blackneighbors & ~highestBit;
				System.out.println("neighborsChecked inside while of findAgroup= "+Long.toBinaryString(neighborsChecked));
				neighborsChecked |= findAgroup(highestBit,neighborsChecked);
			}
			
		}
		else{
			whiteneighbors = allneighbors[coords[0]*width+coords[1]] & whitepieces & ~cutNeighbs(highestBit);
			whiteneighbors = whiteneighbors & ~neighborsChecked;
			System.out.println("whiteneighbors inside while of findAgroup= "+Long.toBinaryString(whiteneighbors));
			
			while (whiteneighbors != 0){
				highestBit = Long.highestOneBit(whiteneighbors);
				neighborsChecked|= highestBit;
				coords = highestBitToCoords(highestBit);
				whiteneighbors = whiteneighbors & ~highestBit;
				System.out.println("neighborsChecked inside while of findAgroup= "+Long.toBinaryString(neighborsChecked));
				neighborsChecked |= findAgroup(highestBit,neighborsChecked);
			}

		}
		return neighborsChecked;
		
	}
	
	
	public long diagonalMask(int row, int col){
		long northWest=0,southWest=0,southEast=0,northEast=0;
		if (row > 0) {
			if (col>0){
				northWest = board.getBite(board.getSquare(row-1,col-1));
			}
		}
		if(row>0){
			if (col<width-1){
				northEast = board.getBite(board.getSquare(row-1,col+1));
			}
	    }
		if (row < height - 1) {
	    	if(col>0){
	    		southWest = board.getBite(board.getSquare(row+1,col-1));
	    	}
	    }
	    if(row<height-1){
	    	if (col<width-1){
	    		southEast = board.getBite(board.getSquare(row+1, col+1));
	    	}
	    }
	    return northWest |= southWest |= southEast |= northEast;
	}
}
