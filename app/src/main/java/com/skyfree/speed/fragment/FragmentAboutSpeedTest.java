package com.skyfree.speed.fragment;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyfree.speed.R;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class FragmentAboutSpeedTest extends Fragment {

    private ImageView mImgBack;
    private TextView mTvContent;
    private View view;

    public static FragmentAboutSpeedTest newInstance(){
        FragmentAboutSpeedTest fragment = new FragmentAboutSpeedTest();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_speed_test, container, false);
        initView();
        addEvent();
        return view;
    }

    private void initView(){
        mImgBack = (ImageView) view.findViewById(R.id.img_back_fragment_about_speed_test);
        mTvContent = (TextView) view.findViewById(R.id.tv_about_speed_test_content);
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = null;
                selectedFragment = FragmentSetting.newInstance();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });
    }

    private void addEvent(){
        String strContent = mTvContent.getText().toString();
        String strPart1 = strContent.substring(0, strContent.indexOf("AdChoices"));
        String strPart2 = "AdChoices";
        String strPart3 = strContent.substring(strContent.indexOf("AdChoices") + 9, strContent.length());

        SpannableString spannableString = new SpannableString(strPart2);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0,spannableString.length(), 0);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString.length(), 0);

        mTvContent.setText(strPart1);
        mTvContent.append(spannableString);
        mTvContent.append(strPart3);
    }
}
