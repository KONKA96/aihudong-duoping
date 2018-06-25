package com.model;

import java.util.List;

public class Building {
    private Integer id;

    private String buildingName;

    private Integer adminId;

    private Integer zoneId;
    
    private Zone zone;
    
    private Admin admin;
    
    private List<Room> roomList;
    
    public List<Room> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<Room> roomList) {
		this.roomList = roomList;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName == null ? null : buildingName.trim();
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

	@Override
	public String toString() {
		return "Building [id=" + id + ", buildingName=" + buildingName + ", adminId=" + adminId + ", zoneId=" + zoneId
				+ ", zone=" + zone + ", admin=" + admin + ", roomList=" + roomList + "]";
	}
    
}