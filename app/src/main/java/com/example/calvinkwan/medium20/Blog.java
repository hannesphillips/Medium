package com.example.calvinkwan.medium20;

/**
 * Created by calvinkwan on 2/8/18.
 */

public class Blog
{

    private String title, desc, image, name, categ;

    public Blog()
    {

    }

    public Blog(String title, String desc, String image, String name, String categ) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.name = name;
        this.categ = categ;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() { return name; }

    public void setUser(String name) { this.name = name; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCateg() { return categ; }

    public void setCateg(String categ) { this.categ = categ; }

}
