package com.slava.emojicfc;

import java.util.HashMap;

public class Emoji {

    public static HashMap<CharSequence, Integer> hashMap = new HashMap<>();

    static {
        for (int i = 0; i < EmojiData.emojiData.length; i++) {
            for (int k = 0; k < EmojiData.emojiData[i].length; k++)
                hashMap.put(EmojiData.emojiData[i][k], R.drawable.emoji_1f604);
        }
    }

}
