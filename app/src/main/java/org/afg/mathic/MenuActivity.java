package org.afg.mathic;

import java.util.LinkedList;
import java.util.List;

import org.afg.mathic.util.MediaPlayerManager;
import org.afg.mathic.world.Activities.*;
import org.afg.mathic.R;
import org.afg.mathic.world.Modes.ColorMode;
import org.afg.mathic.world.Modes.MathematicMode;
import org.afg.mathic.world.Modes.MemoryMode;
import org.afg.mathic.world.Modes.Mode;
import org.afg.mathic.world.ModeGroup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActivity extends Activity {

	public static boolean mIsSplashLoaded = false;
	public static List<ModeGroup> mModeGroups;

    public void mute(View v){
        MediaPlayerManager.toggleMute();
		if(MediaPlayerManager.isMuted()) {
			((Button) v).setBackgroundResource(R.drawable.mute);
		}else{
			((Button) v).setBackgroundResource(R.drawable.unmute);
		}

        SharedPreferences.Editor edit = getSharedPreferences("sound", MODE_PRIVATE).edit();
        edit.putBoolean(MediaPlayerManager.isMutedSoundKey, MediaPlayerManager.isMuted());
        edit.commit();
    }

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
	}

	@Override
	public void onResume() {
		super.onResume();

		new Load(this).execute();

		if(!MediaPlayerManager.isPlaying()){
			MediaPlayerManager.play(R.raw.happy, this);
		}

		if(mIsSplashLoaded) {
			if (MediaPlayerManager.isMuted()) {
				((Button) findViewById(R.id.mute)).setBackgroundResource(R.drawable.mute);
			} else {
				((Button) findViewById(R.id.mute)).setBackgroundResource(R.drawable.unmute);
			}
		}else{
            boolean isMutedFromSave = getSharedPreferences("sound", MODE_PRIVATE).getBoolean(MediaPlayerManager.isMutedSoundKey, false);
            if(isMutedFromSave){
                MediaPlayerManager.mute();
            }else{
                MediaPlayerManager.unmute();
            }
        }
	}

	public void onPause(){
		super.onPause();
		MediaPlayerManager.stop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

        mIsSplashLoaded = false;
	}

	@SuppressLint("ClickableViewAccessibility")
	class Load extends AsyncTask<Context, Void, List<ModeGroup>> {
		private Context ctx;

		public Load(Context ctx) {
			this.ctx = ctx;
        }

		@Override
		protected List<ModeGroup> doInBackground(Context... params) {
			mModeGroups = new LinkedList<ModeGroup>();

            ModeGroup mg1 = new ModeGroup();
            ModeGroup mg2 = new ModeGroup();
            ModeGroup mg3 = new ModeGroup();

            mg1.name = "Mathematic";
            mg2.name = "Colors";
            mg3.name = "Memory";

			MathematicMode m1 = new MathematicMode();
			MathematicMode m2 = new MathematicMode();
			MathematicMode m3 = new MathematicMode();
			ColorMode m4 = new ColorMode();
			ColorMode m5 = new ColorMode();
			ColorMode m6 = new ColorMode();
			MemoryMode m7 = new MemoryMode();
			MemoryMode m8 = new MemoryMode();
			MemoryMode m9 = new MemoryMode();

            m1.name = "easy";
            m1.highscoreKey = String.format("%s_%s", mg1.name, m1.name);
			m1.activityClass = MathematicGameActivity.class;
			m1.timeChangeLevelRate = 5;
			m1.timeChangeRate = 1.05;
			m1.startTime = 2000;
			m1.startMinNumbers = 1;
			m1.startMaxNumbers = 2;

			m2.name = "normal";
            m2.highscoreKey = String.format("%s_%s", mg1.name, m2.name);
			m2.activityClass = MathematicGameActivity.class;
			m2.timeChangeLevelRate = 4;
			m2.timeChangeRate = 1.075;
			m2.startTime = 1650;
			m2.startMinNumbers = 5;
			m2.startMaxNumbers = 8;

			m3.name = "hard";
            m3.highscoreKey = String.format("%s_%s", mg1.name, m3.name);
			m3.activityClass = MathematicGameActivity.class;
			m3.timeChangeLevelRate = 3;
			m3.timeChangeRate = 1.1;
			m3.startTime = 1300;
			m3.startMinNumbers = 10;
			m3.startMaxNumbers = 15;

			m4.name = "easy";
            m4.highscoreKey = String.format("%s_%s", mg2.name, m4.name);
			m4.activityClass = ColorGameActivity.class;
			m4.timeChangeLevelRate = 5;
			m4.timeChangeRate = 1.05;
			m4.startTime = 2000;
			m4.numberOfColors = 6;

			m5.name = "normal";
            m5.highscoreKey = String.format("%s_%s", mg2.name, m5.name);
			m5.activityClass = ColorGameActivity.class;
			m5.timeChangeLevelRate = 4;
			m5.timeChangeRate = 1.075;
			m5.startTime = 1650;
			m5.numberOfColors = 8;

			m6.name = "hard";
            m6.highscoreKey = String.format("%s_%s", mg2.name, m6.name);
			m6.activityClass = ColorGameActivity.class;
			m6.timeChangeLevelRate = 3;
			m6.timeChangeRate = 1.1;
			m6.startTime = 1300;
			m6.numberOfColors = 11;

			m7.name = "easy";
            m7.highscoreKey = String.format("%s_%s", mg3.name, m7.name);
			m7.activityClass = MemoryGameActivity.class;
			m7.startTime = 5000;
			m7.timeChangeLevelRate = 10;
			m7.timeChangeRate = 0.9;
			m7.trueColor.color = Color.WHITE;
			m7.falseColor.color = Color.BLACK;
			m7.fadeTime = 800;

			m8.name = "normal";
            m8.highscoreKey = String.format("%s_%s", mg3.name, m8.name);
			m8.activityClass = MemoryGameActivity.class;
			m8.startTime = 4200;
			m8.timeChangeLevelRate = 6;
			m8.timeChangeRate = 0.925;
			m8.trueColor.color = Color.WHITE;
			m8.falseColor.color = Color.BLACK;
			m8.fadeTime = 700;

			m9.name = "hard";
            m9.highscoreKey = String.format("%s_%s", mg3.name, m9.name);
			m9.activityClass = MemoryGameActivity.class;
			m9.startTime = 3400;
			m9.timeChangeLevelRate = 2;
			m9.timeChangeRate = 0.95;
			m9.trueColor.color = Color.WHITE;
			m9.falseColor.color = Color.BLACK;
			m9.fadeTime = 600;
			m9.hasBorders = false;

			mg1.modes.add(m1);
			mg1.modes.add(m2);
			mg1.modes.add(m3);
			mg2.modes.add(m4);
			mg2.modes.add(m5);
			mg2.modes.add(m6);
			mg3.modes.add(m7);
			mg3.modes.add(m8);
			mg3.modes.add(m9);

			mModeGroups.add(mg1);
			mModeGroups.add(mg2);
			mModeGroups.add(mg3);

			try {
				if(!mIsSplashLoaded) {
					Thread.sleep(3000);
				}
			} catch (InterruptedException e) {
				finish();
			}

			return mModeGroups;
		}

		@Override
		protected void onPostExecute(List<ModeGroup> modeGroups) {
			setContentView(R.layout.menu_layout);
			MenuActivity.mIsSplashLoaded = true;

			if(MediaPlayerManager.isMuted()) {
				((Button) findViewById(R.id.mute)).setBackgroundResource(R.drawable.mute);
			}else{
				((Button) findViewById(R.id.mute)).setBackgroundResource(R.drawable.unmute);
			}

			final LinearLayout container = (LinearLayout) findViewById(R.id.menu_container);
			int red = 225;
			int green = 225;
			int blue = 225;

			for (final ModeGroup modeGroup : modeGroups) {

				LinearLayout titleContainer = new LinearLayout(ctx);
				titleContainer.setOrientation(LinearLayout.VERTICAL);

				final LinearLayout groupContainer = new LinearLayout(ctx);
				groupContainer.setOrientation(LinearLayout.HORIZONTAL);
				groupContainer.setVisibility(LinearLayout.GONE);
				int size = modeGroup.modes.size();

				Button tv = new Button(ctx);
				tv.setText(modeGroup.name);
				tv.setTextSize(24f);
				tv.setPadding(0, 31, 0, 31);
				tv.setTextColor(Color.rgb(255, 255, 255));
				tv.setBackgroundColor(Color.rgb(30, 30, 30));
				tv.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
						1f));

				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						v.setOnClickListener(null);
						Animation anim = AnimationUtils.loadAnimation(ctx,
								R.anim.fadeout);
						anim.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								v.setVisibility(Button.GONE);

								Animation anim = AnimationUtils.loadAnimation(
										ctx, R.anim.fadein);
								groupContainer
										.setVisibility(LinearLayout.VISIBLE);
								groupContainer.startAnimation(anim);
							}
						});
						v.startAnimation(anim);
					}
				});

				titleContainer.addView(tv);
				titleContainer.addView(groupContainer);
				titleContainer.addView(new TextView(ctx));

				for (final Mode m : modeGroup.modes) {

					final Button b = new Button(ctx);

					b.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, 1f / size));

					b.setText(Html.fromHtml("<b>" + m.name + "</b>"));
					b.setBackgroundColor(Color.rgb(red, green, blue));
					b.setTextColor(Color
							.rgb(255 - red, 255 - green, 255 - blue));
					b.setTextSize(32f / ((float) (Math.log(size + 3) / Math
							.log(2)) - 1));
					b.setPadding(10, 37, 10, 40);

					b.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
                            MediaPlayerManager.stop();
							Intent t = new Intent(ctx, m.activityClass);
							t.putExtra("#Mode", m);
							startActivity(t);
							overridePendingTransition(
									R.anim.abc_slide_in_bottom,
									R.anim.abc_slide_out_top);
						}
					});

					groupContainer.addView(b);

					red -= 255;
					green -= 255;
					blue -= 255;
					red *= -1;
					green *= -1;
					blue *= -1;
				}
				container.addView(titleContainer);
			}

			Button tv = new Button(ctx);
			tv.setText("Highscores");
			tv.setTextSize(24f);
			tv.setPadding(31, 31, 31, 31);
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.rgb(255, 255, 255));
			tv.setBackgroundColor(Color.rgb(30, 30, 30));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			tv.setLayoutParams(params);

			tv.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v){
					startActivity(new Intent(ctx, HighscoreActivity.class));
					overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
				}
			});

			container.addView(tv);
			container.invalidate();
		}
	}
}
