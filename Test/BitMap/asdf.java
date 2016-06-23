package BitMap;

public class asdf {

	public static void main(String[] args) {
		BitBoard board = new BitBoard(7, 7);


		// board.addStone(1, 1);
		board.addStone(3, 3);
		board.pass();
		board.addStone(2, 3);
		board.pass();
		board.addStone(1, 3);
		board.pass();
		board.addStone(3, 4);

		//board.addStone(2, 3);
		// board.addStone(3, 2);

		// board.addStone(2, 4);
		// board.addStone(1, 2);

		// / board.addStone(0, 6);
		// board.addStone(5, 5);
		// board.addStone(6, 0);
		// board.pass();

		// board.addStone(4, 4);
		// board.addStone(4, 3);
		// board.addStone(2, 4);
		// board.addStone(0, 2);
		// board.addStone(3, 2);
		// board.addStone(6, 6);

		Influence inf = new Influence(board);
		inf.everyInfluence();
		inf.printer();

	}
}
