package com.model;

public class SetUp {
    private Integer id;

    private String waterMark;

    private String logo;

    private String brush;

    private String blackboard;

    private String copy;

    private String blackScreen;

    private String sharingTheDesktop;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(String waterMark) {
        this.waterMark = waterMark == null ? null : waterMark.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getBrush() {
        return brush;
    }

    public void setBrush(String brush) {
        this.brush = brush == null ? null : brush.trim();
    }

    public String getBlackboard() {
        return blackboard;
    }

    public void setBlackboard(String blackboard) {
        this.blackboard = blackboard == null ? null : blackboard.trim();
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy == null ? null : copy.trim();
    }

    public String getBlackScreen() {
        return blackScreen;
    }

    public void setBlackScreen(String blackScreen) {
        this.blackScreen = blackScreen == null ? null : blackScreen.trim();
    }

    public String getSharingTheDesktop() {
        return sharingTheDesktop;
    }

    public void setSharingTheDesktop(String sharingTheDesktop) {
        this.sharingTheDesktop = sharingTheDesktop == null ? null : sharingTheDesktop.trim();
    }
}