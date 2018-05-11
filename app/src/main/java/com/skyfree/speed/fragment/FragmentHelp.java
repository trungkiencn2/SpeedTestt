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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class FragmentHelp extends Fragment {

    private ImageView mImgBack;
    private TextView mTvContent;
    private View view;

    public static FragmentHelp newInstance() {
        FragmentHelp fragment = new FragmentHelp();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);
        initView();
        addEvent();
        return view;
    }

    private void initView(){
        mImgBack = (ImageView) view.findViewById(R.id.img_back_fragment_help);
        mTvContent = (TextView) view.findViewById(R.id.tv_help_content);
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
        String strPart = getString(R.string.help);
        String strPart1 = getString(R.string.help_one);
        String strPart2 = getString(R.string.help_two);
        String strPart3 = getString(R.string.help_three);
        String strPart4 = getString(R.string.help_four);
        String strPart5 = getString(R.string.help_five);
        String strPart6 = getString(R.string.help_six);

        SpannableString spannableString = new SpannableString(strPart);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0,spannableString.length(), 0);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString.length(), 0);

        final SpannableString spannableString1 = new SpannableString(strPart1);
        spannableString1.setSpan(new URLSpan("https://support.speedtest.net/hc/en-us/articles/203845290-What-is-ping-download-speed-and-upload-speed-"), 0, spannableString1.length(), 0);

        final SpannableString spannableString2 = new SpannableString(strPart2);
        spannableString2.setSpan(new URLSpan("https://support.speedtest.net/hc/en-us/articles/203845400-How-does-the-test-itself-work-How-is-the-result-calculated-"), 0, spannableString2.length(), 0);

        final SpannableString spannableString3 = new SpannableString(strPart3);
        spannableString3.setSpan(new URLSpan("https://support.speedtest.net/hc/en-us/articles/203845190-How-do-I-start-a-speed-test-"), 0, spannableString3.length(), 0);

        final SpannableString spannableString4 = new SpannableString(strPart4);
        spannableString4.setSpan(new URLSpan("https://support.speedtest.net/hc/en-us/articles/203845230-What-do-mbps-and-kbps-mean-"), 0, spannableString4.length(), 0);

        final SpannableString spannableString5 = new SpannableString(strPart5);
        spannableString5.setSpan(new URLSpan("https://support.speedtest.net/hc/en-us/articles/203845210-What-speeds-do-I-need-for-Skype-Netflix-video-games-etc-"), 0, spannableString5.length(), 0);

        final SpannableString spannableString6 = new SpannableString(strPart6);
        spannableString6.setSpan(new URLSpan("https://support.speedtest.net/hc/en-us/articles/210867718-Using-Speedtest-net-with-an-ad-blocker"), 0, spannableString6.length(), 0);

        final Pattern pattern1 = Pattern.compile("Click here");
        ClickableSpan clickableSpan1 = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                Linkify.addLinks(spannableString1, pattern1, null, null, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "https://support.speedtest.net/hc/en-us/articles/203845290-What-is-ping-download-speed-and-upload-speed-";
                    }
                });
            }
        };

        final Pattern pattern2 = Pattern.compile("Click here");
        ClickableSpan clickableSpan2 = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                Linkify.addLinks(spannableString2, pattern2, null, null, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "https://support.speedtest.net/hc/en-us/articles/203845400-How-does-the-test-itself-work-How-is-the-result-calculated-";
                    }
                });
            }
        };

        final Pattern pattern3 = Pattern.compile("Click here");
        ClickableSpan clickableSpan3 = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                Linkify.addLinks(spannableString3, pattern3, null, null, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "https://support.speedtest.net/hc/en-us/articles/203845190-How-do-I-start-a-speed-test-";
                    }
                });
            }
        };

        final Pattern pattern4 = Pattern.compile("Click here");
        ClickableSpan clickableSpan4 = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                Linkify.addLinks(spannableString4, pattern4, null, null, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "https://support.speedtest.net/hc/en-us/articles/203845230-What-do-mbps-and-kbps-mean-";
                    }
                });
            }
        };

        final Pattern pattern5 = Pattern.compile("Click here");
        ClickableSpan clickableSpan5 = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                Linkify.addLinks(spannableString5, pattern5, null, null, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "https://support.speedtest.net/hc/en-us/articles/203845210-What-speeds-do-I-need-for-Skype-Netflix-video-games-etc-";
                    }
                });
            }
        };

        final Pattern pattern6 = Pattern.compile("Click here");
        ClickableSpan clickableSpan6 = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                Linkify.addLinks(spannableString6, pattern6, null, null, new Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return "https://support.speedtest.net/hc/en-us/articles/210867718-Using-Speedtest-net-with-an-ad-blocker";
                    }
                });
            }
        };

        spannableString1.setSpan(clickableSpan1, 0, spannableString1.length(), 0);
        spannableString2.setSpan(clickableSpan2, 0, spannableString2.length(), 0);
        spannableString3.setSpan(clickableSpan3, 0, spannableString3.length(), 0);
        spannableString4.setSpan(clickableSpan4, 0, spannableString4.length(), 0);
        spannableString5.setSpan(clickableSpan5, 0, spannableString5.length(), 0);
        spannableString6.setSpan(clickableSpan6, 0, spannableString6.length(), 0);

        mTvContent.setLinksClickable(true);
        mTvContent.setMovementMethod(LinkMovementMethod.getInstance());

        mTvContent.setText(spannableString);
        mTvContent.append("\n\n");
        mTvContent.append(spannableString1);
        mTvContent.append("\n");
        mTvContent.append(spannableString2);
        mTvContent.append("\n");
        mTvContent.append(spannableString3);
        mTvContent.append("\n");
        mTvContent.append(spannableString4);
        mTvContent.append("\n");
        mTvContent.append(spannableString5);
        mTvContent.append("\n");
        mTvContent.append(spannableString6);
    }

}
