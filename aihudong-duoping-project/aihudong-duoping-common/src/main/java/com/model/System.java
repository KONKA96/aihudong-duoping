package com.model;

public class System {
    private Integer id;

    private String osver;

    private String cpu;

    private String memory;

    private String graphicscard;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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