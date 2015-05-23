package org.afg.mathic.world.Questions;

import java.util.Random;

public class MemoryQuestion {
	public static Random rand = new Random();

	private boolean[][] matrix;

	public MemoryQuestion(int sizeX, int sizeY, int num) {
		num = Math.min(sizeX * sizeY, num);
		matrix = new boolean[sizeX][sizeY];

		while (num > 0) {
			int x = rand.nextInt(sizeX);
			int y = rand.nextInt(sizeY);

			if (matrix[x][y] == false) {
				num--;
				matrix[x][y] = true;
			}
		}
	}

	public boolean guess(boolean[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != this.matrix[i][j]){
					return false;
				}
			}

		}
		return true;
	}
	
	public boolean[][] getMatrix(){
		return matrix;
	}
}
