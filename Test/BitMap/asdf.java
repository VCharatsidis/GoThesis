package BitMap;

public class asdf {

	public static void main(String[] args) {
		BitBoard board = new BitBoard(7, 7);

		/* strange bug */
		// board.addStone(1, 1);
		board.addStone(0, 2);
		board.addStone(0, 4);

		board.addStone(1, 1);
		board.addStone(1, 2);

		board.pass();
		board.addStone(1, 3);

		board.addStone(1, 4);
		board.pass();

		board.addStone(1, 5);
		board.pass();

		board.addStone(1, 6);
		board.pass();

		board.addStone(2, 1);
		board.pass();

		board.addStone(2, 2);
		board.pass();

		board.addStone(2, 3);
		board.addStone(2, 4);

		board.pass();
		board.addStone(2, 5);

		board.pass();
		board.addStone(2, 6);

		board.addStone(3, 1);
		board.addStone(3, 2);

		board.addStone(3, 3);
		board.addStone(3, 4);

		board.pass();
		board.addStone(3, 6);

		board.addStone(4, 1);
		board.addStone(4, 2);

		board.pass();
		board.addStone(4, 3);

		board.pass();
		board.addStone(4, 5);

		board.addStone(5, 1);
		board.addStone(5, 2);

		board.pass();
		board.addStone(5, 6);

		board.addStone(6, 1);
		board.addStone(6, 2);

		board.pass();
		board.addStone(6, 3);

		board.pass();
		board.addStone(6, 4);

		board.pass();
		board.addStone(6, 6);

		board.pass();
		board.addStone(1, 0); // -1704
		// board.pass(); //-1560
		Influence inf = new Influence(board);
		inf.everyInfluence();
		inf.printer();

	}
}
