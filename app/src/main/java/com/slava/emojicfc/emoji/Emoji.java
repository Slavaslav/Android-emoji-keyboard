package com.slava.emojicfc.emoji;

import android.content.SharedPreferences;

import com.slava.emojicfc.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Emoji {

    public static final HashMap<CharSequence, Integer> hashMap = new HashMap<>();
    public static final SharedPreferences sharedPreferencesEmoji = App.applicationContext.getSharedPreferences("emoji", App.MODE_PRIVATE);
    public static final ArrayList<String> recentEmoji = new ArrayList<>();
    private static final HashMap<CharSequence, Integer> recentEmojiMap = new HashMap<>();

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

        if (sharedPreferencesEmoji.contains("emojis")) {
            String s = sharedPreferencesEmoji.getString("emojis", "");
            if (s.length() > 0) {
                String[] s1 = s.split(",");
                for (String ss : s1) {
                    String[] s2 = ss.split("=");
                    recentEmojiMap.put(s2[0], Integer.parseInt(s2[1]));
                    recentEmoji.add(s2[0]);
                }
                sortEmoji();
            }
        }
    }

    public static void addRecentEmoji(String code) {

        SharedPreferences.Editor editor = sharedPreferencesEmoji.edit();

        int count;
        if (recentEmojiMap.containsKey(code)) {
            count = recentEmojiMap.get(code);
            recentEmojiMap.put(code, ++count);
        } else {
            count = 0;
            recentEmojiMap.put(code, count);
        }

        if (!recentEmoji.contains(code)) {
            recentEmoji.add(code);
        }

        sortEmoji();
        //

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry entry : recentEmojiMap.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
        }
        editor.putString("emojis", stringBuilder.toString()).apply();

    }

    private static void sortEmoji() {

        Collections.sort(recentEmoji, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                Integer count1 = recentEmojiMap.get(lhs);
                Integer count2 = recentEmojiMap.get(rhs);

                if (count1 > count2) {
                    return -1;
                } else if (count1 < count2) {
                    return 1;
                }
                return 0;
            }
        });

    }
}
