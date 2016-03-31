package com.slava.emojicfc;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<ImageView> imageViews = new ArrayList<>();
    private final ArrayList<EmojiGridPageFragment.EmojiGridAdapter> emojiGridAdapters = new ArrayList<>();
    private EditText messageEdit;
    private LinearLayout linearEmojiView;
    private ImageView emojiBtn;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int lastPage;
    private Fragment emojiGridPageFragmentHolder;

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

        FrameLayout frameEmojiBtn = (FrameLayout) findViewById(R.id.frame_emoji_btn);
        frameEmojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEmojiView(v);
            }
        });

        emojiBtn = (ImageView) findViewById(R.id.emoji_btn);
        linearEmojiView = (LinearLayout) findViewById(R.id.linear_emoji_view);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_emoji);

        FrameLayout backSpaceBtn = (FrameLayout) findViewById(R.id.emoji_frame_backspace);
        backSpaceBtn.setOnTouchListener(new View.OnTouchListener() {
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
        viewPager.setOffscreenPageLimit(6);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }

                if (imageViews.get(lastPage).isSelected() && Emoji.sharedPreferencesEmoji.getAll().size() != 0) {
                    imageViews.get(lastPage).setSelected(false);
                }
                lastPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        EmojiGridPageFragment.setListener(new EmojiGridPageFragment.Listener() {
            @Override
            public void onClickEmoji(String code) {

                Emoji.addRecentEmoji(code);
                emojiGridAdapters.get(0).notifyDataSetChanged();

                if (viewPager.getCurrentItem() != 0) {

                    // refresh the fragment content recent emoji
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.detach(emojiGridPageFragmentHolder);
                    fragmentTransaction.attach(emojiGridPageFragmentHolder);
                    fragmentTransaction.commit();
                }

                Paint.FontMetricsInt fontMetrics = messageEdit.getPaint().getFontMetricsInt();
                int size = Math.abs(fontMetrics.descent) + Math.abs(fontMetrics.ascent);

                Drawable drawable = ContextCompat.getDrawable(App.applicationContext, Emoji.hashMap.get(code));
                drawable.setBounds(0, 0, size, size);
                SpannableString spannableString = new SpannableString(" ");
                spannableString.setSpan(new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                messageEdit.getText().append(spannableString);
            }

            @Override
            public void createListGridAdapter(EmojiGridPageFragment.EmojiGridAdapter emojiGridAdapter) {
                emojiGridAdapters.clear();
                emojiGridAdapters.add(emojiGridAdapter);
            }
        });


    }

    private void createEmojiView(View v) {

        if (linearEmojiView.getVisibility() == View.VISIBLE) {
            linearEmojiView.setVisibility(View.GONE);
            emojiBtn.setImageResource(R.drawable.ic_emoji1);
            AndroidUtilities.showKeyboard(messageEdit);
            messageEdit.requestFocus();

        } else if (linearEmojiView.getVisibility() == View.GONE && v.getId() == R.id.frame_emoji_btn) {
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
                    imageViews.add(imageView);

                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null) {
                        tab.setCustomView(view);
                    }
                }

                if (Emoji.sharedPreferencesEmoji.getAll().size() == 0) {
                    lastPage = 1;
                } else {
                    lastPage = 0;
                }
                imageViews.get(lastPage).setSelected(true);
                TabLayout.Tab tab = tabLayout.getTabAt(lastPage);
                if (tab != null) {
                    tab.select();
                }
            }

            AndroidUtilities.hideKeyboard(messageEdit);
            emojiBtn.setImageResource(R.drawable.ic_emoji7);
            linearEmojiView.setVisibility(View.VISIBLE);

        }
    }

    private class EmojiPagerAdapter extends FragmentPagerAdapter {

        public EmojiPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int page) {
            EmojiGridPageFragment emojiGridPageFragment = new EmojiGridPageFragment();
            emojiGridPageFragment.createInstance(page - 1);

            if (page == 0) {
                emojiGridPageFragmentHolder = emojiGridPageFragment;
            }

            return emojiGridPageFragment;
        }

        @Override
        public int getCount() {
            return EmojiData.emojiData.length + 1; // one page for recent emoji
        }
    }
}
