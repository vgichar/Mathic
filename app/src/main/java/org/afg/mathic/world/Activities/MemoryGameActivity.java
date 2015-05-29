package org.afg.mathic.world.Activities;

import java.util.LinkedList;

import org.afg.mathic.R;
import org.afg.mathic.util.AsyncTaskOptions;
import org.afg.mathic.util.GameActivity;
import org.afg.mathic.util.MediaPlayerManager;
import org.afg.mathic.util.TimeLapse;
import org.afg.mathic.world.Modes.ColorHolder;
import org.afg.mathic.world.Modes.MemoryMode;
import org.afg.mathic.world.Questions.MemoryQuestion;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MemoryGameActivity extends GameActivity {

	private LinearLayout container;
	private LinearLayout grid_container;
	private ProgressBar pb_timer;
	private TextView tv_level;
	private TimeLapse time_lapse_task;
	private AsyncTaskOptions async_opt;

	private MemoryMode mode;
	private MemoryQuestion current_puzzle;
	private LinkedList<Button> puzzleButtons;

	private int level;
	private boolean isGameStarted;
	private boolean isGameRestarted;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memory_game_layout);
	}

	@Override
	public void onResume() {
		super.onResume();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mode = (MemoryMode) getIntent().getSerializableExtra("#Mode");

		grid_container = (LinearLayout) findViewById(R.id.grid_container);
		pb_timer = (ProgressBar) findViewById(R.id.pb_timer);
		tv_level = (TextView) findViewById(R.id.tv_level);
		container = (LinearLayout) findViewById(R.id.container);

		if (mode.hasTime) {
			async_opt = new AsyncTaskOptions();
			async_opt.pause();
			time_lapse_task = new TimeLapse(async_opt, this, pb_timer);
			time_lapse_task.execute();
		}

		reinitGame("Start Game?");
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
		level = 0;
		isGameStarted = false;
		isGameRestarted = true;
		pb_timer.setMax(mode.startTime);
		pb_timer.setProgress(mode.startTime);
		puzzleButtons = new LinkedList<Button>();

		((LinearLayout) findViewById(R.id.menu_button_container))
				.setVisibility(LinearLayout.GONE);

		updateQuestion();
	}

	private void reinitGame(String message) {
		level = -1;
		isGameStarted = false;
		isGameRestarted = false;
		pb_timer.setMax(mode.startTime);
		pb_timer.setProgress(mode.startTime);
		puzzleButtons = new LinkedList<Button>();

		TextView tv_restart = new TextView(this);
		tv_restart.setTextSize(50);
		tv_restart.setTextColor(Color.WHITE);
		tv_restart.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		tv_restart.setText(message);
		grid_container.removeAllViews();
		grid_container.addView(tv_restart);
		grid_container.invalidate();
		((LinearLayout) findViewById(R.id.menu_button_container))
				.setVisibility(LinearLayout.VISIBLE);
	}

	public void startGame() {
		if (!isGameStarted || !isGameRestarted) {
			isGameStarted = true;
			isGameRestarted = true;
			if (mode.hasTime) {
				async_opt.resume();
			}
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		findViewById(R.id.tv_level).setVisibility(View.VISIBLE);
		findViewById(R.id.info_button).setVisibility(View.GONE);
	}

	public void endGame() {
		isGameStarted = false;
		isGameRestarted = false;
		if (mode.hasTime) {
			async_opt.pause();
		}

		TextView tv_restart = new TextView(this);
		tv_restart.setTextSize(50);
		tv_restart.setTextColor(Color.WHITE);
		tv_restart.setGravity(Gravity.CENTER_HORIZONTAL
				| Gravity.CENTER_VERTICAL);
		tv_restart.setText("Game Over\n Restart?");
		grid_container.removeAllViews();
		grid_container.addView(tv_restart);
		grid_container.invalidate();
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
		async_opt.pause();
		level++;

		if (mode.hasTime) {
			if (level % mode.timeChangeLevelRate == 0) {
				pb_timer.setMax((int) (pb_timer.getMax() * mode.timeChangeRate));
			}

			if (mode.isTimePerGuess) {
				pb_timer.setProgress(pb_timer.getMax());
			}
		}

		grid_container.removeAllViews();
		tv_level.setText(level + "");

		int sizeX = mode.matrixSize
				+ ((level / mode.matrixSizeChangeRate) * mode.matrixSizeChangeValue);
		int sizeY = mode.matrixSize
				+ ((level / mode.matrixSizeChangeRate) * mode.matrixSizeChangeValue);
		int num = mode.numberOfColors
				+ ((level / mode.numberOfColorsChangeRate) * mode.numberOfColorsChangeValue);
		int fadeTime = mode.fadeTime;
		final ColorHolder trueColor = mode.trueColor;
		final ColorHolder falseColor = mode.falseColor;

		container.setBackgroundColor(falseColor.color);
		grid_container.setBackgroundColor(falseColor.color);
		current_puzzle = new MemoryQuestion(sizeX, sizeY, num);

		final LinkedList<Button> buttons = new LinkedList<Button>();

		for (int i = 0; i < sizeX; i++) {
			LinearLayout ll = new LinearLayout(this);
			if(mode.hasBorders){
				ll.setBackgroundColor(trueColor.color);
			}else{
				ll.setBackgroundColor(falseColor.color);
			}
			grid_container.addView(ll);
			for (int j = 0; j < sizeY; j++) {
				Button b = new Button(this);
				buttons.add(b);
				ll.addView(b);

				if (current_puzzle.getMatrix()[i][j]) {
					b.setBackgroundColor(trueColor.color);
					puzzleButtons.add(b);
				} else {
					b.setBackgroundColor(falseColor.color);
				}
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						grid_container.getWidth() / sizeY,
						grid_container.getWidth() / sizeY, 1f / sizeY);

				int topMargin = 0;
				int leftMargin = 0;
				if (j == 0) {
					topMargin = 1;
				}
				if (i == 0) {
					leftMargin = 1;
				}
				params.setMargins(topMargin, leftMargin, 1, 1);

				b.setLayoutParams(params);
			}
		}

		for (final Button b : buttons) {
			if (((ColorDrawable) b.getBackground()).getColor() == trueColor.color) {
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						v.setBackgroundColor(trueColor.color);
						puzzleButtons.remove(b);
						if (puzzleButtons.size() <= 0) {
							evaluateAnswer(true);
						}
					}
				});
				
				Integer colorFrom = mode.trueColor.color;
				Integer colorTo = mode.falseColor.color;
				ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
				colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

							@Override
							public void onAnimationUpdate(ValueAnimator animator) {
								b.setBackgroundColor((Integer) animator
										.getAnimatedValue());
							}

						});
				colorAnimation.start();
			} else {
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						evaluateAnswer(false);
					}
				});
			}
		}
		startGame();
		async_opt.resume();
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
		if (answer) {
			updateQuestion();
		} else {
			endGame();
		}
	}
}