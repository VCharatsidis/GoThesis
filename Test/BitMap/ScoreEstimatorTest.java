package BitMap;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ScoreEstimatorTest {
	BitBoard board;
	ScoreEstimator se ;
	
	@Before
	public void setUp() throws Exception {
		board = new BitBoard(7,7);
		se = new ScoreEstimator(board);
	}
	@Test
	public void testFindAgroup3(){
		board.addStone(0, 1);
		board.addStone(1, 1);
		board.addStone(1, 0);
		board.addStone(1, 2);
		board.addStone(2, 1);
		board.addStone(2, 0);
		board.addStone(3, 0);
		se= new ScoreEstimator(board);
		long checked =0;
		System.out.println("find a group test 3= "+Long.toBinaryString(se.findAgroup(board.getBite(board.getSquare(1,0)),checked)));
		Renderer.drawBoard(board);
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testFindAgroup2(){
		board.addStone(4, 6);
		board.addStone(3, 6);
		board.addStone(4, 5);
		board.addStone(3, 5);
		board.addStone(4, 4);
		board.addStone(3, 4);
		board.addStone(5, 3);
		board.addStone(4, 3);
		board.addStone(6, 3);
		board.addStone(3, 3);
		board.addStone(4, 2);
		board.addStone(1, 3);
		board.addStone(5, 1);
		se= new ScoreEstimator(board);
		long checked =0;
		System.out.println("find a group test 2= "+Long.toBinaryString(se.findAgroup(board.getBite(board.getSquare(4,6)),checked)));
		Renderer.drawBoard(board);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFindAgroup() {
		board.addStone(4, 6);
		board.addStone(3, 6);
		board.addStone(4, 5);
		board.addStone(3, 5);
		board.addStone(4, 4);
		board.addStone(3, 4);
		board.addStone(5, 3);
		board.addStone(4, 3);
		board.addStone(6, 3);
		se= new ScoreEstimator(board);
		long checked =0;
		System.out.println("find a group test 1= "+Long.toBinaryString(se.findAgroup(board.getBite(board.getSquare(4,6)),checked)));
		Renderer.drawBoard(board);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		assertEquals(se.findAgroup(se.getBite(se.getSquare(4,6)),checked),board.getBlackpieces());
	}

}
