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

    private int page;

    public EmojiGridPageFragment() {
        // Required empty public constructor
    }

    public void createInstance(int page) {
        this.page = page;
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
        gridView.setAdapter(new EmojiGridAdapter(page));

    }

    private class EmojiGridAdapter extends BaseAdapter {

        private int emojiPage;

        public EmojiGridAdapter(int emojiPage) {
            this.emojiPage = emojiPage;
        }

        @Override
        public int getCount() {
            if (emojiPage == -1) {
                return 1;
            } else {
                return EmojiData.data[emojiPage - 1].length;
            }
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

            TextView textView = (TextView) convertView;
            if (textView == null) {
                textView = new TextView(getContext());
            }
            String coloredCode;


            if (emojiPage == -1) {
                //recent emoji
                textView.setText("0");
            } else {
                coloredCode = EmojiData.data[emojiPage - 1][position];
                textView.setText(coloredCode);
            }

            textView.setGravity(Gravity.CENTER);
            return textView;
        }
    }

}
