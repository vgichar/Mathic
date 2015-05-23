package org.afg.mathic.world.Modes;

public class MemoryMode extends Mode{
	private static final long serialVersionUID = 7124616730717331284L;

	// time
	public boolean hasTime = true;
	public boolean isTimePerGuess = true;
	public int startTime = 1000;
	public double timeChangeRate = 1.1;
	public int timeChangeLevelRate = 10;

	// pass equation
	public boolean hasPasses = false;
	public int numberOfPasses = 3;
	public int passCooldownLevels = 0;

	// question appearence
	public int fadeTime = 1200;
	public int numberOfColors = 3;
	public int matrixSize = 3;
	public int numberOfColorsChangeRate = 3;
	public int numberOfColorsChangeValue = 1;
	public int matrixSizeChangeRate = 10;
	public int matrixSizeChangeValue = 1;

	// color question
	public final ColorHolder trueColor = new ColorHolder();
	public final ColorHolder falseColor = new ColorHolder();
	public boolean hasBorders = true;
}
