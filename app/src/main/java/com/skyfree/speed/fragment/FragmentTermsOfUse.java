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

public class FragmentTermsOfUse extends Fragment{

    private ImageView mImgBack;
    private TextView mTvContent;
    private View view;

    public static FragmentTermsOfUse newInstance() {
        FragmentTermsOfUse fragment = new FragmentTermsOfUse();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_terms_of_use, container, false);
        initView();
        addEvent();
        return view;
    }

    private void initView(){
        mImgBack = (ImageView) view.findViewById(R.id.img_back_fragment_terms_of_use);
        mTvContent = (TextView) view.findViewById(R.id.tv_terms_of_use_content);
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
        String strPart1 = strContent.substring(0, Utils.getLengthOfString(getString(R.string.speedtest_terms_conditions)));
        String strPart2 = getString(R.string.speedtest_terms_conditions);
        String strPart3 = strContent.substring(strContent.indexOf(strPart2) + Utils.getLengthOfString(strPart2), strContent.indexOf(getString(R.string.linking_policy)));

        String strPart4 = getString(R.string.linking_policy);
        String strPart5 = strContent.substring(strContent.indexOf(strPart4) + Utils.getLengthOfString(strPart4), strContent.length());

        SpannableString spannableString2 = new SpannableString(strPart2);
        spannableString2.setSpan(new StyleSpan(Typeface.BOLD), 0,spannableString2.length(), 0);
        spannableString2.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString2.length(), 0);

        final SpannableString spannableString4 = new SpannableString(strPart4);
        spannableString4.setSpan(new URLSpan("http://www.ziffdavis.com/ziff-davis-linking-policy"), 0, spannableString4.length(), 0);
        spannableString4.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString4.length(), 0);

        final Pattern pattern = Pattern.compile("Click here");
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                Linkify.addLinks(spannableString4, pattern, null, null, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "http://www.ziffdavis.com/ziff-davis-linking-policy";
                    }
                });
            }
        };

        spannableString4.setSpan(clickableSpan, 0, spannableString4.length(), 0);
        mTvContent.setLinksClickable(true);
        mTvContent.setMovementMethod(LinkMovementMethod.getInstance());

//        mTvContent.setText(strPart1);
        mTvContent.setText(spannableString2);
        mTvContent.append(strPart3);
        mTvContent.append(spannableString4);
        mTvContent.append(strPart5);
    }
}
