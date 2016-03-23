package com.slava.emojicfc;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.slava.emojicfc.emoji.EmojiData;
import com.slava.emojicfc.emoji.EmojiGridPageFragment;

public class MainActivity extends AppCompatActivity {

    private EditText messageEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    private void createEmojiView(View v) {

        ImageView emoji_btn = (ImageView) findViewById(R.id.emoji_btn);
        LinearLayout linear_emoji_view = (LinearLayout) findViewById(R.id.linear_emoji_view);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_emoji);

        FrameLayout backSpace_btn = (FrameLayout) findViewById(R.id.emoji_frame_backspace);
        backSpace_btn.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler;
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    messageEdit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    handler.postDelayed(runnable, 50);
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (messageEdit.getText().length() == 0) {
                        return true;
                    } else if (handler == null) {
                        handler = new Handler();
                    }
                    messageEdit.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    handler.postDelayed(runnable, 500);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (handler == null) {
                        return true;
                    }
                    handler.removeCallbacks(runnable);
                    return true;
                }
                return false;
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.emoji_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        EmojiGridPageFragment.setListener(new EmojiGridPageFragment.Listener() {
            @Override
            public void onClickEmoji(String code) {
                messageEdit.getText().append(code);
            }
        });

        if (linear_emoji_view.getVisibility() == View.VISIBLE) {
            linear_emoji_view.setVisibility(View.GONE);
            emoji_btn.setImageResource(R.drawable.ic_emoji1);
            AndroidUtilities.showKeyboard(messageEdit);
            messageEdit.requestFocus();

        } else if (linear_emoji_view.getVisibility() == View.GONE && v.getId() == R.id.frame_emoji_btn) {
            if (viewPager.getAdapter() == null) {
                viewPager.setAdapter(new EmojiPagerAdapter(getSupportFragmentManager()));

                tabLayout.setupWithViewPager(viewPager);

                for (int i = 0; i < tabLayout.getTabCount(); i++) {

                    View view = getLayoutInflater().inflate(R.layout.item_emoji_icon, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.emoji_icon);

                    String resourceName = "ic_emoji" + i + "_selector";
                    int resourceID = getResources().getIdentifier(resourceName, "drawable",
                            getPackageName());

                    imageView.setImageResource(resourceID);
                    tabLayout.getTabAt(i).setCustomView(view);
                }

                tabLayout.getTabAt(1).select();
                tabLayout.getTabAt(0).select();
            }

            AndroidUtilities.hideKeyboard(messageEdit);
            emoji_btn.setImageResource(R.drawable.ic_emoji7);
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
            return EmojiData.emojiData.length + 1; // one page for recent emoji
        }
    }
}
