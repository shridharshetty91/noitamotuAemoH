package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.magnaton.homeautomation.AppComponents.Views.RUIImageView;
import com.magnaton.homeautomation.AppComponents.Views.RUITextView;
import com.magnaton.homeautomation.R;

/**
 * TODO: document your custom view class.
 */
public class SwitchBoardCell extends RelativeLayout {


    private RUITextView mDeviceNameTextView;
    private RUIImageView mImageView;

    public SwitchBoardCell(Context context) {
        super(context);
        init(null, 0);
    }

    public SwitchBoardCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SwitchBoardCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initializeSwitchBoardCell();
    }

    private void initializeSwitchBoardCell() {
        mDeviceNameTextView = (RUITextView) findViewById(R.id.device_name_textview);
        mImageView = (RUIImageView) findViewById(R.id.imageView);
    }

    public void setDeviceName(String deviceName) {
        mDeviceNameTextView.setText(deviceName);
    }

    public void setImageSelected(boolean selected) {

        if (selected) {
            mImageView.setImageResource(R.mipmap.device_available_connected);
        } else {
            mImageView.setImageResource(R.mipmap.device_available);
        }
    }
}
