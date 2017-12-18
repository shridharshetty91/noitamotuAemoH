package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.magnaton.homeautomation.AppComponents.Views.RUIImageView;
import com.magnaton.homeautomation.AppComponents.Views.RUIPopupMenu;
import com.magnaton.homeautomation.AppComponents.Views.RUITextView;
import com.magnaton.homeautomation.R;

import static com.magnaton.homeautomation.AppComponents.Model.Constants.IconTypes;

/**
 * TODO: document your custom view class.
 */
public class FloorCell extends RelativeLayout {

    private IFloorCell mListner;

    private RUIImageView mImageView;
    private RUITextView mFloorNameTextView;
    private RUIImageView mMoreImageView;

    private Object mStage_1;

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

    public void setListner(IFloorCell listner) {
        this.mListner = listner;
    }

    private void initializeFloorCell() {
        mImageView = (RUIImageView) findViewById(R.id.imageView);
        mFloorNameTextView = (RUITextView) findViewById(R.id.floor_name_textview);
        mMoreImageView = (RUIImageView) findViewById(R.id.imageView_more);
        mMoreImageView.setOnClickListener(mOnClickMoreImage);
    }

    public Object getData() {
        return mStage_1;
    }

    public void setData(Object data) {
        this.mStage_1 = data;
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

    private OnClickListener mOnClickMoreImage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            RUIPopupMenu popupMenu = new RUIPopupMenu(getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.floor_cell_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.floor_cell_menu_edit:
                            if (mListner != null) {
                                mListner.floorCellEdit(FloorCell.this, mStage_1);
                            }
                            break;
                        case R.id.floor_cell_menu_delete:
                            mListner.floorCellDelete(FloorCell.this, mStage_1);
                            break;
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
    };

    public interface IFloorCell {
        void floorCellEdit(FloorCell sender, Object data);
        void floorCellDelete(FloorCell sender, Object data);
    }
}
