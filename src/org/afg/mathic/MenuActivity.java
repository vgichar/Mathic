package org.afg.mathic;

import java.util.LinkedList;
import java.util.List;

import org.afg.mathic.R;
import org.afg.mathic.world.Equation;
import org.afg.mathic.world.Mode;
import org.afg.mathic.world.ModeGroup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MenuActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);

		new Load(this).execute();
	}

	@Override
	public void onResume() {
		super.onResume();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onPause() {
		super.onPause();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@SuppressLint("ClickableViewAccessibility")
	class Load extends AsyncTask<Context, Void, List<ModeGroup>> {
		private Context ctx;

		public Load(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		protected List<ModeGroup> doInBackground(Context... params) {
			List<ModeGroup> modeGroups = new LinkedList<ModeGroup>();

			Mode m1 = new Mode();
			Mode m2 = new Mode();
			Mode m3 = new Mode();
			Mode m4 = new Mode();
			Mode m5 = new Mode();
			Mode m6 = new Mode();
			Mode m7 = new Mode();
			Mode m8 = new Mode();
			Mode m9 = new Mode();
			Mode m10 = new Mode();

			m1.name = "easy";
			m1.timeChangeLevelRate = 3;
			m1.timeChangeRate = 1.3;

			m2.name = "medium";
			m2.timeChangeLevelRate = 5;
			m1.timeChangeRate = 1.2;
			m2.startTime = 900;
			m2.startMinNumbers = 5;
			m2.startMaxNumbers = 8;

			m3.name = "hard";
			m3.timeChangeLevelRate = 10;
			m3.startTime = 800;
			m3.startMinNumbers = 15;
			m3.startMaxNumbers = 20;

			m4.name = "normal";

			m5.name = "zen";
			m5.hasTime = false;

			m6.name = "concetrate";
			m6.timeChangeLevelRate = 10;
			m6.startTime = 600;
			m6.colorChanges = false;
			m6.startTextSize = 40;

			m7.name = "funny";
			m7.textSizeVariates = true;
			m7.textSizeRandom = true;
			m7.startTime = 2000;
			m7.timeChangeLevelRate = 10;
			m7.timeChangeRate = 1.2;

			m8.name = "luck";
			m8.hasMissingChar = true;

			m9.name = "kamikaze";
			m9.timeChangeLevelRate = 5;
			m9.timeChangeRate = 0.9;
			m9.startTime = 1500;
			m9.startMinNumbers = 5;
			m9.startMaxNumbers = 7;

			m10.name = "mitre";
			m10.timeChangeLevelRate = 1;
			m10.timeChangeRate = 0.5;
			m10.startTime = 100000;
			m10.startMinNumbers = 5;
			m10.startMaxNumbers = 7;

			ModeGroup mg1 = new ModeGroup();
			ModeGroup mg2 = new ModeGroup();
			ModeGroup mg3 = new ModeGroup();
			ModeGroup mg4 = new ModeGroup();
			ModeGroup mg5 = new ModeGroup();
			ModeGroup mg6 = new ModeGroup();

			mg1.modes.add(m1);
			mg1.modes.add(m2);
			mg1.modes.add(m3);
			mg2.modes.add(m4);
			mg3.modes.add(m5);
			mg4.modes.add(m6);
			mg4.modes.add(m7);
			mg5.modes.add(m8);
			mg6.modes.add(m9);
			mg6.modes.add(m10);

			modeGroups.add(mg1);
			modeGroups.add(mg6);
			modeGroups.add(mg2);
			modeGroups.add(mg3);
			modeGroups.add(mg4);
			modeGroups.add(mg5);

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				finish();
			}

			return modeGroups;
		}

		@Override
		protected void onPostExecute(List<ModeGroup> modeGroups) {
			setContentView(R.layout.menu_layout);
			LinearLayout container = (LinearLayout) findViewById(R.id.menu_container);

			for (ModeGroup modeGroup : modeGroups) {
				LinearLayout groupContainer = new LinearLayout(ctx);
				groupContainer.setOrientation(LinearLayout.HORIZONTAL);
				int size = modeGroup.modes.size();

				for (final Mode m : modeGroup.modes) {
					final int red = Equation.rand.nextInt(200);
					final int green = Equation.rand.nextInt(200);
					final int blue = Equation.rand.nextInt(200);

					final Button b = new Button(ctx);

					b.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, 1f / size));

					b.setText(Html.fromHtml("<b>" + m.name + "</b>"));
					b.setBackgroundColor(Color.rgb(red, green, blue));
					b.setTextSize(32f / ((float) (Math.log(size + 3) / Math
							.log(2)) - 1));
					b.setTextColor(Color.WHITE);
					b.setPadding(10, 17, 10, 20);

					b.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent t = new Intent(ctx, GameActivity.class);
							t.putExtra("#Mode", m);
							startActivity(t);
							overridePendingTransition(
									R.anim.abc_slide_in_bottom,
									R.anim.abc_slide_out_top);
						}
					});

					groupContainer.addView(b);
				}
				container.addView(groupContainer);
			}

			container.invalidate();
		}
	}
}
