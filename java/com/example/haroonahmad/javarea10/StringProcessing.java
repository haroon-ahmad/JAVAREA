package com.example.haroonahmad.javarea10;

//package com.example.haroonahmad.bluetooth;
//package stringprocessing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class StringProcessing {

    private static StringProcessing instance = null;

    protected StringProcessing() {
        // Exists only to defeat instantiation.
    }

    public static StringProcessing getInstance() {
        if(instance == null) {
            instance = new StringProcessing();
        }
        return instance;
    }

    static HashMap<String,String> dictionary=new HashMap(); //Key:word Value:ID
    static HashMap<String,String> deviceDictionary=new HashMap(); //Key:word Value:r-portNumber or key:word Value:d-RoomID.
    static HashMap<String,String> roomDictionary=new HashMap(); //Key:word Value:r-portNumber or key:word Value:d-RoomID.
    static HashSet<String> ordinaryWordsDictionary=new HashSet();
    //Add common ordinary words like house,building,devices,room----> all words that you dont want to allow mapping to.

    static List<Integer> ListForScores=new ArrayList();
    static HashMap<String,Integer> MapForScores=new HashMap();

    static {
        ordinaryWordsDictionary.add("house");
        ordinaryWordsDictionary.add("home");
        ordinaryWordsDictionary.add("power");
        ordinaryWordsDictionary.add("building");
        ordinaryWordsDictionary.add("device");
        ordinaryWordsDictionary.add("devices");
        ordinaryWordsDictionary.add("room");
        ordinaryWordsDictionary.add("rooms");
        ordinaryWordsDictionary.add(".");
        ordinaryWordsDictionary.add(",");
        ordinaryWordsDictionary.add("turn");
        ordinaryWordsDictionary.add("switch");
        ordinaryWordsDictionary.add("machine");
        ordinaryWordsDictionary.add("lights");
        ordinaryWordsDictionary.add("fans");
        ordinaryWordsDictionary.add("sensors");
        ordinaryWordsDictionary.add("LEDs"); //Check how LED's is captured by google api.


    }
    static class Command {
        String bluetoothID;
        String portNum;
        Integer action;
        Integer floor;
        boolean all;

        Command(){
            bluetoothID="-99";
            portNum="-99";
            action=-99;
            floor=0;
            all=false;
        }

    }
    static  {
        //command-room-component.
        //HashMap containing some basic electrical commands and their ID's.
        //Commands

        //For room: We need to return bluetooth ID.
        //For device: We need to return port number.

        dictionary.put("on", "pc0");//pc0==positive command 0th term
        dictionary.put("activate", "pc1");
        dictionary.put("restart", "pc2");
        dictionary.put("start", "pc3");
        dictionary.put("up", "pc4");
        dictionary.put("trigger", "pc5");
        dictionary.put("open", "pc6");
        dictionary.put("enable", "pc7");
        dictionary.put("unlock", "pc8");


        dictionary.put("off", "nc0");//nc1==negative command 1st term
        dictionary.put("deactivate", "nc1");
        dictionary.put("down", "nc2");
        dictionary.put("stop", "nc3");
        dictionary.put("shut", "nc4");
        dictionary.put("shutdown", "nc5");
        dictionary.put("close", "nc6");
        dictionary.put("disable", "nc7");
        dictionary.put("lock", "nc8");
        dictionary.put("of", "nc9");
        dictionary.put("aun", "nc10");


    }

    public static String getValueFromDeviceDictionary(String label){
        if (deviceDictionary.containsKey(label))
        {
            String toBeReturned=deviceDictionary.get(label);
            return toBeReturned;
        }
        return "-1";
    }
    public static String getValueFromRoomDictionary(String label){
        if (roomDictionary.containsKey(label))
        {
            String toBeReturned=roomDictionary.get(label);
            return toBeReturned;
        }
        return "-1";
    }
    /*public static String getKeyFromDeviceDictionary(String portNum){
        if (deviceDictionary.containsValue(portNum))
        {
            String toBeReturned=deviceDictionary.get;
            return toBeReturned;
        }
        return "-1";
    }*/

    public static int addInDeviceDictionary(String label,String portNum){
        if (!deviceDictionary.containsKey(label) && label!= null && !label.isEmpty() && portNum!=null && !portNum.isEmpty())
        {
            //Allow multiple similar named devices.
            label=label.toLowerCase();
            deviceDictionary.put(label,"d$"+portNum);
            return 1;
        }
        if(deviceDictionary.containsKey(label) || roomDictionary.containsKey(label))
        {
            return -1;

        }
        if(label.equals(""))
        {
            return -2;
        }
        if(portNum.equals(""))
        {
            return -3;
        }
        return -1;
    }
    public static int addInRoomDictionary(String label,String bluetoothID){
        if (!roomDictionary.containsKey(label) && label!= null && !label.isEmpty() && bluetoothID!=null && !bluetoothID.isEmpty())
        {
            label=label.toLowerCase();
            if(label.contains("\'"))
            {
                label=label.replaceAll("\'", "-");
                ////System.out.println("label: "+ label);
                String[] Split=label.split("-");
                ////System.out.println("Split:"+ Split[0]);
                roomDictionary.put(Split[0],"r$"+bluetoothID);
            }
            else
                roomDictionary.put(label,"r$"+bluetoothID);
            return 1;

        }
        if(deviceDictionary.containsKey(label) || roomDictionary.containsKey(label))
        {
            return -1;

        }
        if(label.equals(""))
        {
            return -2;
        }
        if(bluetoothID.equals(""))
        {
            return -3;
        }
        return -1;
    }

    public static Command extractCommandFromString(String str)
    {
        if (str.length() == 0) {  //handling of extreme cases.
            throw new IllegalArgumentException("Please Repeat.");
        }
        boolean deviceFlag=false; //device name that does not have any specified name.
        boolean roomFlag=false; //room name that does not have any specified name.
        boolean isOrdinary=false;
        boolean InDeviceDictionaryFlag=false;
        boolean InRoomDictionaryFlag=false;
        String portFromMap;
        String bluetoothIDFromMap;
        String neededName="-99";


        String[] categoryCheck=new String[2];
        str=str.toLowerCase();

        //System.out.println("new str: "+str);
        Command inputCommand=new Command();
        String[] TokensOfInputString = str.split("\\s+");
        int indexOfToken=0;

        for (String Token : TokensOfInputString){

            indexOfToken++;
            ////System.out.println("\nToken Encountered: " + Token);

            if(Token.equals("all") || Token.equals("complete"))
            {
                inputCommand.all=true;
                ////System.out.println("So true.");
            }

            if(ordinaryWordsDictionary.contains(Token))
            {
                isOrdinary=true;
                //////System.out.println("This is that token: "+ Token);
            }

            if(deviceDictionary.containsKey(Token))
            {
                String Split[]=new String[2];
                Split=deviceDictionary.get(Token).split("\\$");
                portFromMap=Split[1];
                ////System.out.println("From Device Dictionary: "+Token+" portNum:"+portFromMap);

                //inputCommand.portNum=portFromMap.toString();
                inputCommand.portNum=Token;

                InDeviceDictionaryFlag=true;
            }
            else if(roomDictionary.containsKey(Token))
            {
                String Split[]=new String[2];
                Split=roomDictionary.get(Token).split("\\$");
                bluetoothIDFromMap=Split[1];
                ////System.out.println("From Room Dictionary: "+Token+" BluetoothNum:"+bluetoothIDFromMap);
                //inputCommand.bluetoothID=bluetoothIDFromMap;
                inputCommand.bluetoothID=Token;

                ////System.out.println("BluetoothID Post: "+inputCommand.bluetoothID); //Room
                InRoomDictionaryFlag=true;
            }
            /*else if(Token.equals("(d)"))
            {
                deviceFlag=true;
                continue;
            }
            else if(Token.equals("(r)"))
            {
                roomFlag=true;
                continue;
            }*/

            else if (dictionary.containsKey(Token) && !InDeviceDictionaryFlag &&!InRoomDictionaryFlag){
                //System.out.println("Token Found: "+Token);

                String c = dictionary.get(Token).toString();
                if (c.equals("pc0")||c.equals("pc1")||c.equals("pc2")||c.equals("pc3")||c.equals("pc4")||c.equals("pc5")||c.equals("pc6")||c.equals("pc7")||c.equals("pc8"))
                {
                    inputCommand.action = 1; //TURN ON!
                }
                else if (c.equals("nc0")||c.equals("nc1")||c.equals("nc2")||c.equals("nc3")||c.equals("nc4")||c.equals("nc5")||c.equals("nc6")||c.equals("nc7")||c.equals("nc8")||c.equals("nc9")||c.equals("nc10"))
                {
                    inputCommand.action = 0; //TURN OFF!
                    if(Token.equals("off"))
                    {
                        ////System.out.println("index: "+TokensOfInputString[indexOfToken-2]);
                        if(indexOfToken!=0 && TokensOfInputString[indexOfToken-2].equals("start"))
                            inputCommand.action=1;
                    }


                }
                else if(deviceFlag)
                {
                    deviceFlag=false;
                    //inputCommand.portNum = c; ////////////////////
                    inputCommand.portNum=Token;
                }
                else if(roomFlag)
                {
                    roomFlag=false;
                    inputCommand.bluetoothID = c; ///////////////////
                    inputCommand.bluetoothID=Token;

                }


            }
            else if(!Token.equalsIgnoreCase("") && !Token.equals("all") && !Token.equals("complete") && !isOrdinary)
            {

                dictionary.putAll(roomDictionary); //Now the big dictionary contains all the other two dictionaries.
                dictionary.putAll(deviceDictionary);

                //System.out.println("Token To Be Matched: "+Token);
                for (Map.Entry<String,String> entry : dictionary.entrySet()) {
                    String key = entry.getKey();
                    Integer score= minDistance(key,Token);
                    //System.out.print("Score: "+ score + "between "+ key + " " +Token + "\n");
                    ListForScores.add(score);
                    MapForScores.put(key, score); //Store each string and its score with StringToBeMatched.
                    ////System.out.println("Token: "+Token+" Key: "+key+" : "+score);

                }
                int minimum=0;
                minimum = getMin(ListForScores);
                //System.out.print("\nMinimum Score: "+ minimum +"\n");
                List<String> multipleMinValues=new ArrayList();
                for(Map.Entry<String,Integer> entry:MapForScores.entrySet())
                {
                    if(entry.getValue()== minimum){
                        ////System.out.println("Match: "+entry.getKey()+" Score: "+entry.getValue());
                        String s=dictionary.get(entry.getKey());
                        //System.out.println("Closest Match: "+entry.getKey());
                        multipleMinValues.add(entry.getKey());
                    }

                }

                if(multipleMinValues.size()==1){ //only one match.
                    ////System.out.println("First and Only Match: "+ dictionary.get(multipleMinValues.get(0)));
                    try
                    {
                        if(!dictionary.get(multipleMinValues.get(0)).equals(null) && dictionary.get(multipleMinValues.get(0)).contains("$"))
                        {
                            categoryCheck =dictionary.get(multipleMinValues.get(0)).split("\\$");
                            ////System.out.println("categoryCheck" +categoryCheck[0] + categoryCheck[1]);
                            //}
                            if(categoryCheck[0].equals("d"))
                            {
                                deviceFlag=false;
                                //inputCommand.portNum=categoryCheck[1];
                                inputCommand.portNum=multipleMinValues.get(0);
                            }
                            else if(categoryCheck[0].equals("r"))
                            {
                                roomFlag=false;
                                //inputCommand.bluetoothID=categoryCheck[1];
                                inputCommand.bluetoothID=multipleMinValues.get(0);

                            }
                        }
                        else if(roomFlag)
                        {
                            roomFlag=false;
                            //inputCommand.bluetoothID=dictionary.get(multipleMinValues.get(0));
                            inputCommand.bluetoothID=multipleMinValues.get(0);

                        }
                        else if(deviceFlag)
                        {
                            deviceFlag=false;
                            //inputCommand.portNum=dictionary.get(multipleMinValues.get(0));
                            inputCommand.portNum=multipleMinValues.get(0);

                        }
                    }
                    catch (NumberFormatException e) {
                        return inputCommand;
                    }

                }
                else if(multipleMinValues.size()>1)
                { //more than one matches with same score.
                    Integer minValue=-99;
                    categoryCheck[0]="-99";
                    categoryCheck[1]="-99";
                    ////System.out.println("what is this:" + dictionary.get(multipleMinValues.get(0)));
                    if(!dictionary.get(multipleMinValues.get(0)).equals(null) && dictionary.get(multipleMinValues.get(0)).contains("$")){
                        categoryCheck =dictionary.get(multipleMinValues.get(0)).split("\\$");
                        ////System.out.println("categoryCheck: " +categoryCheck[0]+","+ categoryCheck[1]);
                        minValue=Integer.valueOf(categoryCheck[1]);
                    }
                    else{
                        try
                        {
                            minValue=Integer.valueOf(dictionary.get(multipleMinValues.get(0)));
                        }////System.out.println("External Min Value: "+dictionary.get(minValue));
                        catch(NumberFormatException e)
                        {

                        }
                    }
                    for(int k=1;k<multipleMinValues.size();k++)
                    {
                        ////System.out.println("Competitors: "+dictionary.get(multipleMinValues.get(k)));
                        if(!dictionary.get(multipleMinValues.get(0)).equals(null) && dictionary.get(multipleMinValues.get(k)).contains("$")){
                            categoryCheck = dictionary.get(multipleMinValues.get(k)).split("\\$");
                            neededName=multipleMinValues.get(k);

                            ////System.out.println("categoryCheck: " +categoryCheck[0]+","+ categoryCheck[1]);
                            if(Integer.valueOf(categoryCheck[1])<minValue)
                            {
                                minValue=Integer.valueOf(categoryCheck[1]);
                                neededName=multipleMinValues.get(k);
                            }
                        }
                        else{
                            try
                            {
                                if(Integer.valueOf(dictionary.get(multipleMinValues.get(k)))<minValue)
                                {
                                    minValue=Integer.valueOf(dictionary.get(multipleMinValues.get(k)));
                                    ////System.out.println("Internal Min Value: "+dictionary.get(minValue));

                                }
                                neededName=multipleMinValues.get(k);
                            }
                            catch(NumberFormatException e)
                            {
                                continue;
                            }
                        }

                    }
                    ////System.out.println("Post Loop Min Value: "+minValue);
                    if(roomFlag)
                    {
                        roomFlag=false;
                        //inputCommand.bluetoothID=minValue.toString();
                        inputCommand.bluetoothID=neededName;

                    }
                    else if(deviceFlag)
                    {
                        deviceFlag=false;
                        //inputCommand.portNum=minValue.toString();
                        inputCommand.portNum=neededName;

                    }
                    else if(!categoryCheck[0].equals(null) && categoryCheck[0].equals("d"))
                    {
                        deviceFlag=false;
                        //inputCommand.portNum=categoryCheck[1];
                        inputCommand.portNum=neededName;

                    }
                    else if(!categoryCheck[0].equals(null) && categoryCheck[0].equals("r"))
                    {
                        roomFlag=false;
                        //inputCommand.bluetoothID=categoryCheck[1];
                        inputCommand.bluetoothID=neededName;

                    }


                }
            }
            roomFlag=false;
            deviceFlag=false;
            isOrdinary=false;
            InDeviceDictionaryFlag=false;
            InRoomDictionaryFlag=false;
            ListForScores.clear();
            MapForScores.clear();

        }
        //System.out.println("\nPortNum: "+inputCommand.portNum); //Device
        //System.out.println("Action : "+inputCommand.action); //Action
        //System.out.println("BluetoothID : "+inputCommand.bluetoothID); //Room
//
        return inputCommand;


    }
    public static int getMin(List<Integer> inputArray){
        int minValue = inputArray.get(0);
        for(int i=1;i<inputArray.size();i++){
            if(inputArray.get(i) < minValue){
                minValue = inputArray.get(i);
            }
        }
        return minValue;
    }

    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }
    public static void checkFeedback(int feedback){
        if(feedback==-1)
        {
            //System.out.println("Name already exists.Please enter another name for your device.");
        }
        if(feedback==-2)
        {
            //System.out.println("Blank space. Please enter a name for your device\n");
        }
        if(feedback==-3)
        {
            //System.out.println("Please enter portnumber\n");
        }
    }

}

