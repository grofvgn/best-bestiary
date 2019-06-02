package com.example.bestbestiary;

import com.google.android.gms.maps.model.LatLng;

public class Monster {
    private String name;
    private String headName;
    private String bodyName;
    private String footName;

    private String description;
    private boolean isOpened;
    int headindex;
    LatLng monsterLocation;

    public Monster(String name, String headName, String bodyName, String footName, boolean isOpened, int headindex, String description, LatLng monsterLocation) {
        this.name = name;
        this.headName = headName;
        this.bodyName = bodyName;
        this.footName = footName;
        this.isOpened = isOpened;
        this.headindex = headindex;
        this.description = description;
        this.monsterLocation = monsterLocation;
    }

    //------ GETTERS -------------------------------------------------------------------------
    public int getHeadindex() { return headindex; }

    public String getName() {
        return name;
    }

    public String getHeadName() { return headName; }

    public String getBodyName() { return bodyName; }

    public String getFootName() { return footName; }

    public String getDescription() { return description; }

    public boolean isOpened() {
        return isOpened;
    }

    public LatLng getMonsterLocation() { return monsterLocation; }

    //------ SETTERS -------------------------------------------------------------------------

    public void setDescription(String description) { this.description = description; }

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

    public void setHeadindex(int headindex) { this.headindex = headindex; }

    public void setMonsterLocation(LatLng monsterLocation) { this.monsterLocation = monsterLocation; }
}
