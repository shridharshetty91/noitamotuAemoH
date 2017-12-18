package com.magnaton.homeautomation.Home;

import java.util.ArrayList;

/**
 * Created by Shridhar on 11/12/17.
 */

public class Stage_2
{
    private ArrayList<Switch> switches;

    private String child_icon;

    private String child_id;

    private String name;

    public Stage_2() {
        switches = new ArrayList<>();
    }

    public ArrayList<Switch> getSwitches ()
    {
        return switches;
    }

    public void setSwitches (ArrayList<Switch> switches)
    {
        this.switches = switches;
    }

    public int getChild_icon ()
    {
        return Integer.parseInt(child_icon);
    }

    public void setChild_icon (String child_icon)
    {
        this.child_icon = child_icon;
    }

    public String getChild_id ()
    {
        return child_id;
    }

    public void setChild_id (String child_id)
    {
        this.child_id = child_id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }
}