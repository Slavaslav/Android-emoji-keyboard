package com.slava.emojicfc.emoji;

import com.slava.emojicfc.App;

import java.util.HashMap;

class Emoji {

    public static final HashMap<CharSequence, Integer> hashMap = new HashMap<>();

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
}
