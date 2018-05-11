package com.skyfree.speed.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.skyfree.speed.R;
import com.skyfree.speed.adapter.AdapterSetting;
import com.skyfree.speed.utils.Utils;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class FragmentSetting extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private CheckBox mCbMbps, mCbMbs, mCbKbs;
    private ListView mLvOption;
    private ArrayList<String> mListSetting;
    private AdapterSetting mAdapter;

    private Fragment selectedFragment = null;
    private FragmentTransaction transaction;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor mEditor;

    public static FragmentSetting newInstance() {
        FragmentSetting fragment = new FragmentSetting();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        mCbMbps = (CheckBox) v.findViewById(R.id.cb_mbps_setting);
        mCbMbs = (CheckBox) v.findViewById(R.id.cb_mbs_setting);
        mCbKbs = (CheckBox) v.findViewById(R.id.cb_kbs_setting);
        mLvOption = (ListView) v.findViewById(R.id.lv_option_setting);

        mCbMbps.setOnClickListener(this);
        mCbMbs.setOnClickListener(this);
        mCbKbs.setOnClickListener(this);
        mLvOption.setOnItemClickListener(this);

        addEvent();
        return v;
    }

    private void addEvent(){
        mListSetting = new ArrayList<>();
        mListSetting.add(getString(R.string.about_speed_test));
        mListSetting.add(getString(R.string.privacy_policy));
        mListSetting.add(getString(R.string.terms_of_use));
        mListSetting.add(getString(R.string.help));
        mListSetting.add(getString(R.string.give_us_feedback));
        mAdapter = new AdapterSetting(getContext(), mListSetting);
        mLvOption.setAdapter(mAdapter);

        sharedPreferences = getContext().getSharedPreferences("setting", MODE_PRIVATE);
        mEditor  = sharedPreferences.edit();
        Utils.UNIT = sharedPreferences.getString(Utils.UNIT_STR, Utils.MBPS);
        if(Utils.UNIT.equals(Utils.MBPS)){
            mCbMbps.setChecked(true);
        }else if(Utils.UNIT.equals(Utils.MBS)){
            mCbMbs.setChecked(true);
        }else if(Utils.UNIT.equals(Utils.KBS)){
            mCbKbs.setChecked(true);
        }
    }

    private void setDefaultCheckboxUnit(){
        mCbMbps.setChecked(false);
        mCbMbs.setChecked(false);
        mCbKbs.setChecked(false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cb_mbps_setting:
                setDefaultCheckboxUnit();
                mCbMbps.setChecked(true);
                mEditor.putString(Utils.UNIT_STR, Utils.MBPS);
                mEditor.apply();
                break;
            case R.id.cb_mbs_setting:
                setDefaultCheckboxUnit();
                mCbMbs.setChecked(true);
                mEditor.putString(Utils.UNIT_STR, Utils.MBS);
                mEditor.apply();
                break;
            case R.id.cb_kbs_setting:
                setDefaultCheckboxUnit();
                mCbKbs.setChecked(true);
                mEditor.putString(Utils.UNIT_STR, Utils.KBS);
                mEditor.apply();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (position){
            case 0:
                selectedFragment = FragmentAboutSpeedTest.newInstance();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                break;
            case 1:
                selectedFragment = FragmentPrivacyPolicy.newInstance();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                break;
            case 2:
                selectedFragment = FragmentTermsOfUse.newInstance();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                break;
            case 3:
                selectedFragment = FragmentHelp.newInstance();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                break;
            case 4:
                selectedFragment = FragmentGiveUsFeedback.newInstance();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                break;
        }
    }
}