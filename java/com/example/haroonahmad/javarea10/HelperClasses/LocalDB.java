package com.example.haroonahmad.javarea10.HelperClasses;

import com.example.haroonahmad.javarea10.Models.Device;
import com.example.haroonahmad.javarea10.Models.Room;
import com.example.haroonahmad.javarea10.Models.SwitchBoard;

import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 2/28/2017.
 */

public  class LocalDB {
    private static LocalDB instance=null;
    static ArrayList<Room> rooms;
    static  ArrayList<SwitchBoard> switchBoards;
    static ArrayList<Device> devices;

    public static ArrayList<SwitchBoard> getSwitchBoards() {
        return switchBoards;
    }

    public static void setSwitchBoards(ArrayList<SwitchBoard> switchBoards) {
        LocalDB.switchBoards = switchBoards;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }
    public ArrayList<Room> getRooms() {
        return rooms;
    }


    public static void setDevices(ArrayList<Device> devices) {
        LocalDB.devices = devices;
    }

    protected LocalDB() {
    }

    public static LocalDB getInstance()
    {
        if(instance==null)
        {
            instance=new LocalDB();
        }
        return instance;
    }




    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public LocalDB(ArrayList<Room> rooms) {

        this.rooms = rooms;
    }
}
