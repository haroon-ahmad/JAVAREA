package com.example.haroonahmad.javarea10.Models;

import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 11/21/2016.
 */

public class SwitchBoard {
  private  String MACAddress;
  private  String name;
  private  String bluetoothName;
  private  String switchboardID;
  private  ArrayList<Device> devices;


    public SwitchBoard() {
    }



    public SwitchBoard(ArrayList<Device> devices, String MACAddress, String name, String bluetoothName, String switchboardID) {
        this.devices = devices;
        this.MACAddress = MACAddress;
        this.name = name;
        this.bluetoothName = bluetoothName;
        this.switchboardID = switchboardID;
    }

    public String getSwitchboardID() {

        return switchboardID;
    }

    public void setSwitchboardID(String switchboardID) {
        this.switchboardID = switchboardID;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public void setMACAddress(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }
    public void addDevice(Device _newDevice)
    {
        devices.add(_newDevice);
    }
    private int findDevice(String _deviceID)
    {
        for(int i=0;i<devices.size();++i)
        {
            if(devices.get(i).getDeviceID().equals(_deviceID))
            {
                return i;
            }
        }
        return  -1;
    }
    public Device getDevice(String _deviceID)
    {
        int index=findDevice(_deviceID);
        if(index>-1)
        {
            return devices.get(index);
        }
        else
        {
            return null;
        }

    }
    public boolean deleteDevice(String _deviceID)
    {
        int index=findDevice(_deviceID);
        if(index>-1)
        {
            devices.remove(index);
            return true;
        }
        else
        {
            return false;
        }
    }

}
