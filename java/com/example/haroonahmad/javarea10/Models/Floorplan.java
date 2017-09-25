package com.example.haroonahmad.javarea10.Models;

import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 11/21/2016.
 */

public class Floorplan {
    int floorID;
    String name;
    ArrayList<Room> rooms;

    public int getFloorID() {
        return floorID;
    }

    public void setFloorID(int floorID) {
        this.floorID = floorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public Floorplan(int floorID, String name, ArrayList<Room> rooms) {

        this.floorID = floorID;
        this.name = name;
        this.rooms = rooms;
    }
    /*private int findRoom(int _roomID)
    {
        for(int i=0;i<rooms.size();++i)
        {
            if(rooms.get(i).getRoomID()==_roomID)
            {
                return i;
            }
        }
        return  -1;
    }*/
    /*public Room getRoom(int _roomID)
    {
        int index=findRoom(_roomID);
        if(index>-1)
        {
            return rooms.get(index);
        }
        else
        {
            return null;
        }

    }*/
    /*public boolean deleteRoom(int _roomID)
    {
        int index=findRoom(_roomID);
        if(index>-1)
        {
            rooms.remove(index);
            return true;
        }
        else
        {
            return false;
        }
    }*/
}
