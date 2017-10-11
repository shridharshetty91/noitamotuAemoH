package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magnaton.homeautomation.R;

import static com.magnaton.homeautomation.Constants.IconTypes;

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

    public void setImage(IconTypes iconType) {
        switch (iconType) {
            case Floor:
                mImageView.setImageResource(R.mipmap.floor);
                break;
            case Garden:
                mImageView.setImageResource(R.mipmap.garden);
                break;
            case Corridor:
                mImageView.setImageResource(R.mipmap.corridor);
                break;
            case Office:
                mImageView.setImageResource(R.mipmap.office);
                break;
            case Bedroom:
                mImageView.setImageResource(R.mipmap.bedroom);
                break;
            case Bathroom:
                mImageView.setImageResource(R.mipmap.bathroom);
                break;
        }
    }

    public void setFloorName(String floorName) {
        mFloorNameTextView.setText(floorName);
    }
}
