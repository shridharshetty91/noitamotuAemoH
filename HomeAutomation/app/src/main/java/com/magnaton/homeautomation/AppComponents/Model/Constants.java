package com.magnaton.homeautomation.AppComponents.Model;

/**
 * Created by Shridhar on 12/3/16.
 */

public class Constants {

    public static String Log_TAG = "HomeAutomation";
    public static String SharedPreferencesTag = "HomeAutomationSharedPreferences";

    //Permission Request Codes
    public static int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 100;

    public enum SideMenuOptions {
        None,
        Home,
        Settings,
        Profile,
        Logout
    }

    public static enum IconTypes {
        Default(0), Floor(1), Garden(2), Corridor(3),
        Office(4), Bedroom(5), Bathroom(6);

        private int mValue;
        IconTypes(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        public static IconTypes fromInteger(int x) {
            switch(x) {
                case 1:
                    return Floor;
                case 2:
                    return Garden;
                case 3:
                    return Corridor;
                case 4:
                    return Office;
                case 5:
                    return Bedroom;
                case 6:
                    return Bathroom;
            }
            return Default;
        }
    }

    public enum SwitchTypes {
        OnOffSwitch, Slider
    }

    //Timings
    public static int ToastMessageSeconds = 3;
    public static int DelayToPresentFragmentInMS = 700;


    //Messages
    public static String ServerFailed = "Unable to process the request. Please try after some time";
    public static String EnterDevicePassword = "Please enter device password";
    public static String DeviceConfigured = "Device configured sucessfully";

    //API Keys
    public static String UserIdKey = "user_id";
    public static String UserTokenKey = "user_token";
    public static String FlagKey = "flag";
    public static String ParentIdKey = "parent_id";
    public static String ParentNameKey= "parent_name";
    public static String ParentIconKey = "parent_icon";
    public static String ChildIdKey = "child_id";
    public static String ChildNameKey= "child_name";
    public static String ChildIconKey = "child_icon";

    public static String DeviceIdKey = "device_id";
    public static String HomeSsidKey = "home_ssid";
    public static String HomePasswordKey = "home_password";
    public static String SwName1Key = "sw_name_1";
    public static String SwType1Key = "sw_type_1";
    public static String SwName2Key = "sw_name_2";
    public static String SwType2Key = "sw_type_2";

    public static String TokenExpireMessage = "Token not proper";



    //Broadcast Keys
    public static String LogoutBroadcastKey = "LogoutBroadcastKey";
}
