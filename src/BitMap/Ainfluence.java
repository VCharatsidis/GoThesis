package BitMap;

public class Ainfluence {
	
	
	public Ainfluence (Influence inf){
		this.inf = inf;
		board = inf.getBoard();
		width =  board.getWidth();
		height = board.getHeight();
		
		
	}
	private Influence inf;
	public int[] bestMove;
	private int width;
	private int height;
	private BitBoard board;
	
	public void findBestMove(){
		for (int i=0;i<width;i++){
			for (int j=0;j<height;j++){
				
			}
		}
	}
	
}