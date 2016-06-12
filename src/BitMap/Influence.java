package BitMap;

import java.util.Arrays;

public class Influence {

	public Influence(BitBoard board){
		this.board = board;
		this.width = board.getWidth();
		this.height = board.getHeight();
		
		influence = new double[width*height];
		Arrays.fill(influence, 0);
		
		lastMove =0;
	}
	private BitBoard board;
	private double[] influence;
	private double[] everyInfluence;
	private long lastMove;
	
	private int width ;
	private int height ;
	
	
	public BitBoard getBoard() {
		return board;
	}

	public double[] getInfluence() {
		
		return influence;
	}
	
	public void everyInfluence(){
		for (int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				
			}
		}
	}
	/** updates the double array influence when a stone is played*/ 
	public void makeInfluence(){
		lastMove = board.getLastMove();
		int[] coords ={0,0 };
		double flex =1.05;
		double attackBonus=1.1;
		coords = highestBitToCoords(lastMove);
		int row =coords[0];
		int col =coords[1];
		for (int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				if (i==row && j==col)
					continue;
				else{
					if((lastMove & board.getBlackpieces()) == lastMove){
						influence[i*width+j] = influence[i*width+j]+ 1/( Math.abs(i-row)*flex+Math.abs(j-col)*flex);
						if ((getBite(getSquare(row,col)) & board.getWhitepieces()) == board.getWhitepieces())
							// if a black plays near a white stone gets bonus influence
							influence[i*width+j] = influence[i*width+j]*attackBonus;
						
					}
					else{
						influence[i*width+j] = influence[i*width+j]- 1/( Math.abs(i-row)*flex+Math.abs(j-col)*flex);
						if ((getBite(getSquare(row,col)) & board.getBlackpieces()) == board.getBlackpieces())
							influence[i*width+j] = influence[i*width+j]*attackBonus;
					}
				}
			}
		}
	}
	
	public void printer(){
		makeInfluence();
		for (int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				System.out.print(influence[i*width+j] +" ");
			}
			System.out.println();

		}
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