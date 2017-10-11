package com.magnaton.homeautomation.AppComponents.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Shridhar on 10/11/17.
 */

public class RUIButton extends Button {
    public RUIButton(Context context) {
        super(context);
    }

    public RUIButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        RUITextView.setCustomFont(context, this, attrs);
    }

    public RUIButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        RUITextView.setCustomFont(context, this, attrs);
    }
}
