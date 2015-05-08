package com.jupiter.on.tetsuo.neofastcat;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Singleton to handle players. An object to communicate via Parse
 */

public class Players {

    private Map<String, Integer> mPlayerMap = new HashMap<>();
    private static Players mPlayers = new Players();

    public Players() {

    }

    public static Players getInstance() {
        return mPlayers;
    }

    public void addPlayer(String player) {
        getmPlayerMap().put(player, 0);
    }

    public Map<String, Integer> getmPlayerMap() {
        return this.mPlayerMap;
    }

    public void addScore(String player, int score) {
        getmPlayerMap().put(player, score);
    }

    public String playersToJSON(Players p) {
        Gson gson = new GsonBuilder().create();
        String JSONtemp = (gson.toJson(p));
        return JSONtemp;
    }

    public void setJSONtoPlayers(String players) {
        Gson gson = new GsonBuilder().create();
        mPlayers = gson.fromJson(players, Players.class);
    }

}
