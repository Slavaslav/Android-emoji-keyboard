package com.slava.emojicfc.emoji;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.slava.emojicfc.R;


public class EmojiGridPageFragment extends Fragment {

    private static Listener listener;
    private int page;
    private EmojiGridAdapter emojiGridAdapter;

    public EmojiGridPageFragment() {
        // Required empty public constructor
    }

    public static void setListener(Listener value) {
        listener = value;
    }

    public void createInstance(int page) {
        this.page = page;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (page == -1 && Emoji.recentEmoji.size() == 0) {
            return inflater.inflate(R.layout.emoji_no_recent, container, false);
        } else {
            return inflater.inflate(R.layout.emoji_grid_view, container, false);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emojiGridAdapter = new EmojiGridAdapter(page);
        if (page == -1) {
            listener.createListGridAdapter(emojiGridAdapter);
        }

        if (page == -1 && Emoji.recentEmoji.size() == 0) {
            // empty
        } else {
            GridView gridView = (GridView) view.findViewById(R.id.emoji_grid_container);
            new EmojiGridHandler().execute(gridView);
        }

    }

    public interface Listener {
        void onClickEmoji(String code);

        void createListGridAdapter(EmojiGridAdapter emojiGridAdapter);
    }

    public class EmojiGridAdapter extends BaseAdapter {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        private final int emojiPage;

        public EmojiGridAdapter(int emojiPage) {
            this.emojiPage = emojiPage;
        }

        @Override
        public int getCount() {
            if (emojiPage == -1) {
                return Emoji.recentEmoji.size();
            } else {
                return EmojiData.emojiData[emojiPage].length;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_emoji_image, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.emoji_icon_image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String code = null;
            if (emojiPage == -1) {
                viewHolder.imageView.setImageResource(Emoji.hashMap.get(Emoji.recentEmoji.get(position)));


            } else {
                code = EmojiData.emojiData[emojiPage][position];
                viewHolder.imageView.setImageResource(Emoji.hashMap.get(code));
            }
            final String finalCode = code;
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emojiPage == -1) {
                        listener.onClickEmoji(Emoji.recentEmoji.get(position));
                    } else {
                        listener.onClickEmoji(finalCode);
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
        }
    }

    private class EmojiGridHandler extends AsyncTask<GridView, Void, GridView> {

        @Override
        protected GridView doInBackground(GridView... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(GridView g) {
            super.onPostExecute(g);
            g.setAdapter(emojiGridAdapter);
        }
    }
}
