

import javax.swing.JPanel;


	public class Gui extends JPanel {
		
//		private int startX = 10;
//		private int startY = 10;
//		private int size = 60;
//		private Go go =new Go(45);
//		private String s;
//		private MiniMax absearch = new MiniMax(go);
//		
//		public Gui(){
//			MyAdapter adapter =new MyAdapter();
//			addMouseListener(adapter);
//			absearch.optimalMove();
//		}
//		
//		
//		public void paintComponent(Graphics g){
//			super.paintComponent(g);
//			for (int i=0;i<3;i++){
//				for (int j=0;j<3;j++){
//					
//					
//					g.drawRect(10+i*size,10+j*size,size,size);
//					//g.drawRect(10+j*size,10+i*size,size,size);
//					
//				
//					//g.drawString(tic.board[i][j].toString(),  10+j*size,70+i*size);
//				}
//			}   
//		}
//
//		class MyAdapter extends MouseAdapter{
//			public void mouseClicked(MouseEvent event){
//					if (go.gameOver()) {
//						return;
//					}
//					go.playMove((event.getX()-startX)/size,(event.getY()-startY)/size);
//					System.out.println((event.getX()-startX)/size+" "+(event.getY()-startY)/size);
//					repaint();
//					
//			}
//		}
//		
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		JFrame frame = new JFrame();
//		frame.setTitle("Board");
//		frame.setSize(500,500);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		Container contentPane = frame.getContentPane();
//		contentPane.add(new Gui());
//		frame.show();
//	}

}
