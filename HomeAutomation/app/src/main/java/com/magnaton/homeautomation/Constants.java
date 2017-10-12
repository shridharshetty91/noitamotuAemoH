package com.magnaton.homeautomation;

/**
 * Created by Shridhar on 12/3/16.
 */

public class Constants {
    public static String Log_TAG = "HomeAutomation";
    public static String SharedPreferencesTag = "HomeAutomationSharedPreferences";

    public enum SideMenuOptions {
        None,
        Home,
        Settings,
        Profile,
        Logout
    }

    public enum IconTypes {
        Floor, Garden, Corridor,
        Office, Bedroom, Bathroom
    }

    public enum SwitchTypes {
        OnOffSwitch, Slider
    }

    public static int DelayToPresentFragmentInMS = 700;
}
