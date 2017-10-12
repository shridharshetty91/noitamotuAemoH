package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.magnaton.homeautomation.AppComponents.Views.RUIPopupMenu;
import com.magnaton.homeautomation.Constants;
import com.magnaton.homeautomation.R;

/**
 * TODO: document your custom view class.
 */
public class SwitchCell extends RelativeLayout {

    private ImageView mImageView_more;
    private TextView mTitleTextView;
    private Switch mSwitch;
    private SeekBar mSeekbar;

    public SwitchCell(Context context) {
        super(context);
        init(null, 0);
    }

    public SwitchCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SwitchCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Update TextPaint and text measurements from attributes
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initializeSwitchCell();
    }

    private void initializeSwitchCell() {
        mImageView_more = (ImageView) findViewById(R.id.imageView_more);
        mTitleTextView = (TextView) findViewById(R.id.title_textview);
        mSwitch = (Switch) findViewById(R.id.switch_view);
        mSeekbar = (SeekBar) findViewById(R.id.seek_bar);

        
        mImageView_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RUIPopupMenu popupMenu = new RUIPopupMenu(getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.switch_cell_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.switch_cell_menu_schedule:
                                break;
                            case R.id.switch_cell_menu_edit:
                                break;
                            case R.id.switch_cell_menu_delete:
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setSwitchType(Constants.SwitchTypes switchType) {
        switch (switchType) {
            case OnOffSwitch:
                mSwitch.setVisibility(VISIBLE);
                mSeekbar.setVisibility(GONE);
                break;
            case Slider:
                mSwitch.setVisibility(GONE);
                mSeekbar.setVisibility(VISIBLE);
                break;
        }
    }
}
