package BitMap;

import org.junit.Before;
import org.junit.Test;

public class InfluenceTest {

	private BitBoard board;
	private Influence inf;

	@Before
	public void setup() {
		board = new BitBoard(7, 7);
		inf = new Influence(board);
	}
	@Test
	public void test() {

		board.pass();
		board.addStone(3, 3);

		inf.printer();
		// assertEquals(280, inf.getAllInfluence());
		// board.addStone(1, 1);
		inf.printer();
		// assertEquals(0, inf.getAllInfluence());

		// board.addStone(1, 1);
		inf.printer();
		// assertEquals(220, inf.getAllInfluence());

	}



}
