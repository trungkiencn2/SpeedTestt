package com.skyfree.speed.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyfree.speed.R;
import com.skyfree.speed.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class FragmentPrivacyPolicy extends Fragment{

    private ImageView mImgBack;
    private TextView mTvContent;
    private View view;

    public static FragmentPrivacyPolicy newInstance() {
        FragmentPrivacyPolicy fragment = new FragmentPrivacyPolicy();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        initView();
        addEvent();
        return view;
    }

    private void initView(){
        mImgBack = (ImageView) view.findViewById(R.id.img_back_fragment_privacy_policy);
        mTvContent = (TextView) view.findViewById(R.id.tv_privacy_policy_content);
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
        String strPart1 = strContent.substring(0, strContent.indexOf("OpenSpeedtest.com"));

        String strPart4 = "OpenSpeedtest.com";
        String strPart5 = strContent.substring(strContent.indexOf("OpenSpeedtest.com") + Utils.getLengthOfString(strPart4), strContent.indexOf(getString(R.string.definitions)));

        String strPart6 = getString(R.string.definitions);
        String strPart7 = strContent.substring(strContent.indexOf(strPart6) + Utils.getLengthOfString(strPart6), strContent.indexOf(getString(R.string.our_commitment_to_your_privacy)));

        String strPart8 = getString(R.string.our_commitment_to_your_privacy);
        String strPart9 = strContent.substring(strContent.indexOf(strPart8) + Utils.getLengthOfString(strPart8), strContent.indexOf(getString(R.string.links_to_other_websites)));

        String strPart10 = getString(R.string.links_to_other_websites);
        String strPart11 = strContent.substring(strContent.indexOf(strPart10) + Utils.getLengthOfString(strPart10), strContent.length());

        final SpannableString spannableString4 = new SpannableString(strPart4);
        spannableString4.setSpan(new URLSpan("http://openspeedtest.com/"), 0, spannableString4.length(), 0);
        spannableString4.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString4.length(), 0);

        SpannableString spannableString6 = new SpannableString(strPart6);
        spannableString6.setSpan(new StyleSpan(Typeface.BOLD), 0,spannableString6.length(), 0);
        spannableString6.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString6.length(), 0);

        SpannableString spannableString8 = new SpannableString(strPart8);
        spannableString8.setSpan(new StyleSpan(Typeface.BOLD), 0,spannableString8.length(), 0);
        spannableString8.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString8.length(), 0);

        SpannableString spannableString10 = new SpannableString(strPart10);
        spannableString10.setSpan(new StyleSpan(Typeface.BOLD), 0,spannableString10.length(), 0);
        spannableString10.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString10.length(), 0);


        final Pattern pattern = Pattern.compile("Click here");
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                Linkify.addLinks(spannableString4, pattern, null, null, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "http://openspeedtest.com/";
                    }
                });
            }
        };

        spannableString4.setSpan(clickableSpan, 0, spannableString4.length(), 0);
        mTvContent.setLinksClickable(true);
        mTvContent.setMovementMethod(LinkMovementMethod.getInstance());

        mTvContent.setText(strPart1);
        mTvContent.append(spannableString4);
        mTvContent.append(strPart5);
        mTvContent.append("\n\n");
        mTvContent.append(spannableString6);
        mTvContent.append("\n");
        mTvContent.append(strPart7);
        mTvContent.append("\n\n");
        mTvContent.append(spannableString8);
        mTvContent.append("\n");
        mTvContent.append(strPart9);
        mTvContent.append("\n\n");
        mTvContent.append(spannableString10);
        mTvContent.append("\n");
        mTvContent.append(strPart11);
    }

}
