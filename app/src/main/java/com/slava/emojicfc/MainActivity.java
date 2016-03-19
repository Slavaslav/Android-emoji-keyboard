package com.slava.emojicfc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static volatile Context applicationContext;
    private final ArrayList<View> viewsEmojiLine = new ArrayList<>();
    private final ArrayList<ImageView> viewsEmojiImage = new ArrayList<>();
    private EditText messageEdit;
    private ViewPager viewPager;
    private int emoji_previous_page = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationContext = getApplicationContext();

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

        FrameLayout backSpace_btn = (FrameLayout) findViewById(R.id.emoji_frame_icon6);
        backSpace_btn.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler;
            Runnable runnable = new Runnable() {
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
                    handler.removeCallbacks(runnable);
                    return true;
                }
                return false;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.emoji_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (emoji_previous_page == position)
                    return;

                //set style unselected item
                String resourceName = "ic_emoji" + emoji_previous_page;
                int resourceID = getResources().getIdentifier(resourceName, "drawable",
                        getPackageName());
                viewsEmojiImage.get(emoji_previous_page).setImageResource(resourceID);

                ViewGroup.LayoutParams layoutParams = viewsEmojiLine.get(emoji_previous_page).getLayoutParams();
                layoutParams.height = AndroidUtilities.dp(1);
                viewsEmojiLine.get(emoji_previous_page).setLayoutParams(layoutParams);
                viewsEmojiLine.get(emoji_previous_page).setBackgroundColor(0x4DAAAAAA);

                // set style selected item
                resourceName = "ic_emoji" + position + "_check";
                resourceID = getResources().getIdentifier(resourceName, "drawable",
                        getPackageName());
                viewsEmojiImage.get(position).setImageResource(resourceID);

                layoutParams = viewsEmojiLine.get(position).getLayoutParams();
                layoutParams.height = AndroidUtilities.dp(2);
                viewsEmojiLine.get(position).setLayoutParams(layoutParams);
                viewsEmojiLine.get(position).setBackgroundColor(0xff33b5e5);
                emoji_previous_page = position;

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
            emoji_btn.setBackgroundResource(R.drawable.ic_emoji1);
            AndroidUtilities.showKeyboard(messageEdit);
            messageEdit.requestFocus();

        } else if (linear_emoji_view.getVisibility() == View.GONE && v.getId() == R.id.frame_emoji_btn) {
            if (viewPager.getAdapter() == null) {
                viewPager.setAdapter(new EmojiPagerAdapter(getSupportFragmentManager()));

                for (int i = 0; i < EmojiData.emojiData.length + 1; i++) {

                    String resourceName = "emoji_icon_line" + i;
                    int resourceID = getResources().getIdentifier(resourceName, "id",
                            getPackageName());
                    View view = findViewById(resourceID);
                    viewsEmojiLine.add(view);

                    resourceName = "emoji_frame_icon" + i;
                    resourceID = getResources().getIdentifier(resourceName, "id",
                            getPackageName());
                    FrameLayout view1 = (FrameLayout) findViewById(resourceID);
                    final int k = i;
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(k);
                        }
                    });

                    resourceName = "emoji_image_icon" + i;
                    resourceID = getResources().getIdentifier(resourceName, "id",
                            getPackageName());
                    ImageView view2 = (ImageView) findViewById(resourceID);
                    viewsEmojiImage.add(view2);

                }

                ViewGroup.LayoutParams layoutParams = viewsEmojiLine.get(0).getLayoutParams();
                layoutParams.height = AndroidUtilities.dp(2);
                viewsEmojiLine.get(0).setLayoutParams(layoutParams);
                viewsEmojiLine.get(0).setBackgroundColor(0xff33b5e5);

                viewsEmojiImage.get(0).setImageResource(R.drawable.ic_emoji0_check);

                emoji_previous_page = 0;
            }

            AndroidUtilities.hideKeyboard(messageEdit);
            emoji_btn.setBackgroundResource(R.drawable.ic_emoji7);
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
            return EmojiData.emojiData.length + 1;
        }
    }
}
