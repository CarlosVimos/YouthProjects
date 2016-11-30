package com.vimos.karol.youthprojects.Model;

/**
 * Created by Karol on 2016-08-21.
 */
public class YouthProject {

    public String title;  // title of a project
    public String place;
    public String date;  // description of a project
    public String deadline;
    public String website; // link for project registration

    public YouthProject(){

    }

    public YouthProject(String title, String place, String date, String deadline, String website){

        this.title = title;
        this.place = place;
        this.date = date;
        this.deadline = deadline;
        this.website = website;
    }

    public String getTitle() {
        return title;
    }
    public String getPlace() {
        return place;
    }


    public String getDate() {
        return date;
    }

    public String getDeadline(){
        return deadline;
    }

    public String getWebsite() {
        return website;
    }
}
