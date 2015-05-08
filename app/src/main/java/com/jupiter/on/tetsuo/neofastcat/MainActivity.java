package com.jupiter.on.tetsuo.neofastcat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.parse.ParseUser;

/**
 * Options for either single player or multiplayer.Also logout from this activity
 */

public class MainActivity extends Activity {

    ParseUser currentUser;
    Button baseModeButton, registerToGameButton,logoutButton,infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        logoutButton=(Button)findViewById(R.id.logout_button);
        infoButton=(Button)findViewById(R.id.main_info_button);
        baseModeButton = (Button) findViewById(R.id.base_mode_button);
        registerToGameButton = (Button) findViewById(R.id.register_button);
        currentUser = ParseUser.getCurrentUser();
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,InfoActivity.class));
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.logOut();
                MainActivity.this.finish();
                Toast.makeText(getApplicationContext(), "You were logged out", Toast.LENGTH_LONG).show();
            }
        });
        registerToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterToGameActivity.class));
            }
        });

        baseModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BaseModeActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);


    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

//    private void androidKeyForFB(){
//
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "Your package name",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//    }
}
