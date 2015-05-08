package com.jupiter.on.tetsuo.neofastcat;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Acts as a receiver for NFC.Also transmits but in not used for that.Creates the instance of the game that will start and when it finishes it deletes it.
 * This is done so that no game instance with the same player name exists. So 1 player can exist in 1 game instance so this way it is simple to find the player in a game.
 * Handle diffently if scale
 */
public class BaseModeActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback {
    public static boolean isGameOver = false;
    static int numberOfPlayers = 0;
    static boolean registeringOn = false;
    CountDownTimer mCountDownTimer;
    // parameters for NFC programming
    NfcAdapter mNfcAdapter;
    TextView infoView;
    Button startRegisteringButton, startGameButton, resultsButton;
    EditText inputNumOfPlayersEditText;
    TextView timer;
    PendingIntent mPendingIntent;
    SharedPreferences pref;
    TextView playerTextView;
    static List<String> playerStringList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_mode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        infoView = (TextView) findViewById(R.id.info_tv);
        timer = (TextView) findViewById(R.id.timer);
        playerStringList = new ArrayList<>();
        playerTextView = (TextView) findViewById(R.id.player_listview);
        pref = getSharedPreferences("NFCPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = pref.edit();
        startRegisteringButton = (Button) findViewById(R.id.start_register_button);
        startGameButton = (Button) findViewById(R.id.play_button);
        resultsButton = (Button) findViewById(R.id.results_button);
        inputNumOfPlayersEditText = (EditText) findViewById(R.id.input_num_players_et);
        inputNumOfPlayersEditText.setHint("i.e 4");
        startGameButton.setEnabled(false);
        resultsButton.setEnabled(false);
        accessNFC();
        startRegisteringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputNumOfPlayersEditText.getText().toString().isEmpty()) {
                    numberOfPlayers = Integer.parseInt(inputNumOfPlayersEditText.getText().toString());
                    edit.putInt("players", numberOfPlayers);
                    if (numberOfPlayers > 0) {
                        registeringOn = true;
                        startRegisteringButton.setEnabled(false);
                        startGameButton.setEnabled(true);
                    } else {
                        Toast.makeText(getApplicationContext(), "No game is played with 0 players!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Add the number of players!", Toast.LENGTH_LONG).show();
                }
            }
        });
        resultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BaseModeActivity.this, ResultsActivity.class));
            }
        });
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerStringList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No player registered yet!Touch the base device to register a player", Toast.LENGTH_LONG).show();
                } else {
                    registeringOn = false;
                    // startGameButton.setEnabled(false);
                    ParseOperations.getInstance().createGameInstance();
                    mCountDownTimer = new CountDownTimer(30000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) ((millisUntilFinished / 1000));
                            timer.setText(seconds + "  seconds ");
                        }

                        public void onFinish() {
                            //play annoying sound
                            resultsButton.setEnabled(true);
                            timer.setVisibility(View.INVISIBLE);
                        }
                    };
                    mCountDownTimer.start();
                    Toast.makeText(getApplicationContext(), "Get ready!The game will start in 30 seconds!!!Have fun!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void savePlayerName(String player) {

        Toast.makeText(getApplicationContext(), "" + player, Toast.LENGTH_LONG).show();
        Players.getInstance().addPlayer(player);
        if (!playerStringList.contains(player)) {
            playerStringList.add(player);
            playerTextView.append("\n" + (playerStringList.indexOf(player) + 1) + " : " + player);
        }
    }


    @Override
    public void onResume() {
        super.onResume();


        if (!isGameOver) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                processIntent(getIntent());
            }
        } else {
            BaseModeActivity.this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParseOperations.getInstance().deleteGameInstance();
        isGameOver = false;
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        savePlayerName(new String(msg.getRecords()[0].getPayload()));

    }

    private void accessNFC() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "No available NFC adapter", Toast.LENGTH_LONG).show();
            return;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseModeActivity.this.finish();
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return null;
    }
}
