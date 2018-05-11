package com.skyfree.speed.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyfree.speed.R;
import com.skyfree.speed.utils.Utils;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class FragmentGiveUsFeedback extends Fragment {

    private ImageView mImgBack;
    private TextView mTvOk;
    private EditText mEdtFeedBack;
    private View view;

    public static FragmentGiveUsFeedback newInstance() {
        FragmentGiveUsFeedback fragment = new FragmentGiveUsFeedback();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_give_us_feedback, container, false);
        initView();
        addEvent();
        return view;
    }

    private void initView(){
        mImgBack = (ImageView) view.findViewById(R.id.img_back_fragment_give_us_feedback);
        mTvOk = (TextView) view.findViewById(R.id.tv_ok_frag_give_us_feedback);
        mEdtFeedBack = (EditText) view.findViewById(R.id.edt_feedback);
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
        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEdtFeedBack.getText().toString().equals("")){
                    Utils.sendByGmail(getContext(), mEdtFeedBack.getText().toString(), "trungkiencn2@gmail.com");
                }
            }
        });
    }

}
