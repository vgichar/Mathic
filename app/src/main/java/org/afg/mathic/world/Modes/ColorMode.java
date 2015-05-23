package org.afg.mathic.world.Modes;

public class ColorMode extends Mode {
	private static final long serialVersionUID = 11111L;

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
	public boolean textSizeVariates = false;
	public boolean textSizeRandom = false;
	public int startTextSize = 68;
	public int textSizeMin = 18;
	public int textSizeMax = 68;
	public int textSizeRate = -5;
	public boolean hasMissingChar = false;

	// color question
	public int numberOfColors = 8;
}
