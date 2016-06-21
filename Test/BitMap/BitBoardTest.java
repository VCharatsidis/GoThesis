package BitMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BitBoardTest {
	
	BitBoard board;
	@Before
	public void setUp() throws Exception {
		board =  new BitBoard(7,7);
	}

	@Test
	public void testGetSquare() {
		
		assertEquals(0, 0);
	}

	@Test
	public void testIsLegal() {
		
		assertTrue(board.isLegal(3, 3));
		board.addStone(3, 3);
		assertFalse(board.isLegal(3, 3));
	}
	@Test
	public void testliberties(){
		board.addStone(5,4);
		board.addStone(4,4);
		board.addStone(4,3);
		board.addStone(1,6);
		board.addStone(3,4);
		board.addStone(2,6);
		board.addStone(4,5);

		assertEquals(false,board.liberties(4, 4));
	}
	@Test
	public void testBlackChainLibs(){
	
		board.addStone(5,4);
		board.addStone(4,4);
		board.addStone(4,3);
		board.addStone(1,6);
		board.addStone(3,4);
		board.addStone(2,6);
		board.addStone(4,5);
	

	}
	@Test
	public void longToSquare(){
		assertEquals(0, board.longToSquare(1));
		assertEquals(10, board.longToSquare(1<<10));
		
		
	}

	@Test
	public void captives() {
		board.addStone(0, 0);
		board.addStone(0, 1);
		board.addStone(1, 0);
		board.addStone(1, 1);
		board.addStone(3, 3);
		board.addStone(2, 0);

		assertEquals(2, board.getBlackCaptives());
	}

	@Test
	public void koCheck() {
		board.addStone(0, 0);
		board.addStone(0, 1);
		board.addStone(1, 1);
		board.addStone(1, 0);
		board.addStone(0, 2);
		board.addStone(3, 3);
		board.addStone(0, 0);


		assertTrue(board.koCheck());

	}

	@Test
	public void equalsTest1() {
		board.addStone(0, 1);
		board.addStone(1, 1);

		board.addStone(1, 0);
		board.addStone(0, 2);

		board.addStone(4, 4);
		board.addStone(0, 0);

		board.addStone(5, 5);
		board.addStone(6, 6);



		BitBoard board2 = new BitBoard(7, 7);

		board2.addStone(0, 1);
		board2.addStone(1, 1);

		board2.addStone(1, 0);
		board2.addStone(0, 2);

		board2.addStone(5, 5);
		board2.addStone(6, 6);

		board2.addStone(4, 4);
		board2.addStone(0, 0);

		Renderer.drawBoard(board);

		Renderer.drawBoard(board2);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertFalse(board.equals(board2));
		assertEquals(board.hashCode(), board2.hashCode());
	}
	

}
