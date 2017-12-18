package com.magnaton.homeautomation.Dashboard;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magnaton.homeautomation.AppComponents.Model.AppPreference;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.R;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class LeftNavigationView extends LinearLayout {

    private TextView _homeTextView;
    private TextView _settingTextView;
    private TextView _profileTextView;
    private TextView _logoutTextView;

    private Constants.SideMenuOptions _currentSelectedOption = Constants.SideMenuOptions.Home;
    private ArrayList<TextView> _arrayOfOptionTextViews = new ArrayList<>();

    public ILeftNavigationView delegate;

    public LeftNavigationView(Context context) {
        super(context);
        init(null, 0);
    }

    public LeftNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LeftNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setUpLeftNavigationView();
    }

    public void setCurrentSelectedOption(Constants.SideMenuOptions _currentSelectedOption) {
        this._currentSelectedOption = _currentSelectedOption;

        UpdateOptions();
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

    private void  setUpLeftNavigationView()
    {
        TextView userNameTextView = (TextView) findViewById(R.id.username_textview);
        userNameTextView.setText(AppPreference.getAppPreference().getUserFirstName());

        _homeTextView = (TextView) findViewById(R.id.home_textview);
        _settingTextView = (TextView) findViewById(R.id.settings_textview);
        _profileTextView = (TextView) findViewById(R.id.profile_textview);
        _logoutTextView = (TextView) findViewById(R.id.logout_textview);

        _arrayOfOptionTextViews.add(_homeTextView);
        _arrayOfOptionTextViews.add(_settingTextView);
        _arrayOfOptionTextViews.add(_profileTextView);
        _arrayOfOptionTextViews.add(_logoutTextView);

        for (TextView textView : _arrayOfOptionTextViews) {
            textView.setOnClickListener(onClickOptionListener);
        }
        UpdateOptions();
    }

    private OnClickListener onClickOptionListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (delegate != null) {
                int index = _arrayOfOptionTextViews.indexOf(v);
                Constants.SideMenuOptions option = Constants.SideMenuOptions.values()[index + 1];
                delegate.clickedOn(LeftNavigationView.this, option);
            }
        }
    };

    private void UpdateOptions() {

        for (TextView textView : _arrayOfOptionTextViews) {
            textView.setBackgroundResource(0);
        }

        switch (_currentSelectedOption) {
            case Home:{
                setBackgroundForTextView(_homeTextView, R.drawable.bottom_green_border);
                break;
            }
            case Settings: {
                setBackgroundForTextView(_settingTextView, R.drawable.bottom_green_border);
                break;
            }
            case Profile: {
                setBackgroundForTextView(_profileTextView, R.drawable.bottom_green_border);
                break;
            }
            case Logout: {
                setBackgroundForTextView(_logoutTextView, R.drawable.bottom_green_border);

                break;
            }
        }
    }

    private void setBackgroundForTextView(TextView textView, int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setBackground(getContext().getResources().getDrawable(drawableId, null));
        } else {
            textView.setBackground(getContext().getResources().getDrawable(drawableId));
        }
    }

    /**
     * Created by Shridhar on 10/6/17.
     */

    public static interface ILeftNavigationView {
        void clickedOn(LeftNavigationView sender, Constants.SideMenuOptions option);
    }
}
