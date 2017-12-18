package com.magnaton.homeautomation.Home;

/**
 * Created by Shridhar on 12/12/17.
 */

public class Switch {

    private String switche_type;
    private String switch_id;
    private String name;


    public String getSwitche_type() {
        return switche_type;
    }

    public void setSwitche_type(String switche_type) {
        this.switche_type = switche_type;
    }

    public String getSwitch_id() {
        return switch_id;
    }

    public void setSwitch_id(String switch_id) {
        this.switch_id = switch_id;
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
