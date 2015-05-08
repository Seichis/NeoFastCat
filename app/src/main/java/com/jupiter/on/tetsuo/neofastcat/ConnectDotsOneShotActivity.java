package com.jupiter.on.tetsuo.neofastcat;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.GridView;

import java.util.Timer;
import java.util.TimerTask;

import adapter.ImageAdapter;
import view.DrawingPanelOneShot;

/**
 * Activity that holds the game. This class along with the adapter and view packages are from another project I am currently working on.
 * Of course it is my work
 * The github repository is : https://github.com/Seichis/DataGathering.git
 */

public class ConnectDotsOneShotActivity extends Activity {
    //    Timer mTimerGame;
    Timer mTimerProgress;
    Handler mHandlerGame;
    DrawingPanelOneShot drawView;
    public static float second = 0;
    public static boolean timeToReset = false;
    int firstTimercount = 0;

    static GridView mGridView;
    static ImageAdapter mImageAdapter;
    public static int positionOfAnswerArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        positionOfAnswerArray = 0;
        setContentView(R.layout.activity_connect_dots_one_shot);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        drawView = (DrawingPanelOneShot) findViewById(R.id.drawPanel);
        DrawingPanelOneShot.score = 0;


        drawView.requestFocus();
        if (mTimerProgress != null) {
            mTimerProgress.cancel();
        } else {
            mTimerProgress = new Timer();
        }
        mTimerProgress.scheduleAtFixedRate(new ProgressBarTimerTask(), 0, 1000);
        mHandlerGame = new Handler();
        mGridView = (GridView) findViewById(R.id.answerImages);
        mImageAdapter = new ImageAdapter(this);
        mGridView.setAdapter(mImageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ProgressBarTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandlerGame.post(new Runnable() {
                @Override
                public void run() {
                    firstTimercount++;
                    drawView.invalidate();
                    Log.i("Timers", "  " + firstTimercount);
                    if (second > 15) {
                        mTimerProgress.cancel();
                        second = 0;
                        RegisterToGameActivity.playerScore += DrawingPanelOneShot.score;

                        RegisterToGameActivity.rounds++;
                        ConnectDotsOneShotActivity.this.finish();
                    }
                    second++;
                }
            });
        }
    }

    public static void setAdapterToShow() {
        mImageAdapter.notifyDataSetChanged();
    }
}