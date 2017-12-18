package com.magnaton.homeautomation.AppComponents.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Shridhar on 10/12/17.
 */

public class RUIListView extends ListView {
    public RUIListView(Context context) {
        super(context);
    }

    public RUIListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RUIListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void scrollMyListViewToBottom() {
        this.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                ListAdapter adapter = getAdapter();
                if (adapter != null && adapter.getCount() > 0) {
                    RUIListView.this.setSelection(adapter.getCount() - 1);
                }
            }
        });
    }

    public void setScrollableBootomPadding(int height) {
        View bottomPaddingFooterView = new View(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(this.getWidth(), height);
        bottomPaddingFooterView.setLayoutParams(lp);
        this.addFooterView(bottomPaddingFooterView);
    }
}
