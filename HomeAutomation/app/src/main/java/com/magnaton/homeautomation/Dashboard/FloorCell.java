package com.magnaton.homeautomation.Dashboard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magnaton.homeautomation.R;

/**
 * TODO: document your custom view class.
 */
public class FloorCell extends RelativeLayout {

    private ImageView mImageView;
    private TextView mFloorNameTextView;

    public FloorCell(Context context) {
        super(context);
        init(null, 0);
    }

    public FloorCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FloorCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initializeFloorCell();
    }

    private void initializeFloorCell() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        mFloorNameTextView = (TextView) findViewById(R.id.floor_name_textview);
    }

    public void setImage(String floorType) {
        if (getResources().getString(R.string.floor_type_room).equalsIgnoreCase(floorType)) {
            mImageView.setImageResource(R.mipmap.floor);
        } else if (getResources().getString(R.string.floor_type_garden).equalsIgnoreCase(floorType)) {
            mImageView.setImageResource(R.mipmap.garden);
        }
    }

    public void setFloorName(String floorName) {
        mFloorNameTextView.setText(floorName);
    }
}
