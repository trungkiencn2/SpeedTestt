package com.skyfree.speed.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.skyfree.speed.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import testhttp.jklib.Singleton;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by KienBeu on 4/23/2018.
 */

public class Utils {

    public static boolean SPEED_CREATE = false;

    public static final String BA_G = "BA_G";
    public static final String WIFI = "WIFI";
    public static final String NO_INTERNET = "NO_INTERNET";
    public static String TYPE_INTERNET;

    public static final String UNIT_STR = "UNIT_STR";
    public static final String MBPS = "MBPS";
    public static final String MBS = "MBS";
    public static final String KBS = "KBS";
    public static String UNIT = KBS;

    public static String getTypeInternet(Context mContext) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            Integer linkSpeed = wifiInfo.getLinkSpeed();
        }
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);

        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if (is3g) {
            return BA_G;
        } else if (isWifi) {
            return WIFI;
        }else {
            return NO_INTERNET;
        }
    }

    public static int getLengthOfString(String str) {
        return str.length();
    }

    public static void sendByGmail(Context mContext, String info, String mGmail) {
        Intent gmail = new Intent(Intent.ACTION_SEND);
        gmail.setPackage("com.google.android.gm");
        gmail.putExtra(Intent.EXTRA_EMAIL, new String[]{mGmail});
        gmail.setData(Uri.parse(mGmail));
        gmail.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.subject_of_email));
        gmail.setType("plain/text");
        gmail.putExtra(Intent.EXTRA_TEXT, info);
        mContext.startActivity(gmail);
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static long getPing(){
        String host = "172.16.0.2";
        long BeforeTime = System.currentTimeMillis();
        try {
            boolean reachable =  InetAddress.getByName(host).isReachable(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long AfterTime = System.currentTimeMillis();
        Long TimeDifference = AfterTime - BeforeTime;
        return TimeDifference;
    }

    private URL getUrl(int responseSize) throws MalformedURLException {
        return new URL(String.format("http://ping.yousense.org/ping/%d/", responseSize));
    }

    public static void sendPassToMail(Context mContext, String info) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"someone@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.subject_of_email));
        i.putExtra(Intent.EXTRA_TEXT, info);
        try {
            mContext.startActivity(Intent.createChooser(i, mContext.getString(R.string.send)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean canShowJk(Context mContext) {
        if (isConnected(mContext)) {
            if (Singleton.getInstance().dataArr.size() == 0)
                return false;
            if (Singleton.getInstance().dataArr.size() != Singleton.getInstance().countLoadImage)
                return false;

            return true;
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable() && conMgr
                .getActiveNetworkInfo().isConnected());
    }


}
