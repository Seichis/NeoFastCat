package com.jupiter.on.tetsuo.neofastcat;

import android.app.Activity;
import android.os.Bundle;

/**
Info....not completed but you get the point
 */
public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
