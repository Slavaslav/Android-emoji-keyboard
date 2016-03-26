package com.slava.emojicfc;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.slava.emojicfc.emoji.Emoji;
import com.slava.emojicfc.emoji.EmojiData;
import com.slava.emojicfc.emoji.EmojiGridPageFragment;

public class MainActivity extends AppCompatActivity {

    private EditText messageEdit;
    private LinearLayout linear_emoji_view;
    private ImageView emoji_btn;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        FrameLayout frame_emoji_btn = (FrameLayout) findViewById(R.id.frame_emoji_btn);
        frame_emoji_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEmojiView(v);
            }
        });

        emoji_btn = (ImageView) findViewById(R.id.emoji_btn);
        linear_emoji_view = (LinearLayout) findViewById(R.id.linear_emoji_view);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_emoji);

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

        viewPager = (ViewPager) findViewById(R.id.emoji_pager);
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
                Drawable drawable;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable = getResources().getDrawable(Emoji.hashMap.get(code), App.applicationContext.getTheme());
                } else {
                    drawable = getDrawable(Emoji.hashMap.get(code));
                }
                drawable.setBounds(0, 0, AndroidUtilities.dp(25), AndroidUtilities.dp(25));
                SpannableString spannableString = new SpannableString(" ");
                spannableString.setSpan(new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                messageEdit.getText().append(spannableString);
            }
        });


    }

    private void createEmojiView(View v) {

        if (linear_emoji_view.getVisibility() == View.VISIBLE) {
            linear_emoji_view.setVisibility(View.GONE);
            emoji_btn.setImageResource(R.drawable.ic_emoji1);
            AndroidUtilities.showKeyboard(messageEdit);
            messageEdit.requestFocus();

        } else if (linear_emoji_view.getVisibility() == View.GONE && v.getId() == R.id.frame_emoji_btn) {
            if (viewPager.getAdapter() == null) {
                viewPager.setAdapter(new EmojiPagerAdapter(getSupportFragmentManager()));

                tabLayout.setupWithViewPager(viewPager);

                LayoutInflater inflater = getLayoutInflater();

                for (int i = 0; i < tabLayout.getTabCount(); i++) {

                    View view = inflater.inflate(R.layout.item_emoji_icon, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.emoji_icon);

                    String resourceName = "ic_emoji" + i + "_selector";
                    int resourceID = getResources().getIdentifier(resourceName, "drawable",
                            getPackageName());

                    imageView.setImageResource(resourceID);
                    tabLayout.getTabAt(i).setCustomView(view);
                }

                tabLayout.getTabAt(1).select(); // to activate ic_emoji0_selector, otherwise it will not be activated
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
