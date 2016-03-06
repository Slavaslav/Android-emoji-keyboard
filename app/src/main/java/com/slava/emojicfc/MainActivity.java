package com.slava.emojicfc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public static volatile Context applicationContext;
    private EditText messageEdit;
    private ImageView emoji_btn;
    private LinearLayout linear_emoji_view;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationContext = getApplicationContext();

        emoji_btn = (ImageView) findViewById(R.id.emoji_btn);
        linear_emoji_view = (LinearLayout) findViewById(R.id.linear_emoji_view);

        FrameLayout frame_emoji_btn = (FrameLayout) findViewById(R.id.frame_emoji_btn);
        frame_emoji_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEmojiView(v);
            }
        });

        messageEdit = (EditText) findViewById(R.id.message_edit);
        messageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEmojiView(v);
            }
        });
        messageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                createEmojiView(v);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.emoji_pager);

    }

    private void createEmojiView(View v) {

        if (linear_emoji_view.getVisibility() == View.VISIBLE) {
            linear_emoji_view.setVisibility(View.GONE);
            emoji_btn.setBackgroundResource(R.drawable.emoji_button);
            AndroidUtilities.showKeyboard(messageEdit);
            messageEdit.requestFocus();

        } else if (linear_emoji_view.getVisibility() == View.GONE && v.getId() == R.id.frame_emoji_btn) {
            if (viewPager.getAdapter() == null) {
                viewPager.setAdapter(new EmojiPagerAdapter(getSupportFragmentManager()));
            }

            AndroidUtilities.hideKeyboard(messageEdit);
            emoji_btn.setBackgroundResource(R.drawable.ic_keyboard_emoji);
            linear_emoji_view.setVisibility(View.VISIBLE);

        }
    }

    private class EmojiPagerAdapter extends FragmentPagerAdapter {

        public EmojiPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int page) {
            if (page == 0) {
                page = -1;
            } else {
                page = page - 1;
            }
            EmojiGridPageFragment emojiGridPageFragment = new EmojiGridPageFragment();
            emojiGridPageFragment.createInstance(page);

            return emojiGridPageFragment;
        }

        @Override
        public int getCount() {
            return EmojiData.data.length + 1;
        }
    }
}
