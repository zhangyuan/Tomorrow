package com.nextcloudmedia.tomorrow;

/**
 * Created by zhangyuan on 5/18/14.
 */
public class App {
    private static App instance = new App();

    String deviceId;
    String line1Number;
    String subscriberId;

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getLine1Number() {

        return line1Number;
    }

    public void setLine1Number(String line1Number) {
        this.line1Number = line1Number;
    }

    public String getDeviceId() {

        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    private App(){

    }

    public static App getInstance(){
        return instance;
    }
}
