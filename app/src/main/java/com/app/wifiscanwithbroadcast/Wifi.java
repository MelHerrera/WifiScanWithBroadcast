package com.app.wifiscanwithbroadcast;

public class Wifi {
    private String ssid;
    private String bssid;
    
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public Wifi(String ssid, String bssid) {
        this.ssid = ssid;
        this.bssid = bssid;
    }

    public Wifi() {

    }
}
