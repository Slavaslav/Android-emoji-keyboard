package com.slava.emojicfc;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class EmojiView extends FrameLayout {

    private final ArrayList<GridView> views = new ArrayList<>();

    public EmojiView(Context context) {
        super(context);

        for (int i = 0; i < 1; i++) {

            GridView gridView = new GridView(context);
            gridView.setColumnWidth(AndroidUtilities.dp(45));
            gridView.setNumColumns(GridView.AUTO_FIT);
            views.add(gridView);

            EmojiGridAdapter emojiGridAdapter = new EmojiGridAdapter(i);
            gridView.setAdapter(emojiGridAdapter);

            FrameLayout fl = new FrameLayout(context);
            fl.addView(gridView);

            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, AndroidUtilities.dp(200));
            layoutParams.topMargin = AndroidUtilities.dp(48);
            addView(fl, layoutParams);

        }

        ViewPager viewPager = new ViewPager(context);

    }

    private class EmojiPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }

    private class EmojiGridAdapter extends BaseAdapter {

        private final int emojiPage;

        public EmojiGridAdapter(int page) {
            emojiPage = page;
        }


        @Override
        public int getCount() {
            return EmojiData.dataColored[emojiPage].length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           /* ImageView imageView = (ImageView) convertView;
            if (imageView == null) {
                imageView = new ImageView(getContext());
            }*/

            TextView textView = (TextView) convertView;
            if (textView == null) {
                textView = new TextView(getContext());
            }

            //String color = "\uD83C\uDFFB";
            String coloredCode;
            coloredCode = EmojiData.dataColored[emojiPage][position];

            textView.setText(coloredCode);
            textView.setGravity(Gravity.CENTER);
            return textView;

            /*imageView.setImageDrawable(Emoji.getEmojiImage(coloredCode));
            return imageView;*/
        }
    }

}
