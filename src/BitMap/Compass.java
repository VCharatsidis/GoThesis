package BitMap;

import java.util.Arrays;

public class Compass {

	private static Compass comp = new Compass();
	private int[] mask;

	private Compass() {


		int row = 7;
		int col = 7;
		mask = new int[row * col];
		Arrays.fill(mask, 0);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (i == 0 || j == 0 || i == 6 || j == 6)
					mask[i * col + j] = 0;
				else if (i == 1 || j == 1 || i == 5 || j == 5)
					mask[i * col + j] = 1;
				else if (i == 2 || j == 2 || i == 4 || j == 4)
					mask[i * col + j] = 5;
				else
					mask[i * col + j] = 6;
			}
		}

	}

	public int[] getMask() {
		return mask;
	}

	public static Compass getComp() {
		return comp;
	}
}
