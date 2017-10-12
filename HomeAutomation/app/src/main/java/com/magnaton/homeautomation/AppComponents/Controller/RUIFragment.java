package com.magnaton.homeautomation.AppComponents.Controller;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

/**
 * Created by Shridhar on 10/27/16.
 */

public abstract class RUIFragment extends Fragment {

    protected abstract void InitialSetupForRootView(View rootView);

    protected void addFragment(int containerViewId, RUIFragment fragment) {

        hideKeyboard();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    protected void popFragment() {

        hideKeyboard();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(this);
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.size() > 1) {
            Fragment fragment = fragments.get(fragments.size() - 2);
        }
        fragmentManager.popBackStack();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
    }

    protected void hideKeyboard() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                try {
                    InputMethodManager inputManager =
                            (InputMethodManager) getContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
            }
        }, 200);
    }
}
