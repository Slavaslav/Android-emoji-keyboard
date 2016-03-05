package com.slava.emojicfc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private EditText messageEdit;
    EmojiView emojiView;
    ImageView emoji_btn;
    FrameLayout frame_emoji_view;
    public static volatile Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationContext = getApplicationContext();

        emoji_btn = (ImageView) findViewById(R.id.emoji_btn);
        frame_emoji_view = (FrameLayout) findViewById(R.id.frame_emoji_view);
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

        if (frame_emoji_view.getVisibility() == View.VISIBLE) {

            emoji_btn.setBackgroundResource(R.drawable.emoji_button);
            frame_emoji_view.setVisibility(View.GONE);
            messageEdit.requestFocus();
            AndroidUtilities.showKeyboard(messageEdit);

        } else if (frame_emoji_view.getVisibility() == View.GONE) {

            if (v.getId() != R.id.message_edit) {
                AndroidUtilities.hideKeyboard(messageEdit);
                emoji_btn.setBackgroundResource(R.drawable.ic_keyboard_emoji);
                frame_emoji_view.setVisibility(View.VISIBLE);
                if (emojiView == null) {
                    emojiView = new EmojiView(this);
                    frame_emoji_view.addView(emojiView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        }
    }
}
