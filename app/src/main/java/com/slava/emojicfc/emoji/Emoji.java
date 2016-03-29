package com.slava.emojicfc.emoji;

import android.content.SharedPreferences;
import android.util.Log;

import com.slava.emojicfc.App;

import java.util.ArrayList;
import java.util.HashMap;

public class Emoji {

    public static final HashMap<CharSequence, Integer> hashMap = new HashMap<>();
    //public static final HashMap<CharSequence, Integer> recentEmoji = new HashMap<>();
    public static final ArrayList<String> recentEmoji = new ArrayList<>();
    public static final SharedPreferences sharedPreferencesEmoji = App.applicationContext.getSharedPreferences("recentEmoji", App.MODE_PRIVATE);

    static {

        String resourceName;
        int n = 1;

        for (int i = 0; i < EmojiData.emojiData.length; i++) {
            for (int k = 0; k < EmojiData.emojiData[i].length; k++) {
                resourceName = "emoji_" + n;
                int resourceID = App.applicationContext.getResources().getIdentifier(resourceName, "drawable",
                        App.applicationContext.getPackageName());

                hashMap.put(EmojiData.emojiData[i][k], resourceID);
                n++;
            }
        }

        for (String s : sharedPreferencesEmoji.getAll().keySet()) {
            recentEmoji.add(s);
        }

    }

    public static void addRecentEmoji(String code) {
        SharedPreferences.Editor editor = sharedPreferencesEmoji.edit();

        int count;
        if (sharedPreferencesEmoji.contains(code)) {
            count = sharedPreferencesEmoji.getInt(code, 0);
            editor.putInt(code, ++count);
        } else {
            count = 0;
            editor.putInt(code, count);
        }
        Log.d("LOG", "LOG = " + count);
        editor.apply();
    }
}
