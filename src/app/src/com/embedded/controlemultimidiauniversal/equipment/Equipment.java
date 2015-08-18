
package com.embedded.controlemultimidiauniversal.equipment;

public class Equipment {

    private String name;

    private Integer channel;

    private Integer volume;

    private Boolean statusPower;

    public Equipment(String name, Integer channel, Integer volume, Boolean statusPower) {
        this.name = name;
        this.channel = channel;
        this.volume = volume;
        this.statusPower = statusPower;
    }

    public String getName() {
        return name;
    }

    public Integer getChannel() {
        return channel;
    }

    public Integer getVolume() {
        return volume;
    }

    public Boolean getStatusPower() {
        return statusPower;
    }

}
