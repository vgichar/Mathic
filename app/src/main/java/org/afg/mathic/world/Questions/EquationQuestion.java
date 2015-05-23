package org.afg.mathic.world.Questions;

import java.util.Random;

public class EquationQuestion {
	public static Random rand = new Random();
	private int num1;
	private int num2;
	private int sum;
	private boolean correct;

	public EquationQuestion(int min, int max) {
		num1 = rand.nextInt(max - min) + min;
		num2 = rand.nextInt(max - min) + min;
		sum = num1 + num2;
		correct = true;

		if (rand.nextBoolean()) {
			sum += (rand.nextBoolean() ? (rand.nextInt(2) + 1) * -1 : rand.nextInt(2) + 1);
			correct = false;
		}
	}

	public boolean isCorrect() {
		return correct;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(num1);
		sb.append("+");
		sb.append(num2);
		sb.append("=");
		sb.append(sum);

		return sb.toString();
	}
}
