package com.jupiter.on.tetsuo.neofastcat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;

/**
 * Initialize Parse, Facebook and Twitter.Authentication
 */
public class NFCApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "uerDzxRpV1kyEzPdSDqA10XJb8m1oSQiCmpXCVci", "U0K27LprwJIVmWl2Xh3WRsjvhxUxVbbXm20YABzD");

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        ParseFacebookUtils.initialize(this);

        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
    }
}