package com.magnaton.homeautomation.Views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Shridhar on 12/30/16.
 */

public class ButtonCell extends RelativeLayout {
    public ButtonCell(Context context) {
        super(context);
    }

    public ButtonCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ButtonCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
