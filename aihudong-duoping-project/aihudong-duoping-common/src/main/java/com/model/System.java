package com.model;

public class System {
	private String sid;

	private String osver;

	private String cpu;

	private String memory;

	private String graphicscard;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid == null ? null : sid.trim();
	}

	public String getOsver() {
		return osver;
	}

	public void setOsver(String osver) {
		this.osver = osver == null ? null : osver.trim();
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu == null ? null : cpu.trim();
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory == null ? null : memory.trim();
	}

	public String getGraphicscard() {
		return graphicscard;
	}

	public void setGraphicscard(String graphicscard) {
		this.graphicscard = graphicscard == null ? null : graphicscard.trim();
	}
}