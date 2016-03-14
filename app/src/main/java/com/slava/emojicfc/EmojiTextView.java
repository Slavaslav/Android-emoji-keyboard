package com.slava.emojicfc;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class EmojiTextView extends TextView {

    private int emojiTextSize;
    private int emojiSize;
    private int emojiAlignment;
    private int textStart;
    private int textLength;

    public EmojiTextView(Context context) {
        super(context);
        init(null);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        emojiTextSize = (int) getTextSize(); // return text size px
        if (attrs == null) {
            emojiSize = (int) getTextSize();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emoji);
            emojiSize = (int) a.getDimension(R.styleable.Emoji_emojiSize, getTextSize());
            emojiAlignment = a.getInt(R.styleable.Emoji_emojiAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
            textStart = a.getInteger(R.styleable.Emoji_emojiTextStart, 0);
            textLength = a.getInteger(R.styleable.Emoji_emojiTextLength, -1);
            a.recycle();
        }
        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            Emoji.addEmojis(getContext(), builder, emojiSize, emojiAlignment, emojiTextSize, textStart, textLength);
            text = builder;
        }
        super.setText(text, type);
    }

}
