package BitMap;


public class Territory {

	private Influence inf;
	private long blackTerritory;
	private long whiteTerritory;
	private int[] influence;
	private int width;
	private int height;
	
	public Territory(Influence inf) {
		this.inf = inf;
		influence = inf.getInfluence();
		height=7;
		width=7;
	}
	
	public long getBit(int row,int col){
		return 1L<<(row*width+col);
	}
	
	public void corner(){
	
		int row = 0;
		int col = 0;

			if ((blackTerritory & getBit(row+1,col+1)) == getBit(row+1,col+1))
					blackTerritory |= getBit(row,col);	
			else if ((whiteTerritory & getBit(row+1,col-1)) == getBit(row+1,col-1))
				whiteTerritory |= getBit(row,col);

		row = 0;
		col = 6;

			if ((blackTerritory & getBit(row+1,col-1)) == getBit(row+1,col-1))
				blackTerritory |= getBit(row,col);
			else if ((whiteTerritory & getBit(row+1,col-1)) == getBit(row+1,col-1))
				whiteTerritory |= getBit(row,col);

		row = 6;
		col = 0;

			if ((blackTerritory & getBit(row-1,col+1)) == getBit(row-1,col+1))
				blackTerritory |= getBit(row,col);
			else if ((whiteTerritory & getBit(row-1,col+1)) == getBit(row-1,col+1))
				whiteTerritory |= getBit(row,col);
		
		row = 6;
		col = 6;

		if ((blackTerritory & getBit(row - 1, col - 1)) == getBit(row - 1,
				col - 1))
			blackTerritory |= getBit(row, col);
		else if ((whiteTerritory & getBit(row - 1, col - 1)) == getBit(row - 1,
				col - 1))
			whiteTerritory |= getBit(row, col);

	}
	
	public void sides() {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (row == 0 || col == 0 || row == 6 || col == 6) {
					if (influence[row * width + col] >= 5) {
						blackTerritory |= getBit(row, col);
					} else if (influence[row * width + col] <= -5) {
						whiteTerritory |= getBit(row, col);
					}
				}
			}
		}
	}

	public void sidesPlusOne() {
		for (int row = 1; row < height - 1; row++) {
			for (int col = 1; col < width - 1; col++) {
				if (row == 1 || col == 1 || row == 5 || col == 5) {
					if (influence[row * width + col] >= 12) {
						blackTerritory |= getBit(row, col);
					} else if (influence[row * width + col] <= -12) {
						whiteTerritory |= getBit(row, col);
					}
				}
			}
		}

	}

	public void center() {
		for (int row = 2; row < height - 2; row++) {
			for (int col = 2; col < width - 2; col++) {
				if (row == 2 || col == 2 || row == 4 || col == 4) {
					if (influence[row * width + col] >= 30) {
						blackTerritory |= getBit(row, col);
					} else if (influence[row * width + col] <= -30) {
						whiteTerritory |= getBit(row, col);
					}
				}
			}
		}
	}
	
	public void countTerritory() {
		countBlack();
		countWhite();

	}

	public void countBlack(){
		 
	}
	
	public void countWhite(){
		
	}
}
