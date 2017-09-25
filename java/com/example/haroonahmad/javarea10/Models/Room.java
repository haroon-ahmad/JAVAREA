package com.example.haroonahmad.javarea10.Models;

import java.util.ArrayList;

/**
 * Created by Haroon Ahmad on 12/30/2016.
 */

public class Room {
    String roomID;
    String name;
    String type;
    ArrayList<SwitchBoard> switchboards;

    public String getRoomID() {
        return roomID;
    }

    public Room(String roomID, String name, String type) {
        this.roomID = roomID;
        this.name = name;
        this.type = type;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getName() {
        return name;
    }

    public Room() {
    }

    public Room(String roomID, String name) {

        this.roomID = roomID;
        this.name = name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public ArrayList<SwitchBoard> getSwitchboards() {
        return switchboards;
    }

    public void setSwitchboards(ArrayList<SwitchBoard> switchboards) {
        this.switchboards = switchboards;
    }

    public Room(String roomID, String name, ArrayList<SwitchBoard> switchboards) {

        this.roomID = roomID;
        this.name = name;
        this.switchboards = switchboards;
    }
    public  void addSwitchboard(SwitchBoard _newSwitchboard)
    {
        switchboards.add(_newSwitchboard);
    }

    public String getType() {
        return type;
    }

    private int findSwitchboard(String _switchboardID)
    {
        for(int i=0;i<switchboards.size();++i)
        {
            if(switchboards.get(i).getSwitchboardID().equals(_switchboardID))
            {
                return i;
            }
        }
        return  -1;
    }
    public SwitchBoard getSwitchBoard(String _switchboardID)
    {
        int index=findSwitchboard(_switchboardID);
        if(index>-1)
        {
            return switchboards.get(index);
        }
        else
        {
            return null;
        }

    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean deleteSwitchboard(String _switchboardID)
    {
        int index=findSwitchboard(_switchboardID);
        if(index>-1)
        {
            switchboards.remove(index);
            return true;
        }
        else
        {
            return false;
        }
    }
}

