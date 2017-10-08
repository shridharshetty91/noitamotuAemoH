package com.magnaton.homeautomation.AppComponents.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Shridhar on 10/8/17.
 */

public class RUIPopupMenu extends PopupMenu {

    public RUIPopupMenu(@NonNull Context context, @NonNull View anchor) {
        super(context, anchor);

        try {
            Field[] fields = PopupMenu.class.getDeclaredFields();

            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(this);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
