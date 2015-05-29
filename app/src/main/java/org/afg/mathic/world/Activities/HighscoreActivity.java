package org.afg.mathic.world.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.afg.mathic.MenuActivity;
import org.afg.mathic.R;
import org.afg.mathic.util.MediaPlayerManager;
import org.afg.mathic.world.ModeGroup;
import org.afg.mathic.world.Modes.Mode;

/**
 * Created by vojda_000 on 28-May-15.
 */
public class HighscoreActivity extends Activity{

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.highscore_layout);

        if(MediaPlayerManager.isMuted()) {
            ((Button) findViewById(R.id.mute)).setBackgroundResource(R.drawable.mute);
        }else{
            ((Button) findViewById(R.id.mute)).setBackgroundResource(R.drawable.unmute);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences highscores = this.getSharedPreferences("highscore", MODE_PRIVATE);

        for (ModeGroup mg : MenuActivity.mModeGroups){
            addHighscoreTitle(mg.name);
            for (Mode m : mg.modes){
                int score = highscores.getInt(m.highscoreKey, 0);
                addHighscore(m.name, score);
            }
        }
    }

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

    public void addHighscore(String name, int score){
        LinearLayout mHighscoreContainer = (LinearLayout)findViewById(R.id.highscore_container);

        LinearLayout rowContainer = new LinearLayout(this);
        rowContainer.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextSize(20);
        tvName.setPadding(25, 0, 0, 0);
        tvName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        tvName.setTextColor(Color.BLACK);
        rowContainer.addView(tvName);

        TextView tvScore = new TextView(this);
        tvScore.setText(score + "");
        tvScore.setTextSize(20);
        tvScore.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        tvScore.setTextColor(Color.BLACK);
        tvScore.setGravity(Gravity.RIGHT);
        rowContainer.addView(tvScore);

        mHighscoreContainer.addView(rowContainer);
    }

    public void addHighscoreTitle(String title){
        LinearLayout mHighscoreContainer = (LinearLayout)findViewById(R.id.highscore_container);

        LinearLayout rowContainer = new LinearLayout(this);
        rowContainer.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setTextSize(25);
        tvTitle.setPadding(0, 30, 0, 0);
        tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        tvTitle.setTextColor(Color.BLACK);
        rowContainer.addView(tvTitle);
        mHighscoreContainer.addView(rowContainer);
    }
}
