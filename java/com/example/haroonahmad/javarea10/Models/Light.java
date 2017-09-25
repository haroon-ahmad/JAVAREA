package com.example.haroonahmad.javarea10.Models;

/**
 * Created by Haroon Ahmad on 11/21/2016.
 */

public class Light extends Device {

    public Light()
    {
        super();
    }
    public Light(String deviceID, String name, boolean state, int portNum) {
        super(deviceID, name, state, portNum);
    }

    public Light(int portNum) {
        super(portNum);
    }
}
