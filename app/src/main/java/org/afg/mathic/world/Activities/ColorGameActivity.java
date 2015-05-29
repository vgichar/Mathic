package org.afg.mathic.world.Activities;

import org.afg.mathic.R;
import org.afg.mathic.util.AsyncTaskOptions;
import org.afg.mathic.util.GameActivity;
import org.afg.mathic.util.MediaPlayerManager;
import org.afg.mathic.util.TimeLapse;
import org.afg.mathic.world.Questions.ColorQuestion;
import org.afg.mathic.world.Modes.ColorMode;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ColorGameActivity extends GameActivity {

	private LinearLayout container;
	private TextView tv_equation;
	private ProgressBar pb_timer;
	private TextView tv_level;
	private TimeLapse time_lapse_task;
	private AsyncTaskOptions async_opt;

	private ColorMode mode;
	private ColorQuestion current_color;

	private int level;
	private boolean isGameStarted;
	private boolean isGameRestarted;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
	}

	@Override
	public void onResume() {
		super.onResume();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mode = (ColorMode) getIntent().getSerializableExtra("#Mode");

		tv_equation = (TextView) findViewById(R.id.tv_equation);
		pb_timer = (ProgressBar) findViewById(R.id.pb_timer);
		tv_level = (TextView) findViewById(R.id.tv_level);
		container = (LinearLayout) findViewById(R.id.container);

		if (mode.hasTime) {
			async_opt = new AsyncTaskOptions();
			async_opt.pause();
			time_lapse_task = new TimeLapse(async_opt, this, pb_timer);
			time_lapse_task.execute();
		}

		initGame();
	}

	@Override
	public void onPause() {
		super.onPause();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		if (mode.hasTime) {
			time_lapse_task.cancel(true);
		}
	}

	public void yes_click(View v) {
		evaluateAnswer(true);
	}

	public void no_click(View v) {
		evaluateAnswer(false);
	}

	public void initGame() {
		level = -1;
		isGameStarted = false;
		isGameRestarted = true;
		tv_equation.setTextSize(mode.startTextSize);
		pb_timer.setMax(mode.startTime);
		pb_timer.setProgress(mode.startTime);

		((LinearLayout) findViewById(R.id.button_container))
				.setVisibility(LinearLayout.VISIBLE);
		((LinearLayout) findViewById(R.id.menu_button_container))
				.setVisibility(LinearLayout.GONE);

		updateQuestion();
	}

	public void startGame() {
		isGameStarted = true;
		isGameRestarted = true;
		if (mode.hasTime) {
			async_opt.resume();
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		findViewById(R.id.tv_level).setVisibility(View.VISIBLE);
		findViewById(R.id.info_button).setVisibility(View.GONE);
	}

	public void endGame() {
		isGameStarted = false;
		isGameRestarted = false;
		tv_equation.setTextSize(50);
		tv_equation.setText("Game Over\n Restart?");
		if (mode.hasTime) {
			async_opt.pause();
		}

		((LinearLayout) findViewById(R.id.button_container))
				.setVisibility(LinearLayout.GONE);

		((LinearLayout) findViewById(R.id.menu_button_container))
				.setVisibility(LinearLayout.VISIBLE);

		try {
			Thread.sleep(10);
            MediaPlayerManager.play(R.raw.buzz, this);
			Thread.sleep(300);
            MediaPlayerManager.pause();
		} catch (Exception e) {
			e.printStackTrace();
		}

		SharedPreferences preferences = getSharedPreferences("highscore", MODE_PRIVATE);
		if(preferences.getInt(mode.highscoreKey, 0) < level) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(mode.highscoreKey, level);
			editor.commit();
		}

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		findViewById(R.id.tv_level).setVisibility(View.INVISIBLE);
		findViewById(R.id.info_button).setVisibility(View.VISIBLE);
	}

	public void updateQuestion() {
		level++;
		
		if (mode.hasTime) {
			if (level % mode.timeChangeLevelRate == 0) {
				pb_timer.setMax((int) (pb_timer.getMax() * mode.timeChangeRate));
			}

			if (mode.isTimePerGuess) {
				pb_timer.setProgress(pb_timer.getMax());
			}
		}

		current_color = new ColorQuestion(mode.numberOfColors);

		tv_level.setText(level + "");

		if (mode.hasMissingChar) {
			char[] text = current_color.toString().toCharArray();
			text[ColorQuestion.rand.nextInt(text.length)] = '.';
			tv_equation.setText(new String(text));
		} else {
			tv_equation.setText(current_color.toString());
		}

		if (mode.textSizeVariates) {
			if (mode.textSizeRandom) {
				tv_equation.setTextSize(ColorQuestion.rand
						.nextInt(mode.textSizeMax - mode.textSizeMin)
						+ mode.textSizeMin);
			} else {
				tv_equation.setTextSize(Math.min(Math.max(
						tv_equation.getTextSize() + mode.textSizeRate,
						mode.textSizeMin), mode.textSizeMax));
			}
		}

		container.setBackgroundColor(current_color.getBgColor());
		tv_equation.setTextColor(current_color.getTextColor());
	}

	public void evaluateAnswer(boolean answer) {
		if (!isGameRestarted) {
			if (answer) {
				initGame();
			} else {
				onBackPressed();
			}
			return;
		}
		if (!isGameStarted) {
			startGame();
		}
		if (current_color.isCorrect() == answer) {
			updateQuestion();
		} else {
			endGame();
		}
	}
}