package com.vimos.karol.youthprojects.Adapter;

/**
 * Created by Karol on 2016-08-28.
 */
public class Project {

    private String text;
    private String others;
    private int iconID;


    public Project(String text, String others, int iconID){
        super();
        this.text = text;
        this.others = others;
        this.iconID = iconID;

    }

    public String getText(){
        return text;
    }

    public String getOthers(){
        return others;
    }

    public int getIconID(){
        return iconID;
    }


}
