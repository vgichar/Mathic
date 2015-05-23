package org.afg.mathic.world.Questions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.graphics.Color;

public class ColorQuestion {
	public static Random rand = new Random();

	private Integer bgColor;
	private Integer textColor;
	private String text;
	private boolean correct;

	public ColorQuestion(int num) {
		HashMap<String, Integer> colors = generateColors(num);
		
		ArrayList<String> colorNames = new ArrayList<String>();
		
		for (String name : colors.keySet()) {
			colorNames.add(name);
		}
		
		String otherText = colorNames.get(rand.nextInt(num));
		
		text = colorNames.get(rand.nextInt(num));
		while(text.equals(otherText))
			text = colorNames.get(rand.nextInt(num));
		
		textColor = colors.get(otherText);
		bgColor = colors.get(text);
		correct = true;

		if (rand.nextBoolean()) {
			text = otherText;
			correct = false;
		}
	}

	private HashMap<String, Integer> generateColors(int numColors) {
		HashMap<String, Integer> colors = new HashMap<String, Integer>(
				numColors + (numColors / 2));

		colors.put("Red", Color.RED);
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Blue", Color.BLUE);
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Green", Color.GREEN);
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Black", Color.BLACK);
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("White", Color.WHITE);
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Gray", Color.GRAY);
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Yellow", Color.YELLOW);
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Orange", Color.rgb(255, 165, 0));
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Brown", Color.rgb(165, 42, 42));
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Purple", Color.rgb(160, 32, 240));
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Violet", Color.rgb(238, 130, 238));
		numColors--;
		if (numColors == 0)
			return colors;
		colors.put("Pink", Color.rgb(255, 192, 203));
		numColors--;
		if (numColors == 0)
			return colors;

		return colors;
	}

	public boolean isCorrect() {
		return correct;
	}
	
	public Integer getBgColor(){
		return bgColor;
	}
	
	public Integer getTextColor(){
		return textColor;
	}

	public String toString() {
		return text;
	}
}
