package org.afg.mathic.util;

public class AsyncTaskOptions {
	private boolean isPaused;
	
	public AsyncTaskOptions(){
		isPaused = false;
	}
	
	public void pause(){
		isPaused = true;
	}
	
	public void resume(){
		isPaused = false;
	}
	
	public boolean isPaused(){
		return isPaused;
	}
}
