package org.afg.mathic.world;

import java.io.Serializable;

public class Mode implements Serializable{
	private static final long serialVersionUID = 8949368598494491784L;

	// aux
	public String name = "normal";
	
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
	
	// background
	public boolean colorChanges = true;
	
	// equation appearence
	public boolean textSizeVariates = false;
	public boolean textSizeRandom = false;
	public int startTextSize = 68;
	public int textSizeMin = 18;
	public int textSizeMax = 68;
	public int textSizeRate = -5;
	public boolean hasMissingChar = false;
	
	// equation
	public boolean hasAddition = true;
	public boolean hasSubstraction = false;
	public boolean hasMultiplication = false;
	public boolean hasDivision = false;
	public int startMinNumbers = 0;
	public int startMaxNumbers = 1;
	public double minNumbersChangeRate = 0.6;
	public double maxNumbersChangeRate = 1.2;
}
