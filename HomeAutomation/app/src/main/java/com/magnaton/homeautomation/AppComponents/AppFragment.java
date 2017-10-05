package com.magnaton.homeautomation.AppComponents;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

/**
 * Created by Shridhar on 10/27/16.
 */

public class AppFragment extends Fragment {

    protected void addFragment(int containerViewId, AppFragment fragment) {

        hideKeyboard();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(this.toString());
//        fragmentTransaction.hide(this);
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
//            transaction.attach(fragment);
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
