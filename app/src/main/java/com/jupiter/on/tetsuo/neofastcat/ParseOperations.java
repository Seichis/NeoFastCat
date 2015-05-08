package com.jupiter.on.tetsuo.neofastcat;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton to handle the operations with Parce, both single and multiplayer mode.For now its ok but in future should be separate
 *
 */
public class ParseOperations {
    private static ParseOperations mParseOperations = new ParseOperations();
    ParseQuery<ParseObject> query;
    ParseObject createInstance;

    public ParseOperations() {
    }

    public static ParseOperations getInstance() {
        return mParseOperations;
    }

    public void createGameInstance() {
        createInstance = new ParseObject("MultiplayerInstance");
        createInstance.put("Players", Players.getInstance().playersToJSON(Players.getInstance()));
        Log.i("players", "  " + Players.getInstance().getmPlayerMap().size());
        createInstance.saveInBackground();

    }

    public void uploadPlayerScorePersonalData(ParseUser player, int score, int rounds) {
        String tempScore;
        String tempGameid;

        tempGameid = Integer.toString(player.getInt("gameid"));
        tempScore = Integer.toString(player.getInt("score"));
        if (tempGameid == null || tempScore == null) {
            player.put("gameid", 0);
            player.put("score", 0);
        } else {
            score += Integer.parseInt(tempScore);
            rounds = rounds + Integer.parseInt(tempGameid);
            player.put("gameid", rounds);
            player.put("score", score);
        }
        player.saveInBackground();
    }

    public float getAveragePlayerScore(ParseUser player) {
        float average=0;
        String tempScore;
        String tempGameid;

        tempGameid = Integer.toString(player.getInt("gameid"));
        tempScore = Integer.toString(player.getInt("score"));
        if (tempGameid == null || tempScore == null) {
            //do nothing
        } else {
            average = Float.parseFloat(tempScore) / Float.parseFloat(tempGameid);
        }
        return average;
    }

    public void uploadPlayerScore(final String player, final int score) {
        query = ParseQuery.getQuery("MultiplayerInstance");
        query.whereMatches("Players", player);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> mGames, ParseException e) {

                if (e == null) {
                    for (ParseObject po : mGames) {
                        Players.getInstance().setJSONtoPlayers(po.getString("Players"));
                        Players.getInstance().addScore(player, score);
                        po.put("Players", Players.getInstance().playersToJSON(Players.getInstance()));
                        po.saveInBackground();
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void deleteGameInstance() {
        if (createInstance != null) {
            createInstance.deleteInBackground();
        }
    }
//
//    private ParseObject getCurrentGameInstance(String id) {
//        final ParseObject[] tmp = {new ParseObject("MultiplayerInstance")};
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("MultiplayerInstance");
//        query.getInBackground(id, new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
//                    tmp[0] = object;
//                    Log.i("parse obj", "Retrieved " + tmp[0].getObjectId());
//                } else {
//                    // something went wrong
//                    Log.i("parse obj", "ton poulo ");
//                }
//            }
//        });
//        return tmp[0];
//    }

    public void getWinnerMap(final String player) {
        final Map<String, Integer>[] map = new Map[]{new HashMap<>()};
        query = ParseQuery.getQuery("MultiplayerInstance");
        query.whereMatches("Players", player);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> mGames, ParseException e) {

                if (e == null) {
                    for (ParseObject po : mGames) {

                        Players.getInstance().getmPlayerMap().clear();
                        Log.d("winner", "cleared" + Players.getInstance().getmPlayerMap().size());

                        Players.getInstance().setJSONtoPlayers(po.getString("Players"));

                        Log.d("winner", "after" + Players.getInstance().getmPlayerMap());
                        map[0] = Players.getInstance().getmPlayerMap();


                    }
                    returnOrderedResults(map[0]);
                    // Get entries and sort them.


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        Log.d("winner", "before return" + map[0]);
    }

    private void returnOrderedResults(Map<String, Integer> results) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(results.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

// Put entries back in an ordered map.
        Map<String, Integer> orderedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entries) {
            orderedMap.put(entry.getKey(), entry.getValue());
        }
        ResultsActivity.setScoreTextView(orderedMap.toString());
    }

}
