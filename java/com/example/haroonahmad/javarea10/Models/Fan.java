package com.example.haroonahmad.javarea10.Models;

/**
 * Created by Haroon Ahmad on 2/23/2017.
 */

public class Fan extends Device {
    int speed;

    public Fan()
    {

        super();
        speed=1;
    }
    public Fan(String name, boolean state, int portNum, int speed) {
        super(name, state, portNum);
        this.speed = speed;
    }

    public Fan(int portNum, int speed) {
        super(portNum);
        this.speed = speed;
    }

    public Fan(int speed) {
        this.speed = speed;
    }

    public Fan(String deviceID, String name, boolean state, int portNum, int speed) {
        super(deviceID, name, state, portNum);
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
