package com.skyfree.speed.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import android.os.Handler;

/**
 * @author erdigurbuz
 */
public class PingTest extends Thread {

    HashMap<String, Object> result = new HashMap<>();
    String server = "";
    int count;
    double instantRtt = 0;
    double avgRtt = 0.0;
    boolean finished = false;
    boolean started = false;

    public PingTest(String serverIpAddress, int pingTryCount) {
        this.server = serverIpAddress;
        this.count = pingTryCount;
    }

    public double getAvgRtt() {
        return avgRtt;
    }

    public double getInstantRtt() {
        return instantRtt;
    }

    public boolean isFinished() {
        return finished;
    }

    public double getLatency(String ipAddress){
        String pingCommand = "/system/bin/ping -w 3 -c " + 3 + " " + ipAddress;
        String inputLine = "";
        double avgRtt = 0;

        try {
            Process process = Runtime.getRuntime().exec(pingCommand);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            inputLine = bufferedReader.readLine();
            while ((inputLine != null)) {
                if (inputLine.length() > 0 && inputLine.contains("avg")) {
                    break;
                }

                inputLine = bufferedReader.readLine();
                instantRtt = Double.parseDouble(inputLine.split(" ")[inputLine.split(" ").length - 2].replace("time=", ""));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String afterEqual = inputLine.substring(inputLine.indexOf("="), inputLine.length()).trim();
        String afterFirstSlash = afterEqual.substring(afterEqual.indexOf('/') + 1, afterEqual.length()).trim();
        String strAvgRtt = afterFirstSlash.substring(0, afterFirstSlash.indexOf('/'));
        avgRtt = Double.valueOf(strAvgRtt);
        return avgRtt;
    }

    @Override
    public void run() {
        try {
            avgRtt = getLatency(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finished = true;
    }
}
