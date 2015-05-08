package com.jupiter.on.tetsuo.neofastcat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity for the results after the game is over. In order to have results the Gameover button must be pressed after finishing the game by the players
 */

public class ResultsActivity extends Activity {
    Set<String> mapStringSet;
    static TextView resultsTv;
    List<String> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        mapStringSet = Players.getInstance().getmPlayerMap().keySet();
        playerList = new ArrayList<>(mapStringSet);
        resultsTv = (TextView) findViewById(R.id.results_tv);
        ParseOperations.getInstance().getWinnerMap(playerList.get(playerList.size() - 1));
    }
    
    public static void setScoreTextView(String results) {
        CharSequence mCharSequence = results.subSequence(1, results.length() - 1);
        Pattern p = Pattern.compile(",");
        Matcher m = p.matcher(mCharSequence);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "\n");
        }
        m.appendTail(sb);
        results = sb.toString();
        resultsTv.append("\n" + results);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseModeActivity.isGameOver = true;
    }
}
