package com.slava.emojicfc.emoji;

import com.slava.emojicfc.MainActivity;

import java.util.HashMap;

public class Emoji {

    public static HashMap<CharSequence, Integer> hashMap = new HashMap<>();

    static {

        String resourceName;
        int n = 1;

        for (int i = 0; i < EmojiData.emojiData.length; i++) {
            for (int k = 0; k < EmojiData.emojiData[i].length; k++) {
                resourceName = "emoji_" + n;
                int resourceID = MainActivity.applicationContext.getResources().getIdentifier(resourceName, "drawable",
                        MainActivity.applicationContext.getPackageName());

                hashMap.put(EmojiData.emojiData[i][k], resourceID);
                n++;
            }
        }
    }
}
