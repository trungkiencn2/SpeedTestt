package com.skyfree.speed.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.skyfree.speed.R;
import com.skyfree.speed.fragment.FragmentResult;
import com.skyfree.speed.fragment.FragmentSetting;
import com.skyfree.speed.fragment.FragmentSpeed;
import com.skyfree.speed.fragment.FragmentTool;
import com.skyfree.speed.utils.Utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.xml.transform.Transformer;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import testhttp.jklib.JkActivity;
import testhttp.jklib.Singleton;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent();
    }

    public void rateApp() {
        boolean showPopup = this.getSharedPreferences("MyPrefsFile", 0).getBoolean("showPopup", true);
        if (showPopup) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.popup_note);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK &&
                            event.getAction() == KeyEvent.ACTION_UP &&
                            !event.isCanceled()) {

                        dialog.cancel();
                        finish();
                        return true;
                    }
                    return false;
                }
            });
            dialog.show();

            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    startActivity(myIntent);

                    SharedPreferences.Editor prefsEditor = MainActivity.this.getSharedPreferences("MyPrefsFile", 0).edit();
                    prefsEditor.putBoolean("showPopup", true);
                    prefsEditor.commit();

                    dialog.cancel();
                    finish();
                }

            });


            Button btnDontShow = (Button) dialog.findViewById(R.id.btnDontShow);
            btnDontShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor prefsEditor = MainActivity.this.getSharedPreferences("MyPrefsFile", 0).edit();
                    prefsEditor.putBoolean("showPopup", true);
                    prefsEditor.commit();
                    dialog.cancel();
                    finish();
                }
            });

            Button btnLater = (Button) dialog.findViewById(R.id.btnLater);
            btnLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    finish();
                }
            });

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (Utils.isConnected(this)) {
            rateApp();
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

    }

    private void addEvent() {

        if (!Utils.isConnected(this)) {
            BottomBarTab tabTool = mBottomBar.getTabAtPosition(2);
            tabTool.setVisibility(View.GONE);
        }

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                Fragment selectedFragment = null;
                if (tabId == R.id.tab_speed) {
                    selectedFragment = FragmentSpeed.newInstance();
                    Utils.SPEED_CREATE = true;
                } else if (tabId == R.id.tab_results) {
                    selectedFragment = FragmentResult.newInstance();
                } else if (tabId == R.id.tab_tool) {
                    selectedFragment = FragmentTool.newInstance();
                } else if (tabId == R.id.tab_setting) {
                    selectedFragment = FragmentSetting.newInstance();
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });
//        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
//            @Override
//            public void onTabReSelected(int tabId) {
//                Fragment selectedFragment = null;
//                if (tabId == R.id.tab_tool) {
//                    selectedFragment = FragmentTool.newInstance();
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.frame_layout, selectedFragment);
//                    transaction.commit();
//                }
//
//            }
//        });//
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, FragmentSpeed.newInstance());
        transaction.commit();
    }



}
