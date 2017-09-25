package com.example.haroonahmad.javarea10.Models;

/**
 * Created by Haroon Ahmad on 11/21/2016.
 */

public class Device {
    String deviceID;
    String name;
    boolean state;
    int portNum;

    public String getDeviceID() {
        return deviceID;
    }

    public Device(String name, boolean state, int portNum) {
        this.name = name;
        this.state = state;
        this.portNum = portNum;
    }

    public Device(int portNum) {
        this.portNum = portNum;

    }

    public Device() {
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getPortNum() {
        return portNum;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    public Device(String deviceID, String name, boolean state, int portNum) {

        this.deviceID = deviceID;
        this.name = name;
        this.state = state;
        this.portNum = portNum;
    }
    public static  Device  factorymethod(String type)
    {
        if(type.equals("Fan"))
        {
            return new Fan();
        }
        if(type.equals("Light"))
        {
            return  new Light();
        }
        else
        {
            return new Device();
        }
    }
}
