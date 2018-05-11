package com.skyfree.speed.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyfree.speed.R;
import com.skyfree.speed.utils.Utils;

import testhttp.jklib.JkActivity;
import testhttp.jklib.Singleton;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class FragmentTool extends Fragment {

    public JkActivity jkActivity;

    public static FragmentTool newInstance() {
        FragmentTool fragment = new FragmentTool();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        jkActivity = new JkActivity();
//        jkActivity.initJk(getContext().getPackageName(), Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID), getContext());
//        if(Utils.isConnected(getContext())){
//            openJk();
//        }
        return inflater.inflate(R.layout.fragment_tool, container, false);
    }

    public void openJk() {
        Intent myIntent = new Intent(getContext(), jkActivity.getClass());
        startActivity(myIntent);
    }

}