package com.jupiter.on.tetsuo.neofastcat;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import static android.nfc.NdefRecord.createMime;

/**
 * Used as the activity to transmit the player name to the base and the activity to start playing the game.
 * After the players have agreed to end the game they must press the gameover button to upload the score and see the results from the base
 */
public class RegisterToGameActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    // parameters for NFC programming
    NfcAdapter mNfcAdapter;
    private static final int MESSAGE_SENT = 1;
    TextView tv;
    String playerName;
    Button playButton, gameOverButton;
    public static boolean isRegisteredToGame = false;
    public static int playerScore;
    public static int rounds;
    ParseUser currentPlayerObj;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_to_game);
        tv = (TextView) findViewById(R.id.register_tv);
        currentPlayerObj = ParseUser.getCurrentUser();
        playerName = currentPlayerObj.getString("name");
        tv.append(playerName + "\n" + "Average Score before the current game: " +String.valueOf(ParseOperations.getInstance().getAveragePlayerScore(currentPlayerObj)));
        playerScore = 0;
        rounds=0;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "No available NFC adapter", Toast.LENGTH_LONG).show();
            return;
        }

        mNfcAdapter.setNdefPushMessageCallback(this, this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        playButton = (Button) findViewById(R.id.play_button);
        gameOverButton = (Button) findViewById(R.id.game_over_button);
        gameOverButton.setEnabled(false);
        // playButton.setEnabled(false);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterToGameActivity.this, ConnectDotsOneShotActivity.class));
                gameOverButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Have fun!!", Toast.LENGTH_SHORT).show();
            }
        });
        gameOverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseOperations.getInstance().uploadPlayerScore(playerName, playerScore);
                ParseOperations.getInstance().uploadPlayerScorePersonalData(currentPlayerObj, playerScore,rounds);
                RegisterToGameActivity.this.finish();

            }
        });

        if (isRegisteredToGame) {
            playButton.setEnabled(true);

        }

    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        NdefMessage msg = new NdefMessage(
                new NdefRecord[]{createMime(
                        "application/registerplayer", playerName.getBytes())
                        , NdefRecord.createApplicationRecord("com.jupiter.on.tetsuo.neofastcat")
                });
        return msg;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();

    }

    /**
     * This handler receives a message from onNdefPushComplete
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SENT:
                    playButton.setEnabled(true);
                    isRegisteredToGame = true;
                    Toast.makeText(getApplicationContext(), playerName, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}