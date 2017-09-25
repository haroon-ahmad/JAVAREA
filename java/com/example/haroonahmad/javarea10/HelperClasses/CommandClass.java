package com.example.haroonahmad.javarea10.HelperClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haroon Ahmad on 3/5/2017.
 */

public class CommandClass {
    String RoomID;
    String BoardID;
    String MacAddress;
    String DeviceID;
    String PortNum;
    Map<String,String> boardmap;



    public Map<String, String> getBoardmap() {
        return boardmap;
    }

    public void setBoardmap(Map<String, String> boardmap) {
        this.boardmap = boardmap;
    }

    public CommandClass() {
        boardmap=new HashMap<>();

    }

    public CommandClass(String roomID, String boardID, String macAddress, String deviceID, String portNum) {

        RoomID = roomID;
        BoardID = boardID;
        MacAddress = macAddress;
        DeviceID = deviceID;
        PortNum = portNum;
    }

    public String getRoomID() {

        return RoomID;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }

    public String getBoardID() {
        return BoardID;
    }

    public void setBoardID(String boardID) {
        BoardID = boardID;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getPortNum() {
        return PortNum;
    }

    public void setPortNum(String portNum) {
        PortNum = portNum;
    }
}
