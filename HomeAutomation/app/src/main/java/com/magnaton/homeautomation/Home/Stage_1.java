package com.magnaton.homeautomation.Home;

import java.util.ArrayList;

public class Stage_1
{
    private ArrayList<Stage_2> Stage_2;

    private String name;

    private String parent_icon;

    private String parent_id;

    public Stage_1() {
        Stage_2 = new ArrayList<>();
    }

    public ArrayList<Stage_2> getStage_2 ()
    {
        return Stage_2;
    }

    public void setStage_2 (ArrayList<Stage_2> Stage_2)
    {
        this.Stage_2 = Stage_2;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public int getParent_icon ()
    {
        return Integer.parseInt(parent_icon);
    }

    public void setParent_icon (String parent_icon)
    {
        this.parent_icon = parent_icon;
    }

    public String getParent_id ()
    {
        return parent_id;
    }

    public void setParent_id (String parent_id)
    {
        this.parent_id = parent_id;
    }
}
