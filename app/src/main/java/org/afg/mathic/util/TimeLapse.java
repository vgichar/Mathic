package org.afg.mathic.util;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ProgressBar;

public class TimeLapse extends AsyncTask<Void, Integer, Void> {
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
