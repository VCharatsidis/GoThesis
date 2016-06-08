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
		Renderer.drawBoard(board);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Renderer.drawBoard(board);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	@Test
	public void longToSquare(){
		assertEquals(0, board.longToSquare(1));
		assertEquals(10, board.longToSquare(1<<10));
		
		
	}
	
}
