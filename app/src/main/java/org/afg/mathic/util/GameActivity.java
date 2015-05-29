package org.afg.mathic.util;

import org.afg.mathic.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public abstract class GameActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		MediaPlayerManager.stop();
	}

	public abstract void initGame();

	public abstract void startGame();

	public abstract void endGame();

	public abstract void updateQuestion();

	public abstract void evaluateAnswer(boolean answer);
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.abc_slide_in_bottom,
				R.anim.abc_slide_out_top);
	}
}
