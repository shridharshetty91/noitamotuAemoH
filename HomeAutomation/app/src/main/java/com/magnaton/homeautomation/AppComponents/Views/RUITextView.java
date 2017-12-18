package com.magnaton.homeautomation.AppComponents.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.R;

/**
 * Created by Shridhar on 10/11/17.
 */

public class RUITextView extends TextView {
    public RUITextView(Context context) {
        super(context);
    }

    public RUITextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, this, attrs);
    }

    public RUITextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, this, attrs);
    }

    public static void setCustomFont(Context ctx, TextView textView, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        String customFont = a.getString(R.styleable.TextViewPlus_custom_font_family);
        setCustomFont(ctx, textView, customFont);
        a.recycle();
    }

    public static void setCustomFont(Context ctx, TextView textView, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(Constants.Log_TAG, "Could not get typeface: "+e.getMessage());
        }

        textView.setTypeface(tf);
    }
}
