package com.model;

import java.util.List;

public class Zone {
    private Integer id;

    private String zoneName;

    private String desc;
	
	private List<Building> buildingList;

    public List<Building> getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(List<Building> buildingList) {
		this.buildingList = buildingList;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName == null ? null : zoneName.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

	@Override
	public String toString() {
		return "Zone [id=" + id + ", zoneName=" + zoneName + ", desc=" + desc + ", buildingList=" + buildingList + "]";
	}
    
}