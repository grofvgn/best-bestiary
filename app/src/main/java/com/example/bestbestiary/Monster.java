package com.example.bestbestiary;

public class Monster {
    private String name, headName, bodyName, footName;
    private boolean isOpened;

    public Monster(String name, String headName, String bodyName, String footName, boolean isOpened) {
        this.name = name;
        this.headName = headName;
        this.bodyName = bodyName;
        this.footName = footName;
        this.isOpened = isOpened;
    }

    //------ GETTERS -------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public String getHeadName() {
        return headName;
    }

    public String getBodyName() {
        return bodyName;
    }

    public String getFootName() {
        return footName;
    }

    public boolean isOpened() {
        return isOpened;
    }

    //------ SETTERS -------------------------------------------------------------------------


    public void setName(String name) {
        this.name = name;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public void setBodyName(String bodyName) {
        this.bodyName = bodyName;
    }

    public void setFootName(String footName) {
        this.footName = footName;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}
