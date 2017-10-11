package com.magnaton.homeautomation.AppComponents.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Shridhar on 10/12/17.
 */

public class RUIEditText extends EditText {
    public RUIEditText(Context context) {
        super(context);
    }

    public RUIEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        RUITextView.setCustomFont(context, this, attrs);
    }

    public RUIEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        RUITextView.setCustomFont(context, this, attrs);
    }
}
