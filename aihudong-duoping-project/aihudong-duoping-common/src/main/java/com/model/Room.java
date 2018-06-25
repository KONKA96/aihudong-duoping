package com.model;

import java.util.List;

public class Room {
    private String id;

    private String num;

    private Integer buildingId;

    private String desc;
	
	private List<Screen> screenList;
	
	private Building building;
	
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public List<Screen> getScreenList() {
		return screenList;
	}

	public void setScreenList(List<Screen> screenList) {
		this.screenList = screenList;
	}
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

	@Override
	public String toString() {
		return "Room [id=" + id + ", num=" + num + ", buildingId=" + buildingId + ", desc=" + desc + ", screenList="
				+ screenList + ", building=" + building + "]";
	}
    
}