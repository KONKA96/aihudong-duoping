package com.model;

public class Screen {
    private String id;
    
    private String title;

    private String username;

    private String password;

    private String roomId;
    
    private Integer adminId;

    private String duration;

    private Integer number;

    private String divice;
	
	private Room room;
	
	private int role;
	
	private String sessionId;
	
	private String type;
	
	private String resolution;
	
	private Admin admin;
	
	private String sid;
	
	private String ipAddr;

    private String macAddr;
    
    private String randomCode1;
    
    private String randomCode2;
    
    private String isSpeaker;
    
    private String isVideo;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsSpeaker() {
		return isSpeaker;
	}

	public void setIsSpeaker(String isSpeaker) {
		this.isSpeaker = isSpeaker;
	}

	public String getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(String isVideo) {
		this.isVideo = isVideo;
	}

	public String getRandomCode1() {
		return randomCode1;
	}

	public void setRandomCode1(String randomCode1) {
		this.randomCode1 = randomCode1;
	}

	public String getRandomCode2() {
		return randomCode2;
	}

	public void setRandomCode2(String randomCode2) {
		this.randomCode2 = randomCode2;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Screen() {
		super();
	}

	public Screen(String username) {
		super();
		this.username = username;
	}
	
	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
    
    public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDivice() {
        return divice;
    }

    public void setDivice(String divice) {
        this.divice = divice == null ? null : divice.trim();
    }

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roomId == null) ? 0 : roomId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Screen other = (Screen) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roomId == null) {
			if (other.roomId != null)
				return false;
		} else if (!roomId.equals(other.roomId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Screen [id=" + id + ", title=" + title + ", username=" + username + ", password=" + password
				+ ", roomId=" + roomId + ", adminId=" + adminId + ", duration=" + duration + ", number=" + number
				+ ", divice=" + divice + ", room=" + room + ", role=" + role + ", sessionId=" + sessionId + ", type="
				+ type + ", resolution=" + resolution + ", admin=" + admin + ", sid=" + sid + ", ipAddr=" + ipAddr
				+ ", macAddr=" + macAddr + ", randomCode1=" + randomCode1 + ", randomCode2=" + randomCode2
				+ ", isSpeaker=" + isSpeaker + ", isVideo=" + isVideo + "]";
	}

}