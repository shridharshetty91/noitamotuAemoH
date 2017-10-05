package com.magnaton.homeautomation.Views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Shridhar on 12/30/16.
 */

public class SliderCell extends RelativeLayout {
    public SliderCell(Context context) {
        super(context);
    }

    public SliderCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SliderCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SliderCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}