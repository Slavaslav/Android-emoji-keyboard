package com.slava.emojicfc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;


public class EmojiGridPageFragment extends Fragment {

    public EmojiGridPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emoji_grid_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView gridView = (GridView) view.findViewById(R.id.emoji_grid_container);
        gridView.setAdapter(new EmojiGridAdapter());

    }

    private class EmojiGridAdapter extends BaseAdapter {

        private final int emojiPage;

        public EmojiGridAdapter() {
            emojiPage = 0;
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
