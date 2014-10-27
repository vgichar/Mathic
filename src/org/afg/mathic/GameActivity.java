package org.afg.mathic;

import org.afg.mathic.util.AsyncTaskOptions;
import org.afg.mathic.world.Equation;
import org.afg.mathic.world.Mode;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends Activity {

	private LinearLayout container;
	private TextView tv_equation;
	private ProgressBar pb_timer;
	private TextView tv_level;
	private TimeLapse time_lapse_task;
	private AsyncTaskOptions async_opt;

	private Mode mode;
	private Equation current_equation;

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

		mode = (Mode) getIntent().getSerializableExtra("#Mode");

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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.abc_slide_in_top,
				R.anim.abc_slide_out_bottom);
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
		tv_equation.setTextSize(mode.startTextSize);
		pb_timer.setMax(mode.startTime);
		pb_timer.setProgress(mode.startTime);

		((LinearLayout) findViewById(R.id.button_container))
				.setVisibility(LinearLayout.VISIBLE);
		((LinearLayout) findViewById(R.id.menu_button_container))
				.setVisibility(LinearLayout.GONE);

		updateEquation();
	}

	public void startGame() {
		isGameStarted = true;
		isGameRestarted = true;
		if (mode.hasTime) {
			async_opt.resume();
		}
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
	}

	public void updateEquation() {
		level++;

		if (mode.hasTime) {
			if (level % mode.timeChangeLevelRate == 0) {
				pb_timer.setMax((int) (pb_timer.getMax() * mode.timeChangeRate));
			}

			if (mode.isTimePerGuess) {
				pb_timer.setProgress(pb_timer.getMax());
			}
		}

		current_equation = new Equation(
				(int) (Math.ceil((mode.startMinNumbers + level)
						* mode.minNumbersChangeRate)),
				(int) (Math.ceil((mode.startMaxNumbers + level)
						* mode.maxNumbersChangeRate)));

		tv_level.setText(level + "");

		if (mode.hasMissingChar) {
			char[] text = current_equation.toString().toCharArray();
			text[Equation.rand.nextInt(text.length)] = '.';
			tv_equation.setText(new String(text));
		} else {
			tv_equation.setText(current_equation.toString());
		}

		if (mode.textSizeVariates) {
			if (mode.textSizeRandom) {
				tv_equation.setTextSize(Equation.rand.nextInt(mode.textSizeMax
						- mode.textSizeMin)
						+ mode.textSizeMin);
			} else {
				tv_equation.setTextSize(Math.min(Math.max(
						tv_equation.getTextSize() + mode.textSizeRate,
						mode.textSizeMin), mode.textSizeMax));
			}
		}

		if (mode.colorChanges) {
			int randColor = Color.rgb(Equation.rand.nextInt(200),
					Equation.rand.nextInt(200), Equation.rand.nextInt(200));
			container.setBackgroundColor(randColor);
			pb_timer.setBackgroundColor(randColor);
		}

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
		if (current_equation.isCorrect() == answer) {
			updateEquation();
		} else {
			endGame();
		}
	}
}

class TimeLapse extends AsyncTask<Void, Integer, Void> {
	private AsyncTaskOptions opt;
	private GameActivity ctx;
	private ProgressBar pb;

	private long endTime;
	private int deltaTime;
	private long startTime;

	// aux
	private int FRAME_TIMEOUT = 20;

	public TimeLapse(AsyncTaskOptions opt, GameActivity ctx, ProgressBar pb) {
		super();
		this.opt = opt;
		this.ctx = ctx;
		this.pb = pb;
		this.endTime = 0;
		this.startTime = 0;
		this.deltaTime = 0;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			while (true) {
				if (opt.isPaused()) {
					while (opt.isPaused()) {
						Thread.sleep(FRAME_TIMEOUT);
					}
					startTime = 0;
				}
				endTime = SystemClock.elapsedRealtime();
				if (startTime != 0)
					deltaTime = (int) (endTime - startTime);
				startTime = SystemClock.elapsedRealtime();
				publishProgress(deltaTime);
				Thread.sleep(FRAME_TIMEOUT);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onProgressUpdate(Integer... args) {
		super.onProgressUpdate(args);
		pb.setProgress(pb.getProgress() - args[0]);
		if (pb.getProgress() <= 0) {
			ctx.endGame();
		}
	}
}
