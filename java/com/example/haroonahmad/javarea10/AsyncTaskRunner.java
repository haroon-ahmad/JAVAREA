package com.example.haroonahmad.javarea10;

import android.os.AsyncTask;
import android.util.Log;

import com.example.haroonahmad.javarea10.HelperClasses.ProcessedString;

/**
 * Created by Haroon Ahmad on 3/7/2017.
 */

public class AsyncTaskRunner extends AsyncTask<String, String, String> {


        private String resp;
        public AsyncResponse delegate = null;



        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                // Do your long operations here and return the processedString
                /*int time = Integer.parseInt(params[0]);
                // Sleeping for given time period
                Thread.sleep(time);
                resp = "Slept for " + time + " milliseconds";*/

                Log.d("asynctask", "doInBackground: ");
                //////////////////Manaal-Testing Dictionary Code
                StringProcessing obj=StringProcessing.getInstance();

                StringProcessing.Command processedString = obj.extractCommandFromString(String.valueOf(params[0]));

                //Toast.makeText(this,"Voice Command by Manaal" , Toast.LENGTH_SHORT).show();

                if(processedString.portNum=="-99" && processedString.bluetoothID=="-99" && processedString.all && processedString.action!=-99)
                    resp="\nAll devices in the house " + "and Action: "+ processedString.action;
                else if(processedString.portNum=="-99" && processedString.bluetoothID!="-99" && processedString.all)
                    resp="\nAll devices in Room: " + processedString.bluetoothID + "and Action: "+ processedString.action;
                 /*else if(!processedString.portNum.equals("-99") && processedString.bluetoothID.equals("-99") && !processedString.all)
                    System.out.println("Look for the room that has this device: "+ processedString.portNum);*/
                else if(processedString.portNum=="-99" || processedString.action==-99 || processedString.bluetoothID=="-99")
                    resp="\nPlease Repeat Voice Command"; //PUT THIS AS TOAST OR SOMETHING.
                else
                    resp=processedString.bluetoothID + ":" + processedString.portNum + ":" + processedString.action;

            } /*catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            }*/
            catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String processedString) {
            // execution of processedString of Long time consuming operation

            //finalresult.setText(processedString); //Check this from here.


            //finalresult.setText(processedString);
            //delegate.processFinish(processedString);

            Log.d("Async Task",processedString);
            ProcessedString p=new ProcessedString();
            p.setStr(processedString);

            /////SpeechSynthesis//////

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {
            //finalresult.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }

}



