package com.slava.emojicfc.emoji;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

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

        if (recentEmoji.size() > 50) {
            int i = recentEmoji.size() - 2;
            recentEmojiMap.remove(recentEmoji.get(i));
            recentEmoji.remove(i);
        }

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

    public static Spannable handleStringEmoji(String s, Paint.FontMetricsInt fontMetrics, int q) {
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(s);

        if (q == 0) {
            int firstIndex = s.indexOf(s);
            int lastIndex = firstIndex + s.length();

            Drawable drawable = ContextCompat.getDrawable(App.applicationContext, Emoji.hashMap.get(s));
            EmojiSpan emojiSpan = new EmojiSpan(drawable, fontMetrics);
            spannable.setSpan(emojiSpan, firstIndex, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (q == 1) {

            for (int i = 0; i < EmojiData.emojiData.length; i++) {
                for (int k = 0; k < EmojiData.emojiData[i].length; k++) {

                    String code = EmojiData.emojiData[i][k];

                    if (s.contains(code)) {
                        int firstIndex = s.indexOf(code);
                        int lastIndex = firstIndex + code.length();

                        Drawable drawable = ContextCompat.getDrawable(App.applicationContext, Emoji.hashMap.get(code));
                        EmojiSpan emojiSpan = new EmojiSpan(drawable, fontMetrics);
                        spannable.setSpan(emojiSpan, firstIndex, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        s = s.replaceFirst(code, "  ");
                        k--;
                    }
                }
            }
        }
        return spannable;
    }

    private static class EmojiSpan extends ImageSpan {
        final Paint.FontMetricsInt fontMetrics;

        public EmojiSpan(Drawable d, Paint.FontMetricsInt fm) {
            super(d, DynamicDrawableSpan.ALIGN_BOTTOM);
            fontMetrics = fm;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {

            if (fm == null) {
                fm = new Paint.FontMetricsInt();
            }
            int size = Math.abs(fontMetrics.descent) + Math.abs(fontMetrics.ascent);

            fm.ascent = fontMetrics.ascent;
            fm.descent = fontMetrics.descent;
            fm.top = fontMetrics.top;
            fm.bottom = fontMetrics.bottom;

            if (getDrawable() != null) {
                getDrawable().setBounds(0, 0, size, size);
            }

            return size;
        }
    }
}
