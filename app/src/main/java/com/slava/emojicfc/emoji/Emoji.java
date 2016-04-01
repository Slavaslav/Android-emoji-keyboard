package com.slava.emojicfc.emoji;

import android.content.SharedPreferences;

import com.slava.emojicfc.App;

import java.util.ArrayList;
import java.util.HashMap;

public class Emoji {

    public static final HashMap<CharSequence, Integer> hashMap = new HashMap<>();
    public static final SharedPreferences sharedPreferencesEmoji = App.applicationContext.getSharedPreferences("recentEmoji", App.MODE_PRIVATE);
    public static final ArrayList<String> recentEmoji = new ArrayList<>();

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
        editor.apply();
    }

    public static String getRecentEmoji(int position) {

        if (recentEmoji.size() != sharedPreferencesEmoji.getAll().size()) {
            recentEmoji.clear();
            for (String s : sharedPreferencesEmoji.getAll().keySet()) {
                recentEmoji.add(s);
            }
        }
        return recentEmoji.get(position);
    }
}
