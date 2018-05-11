package com.skyfree.speed.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyfree.speed.R;
import com.skyfree.speed.adapter.AdapterResult;
import com.skyfree.speed.database.DataHandler;
import com.skyfree.speed.model.Result;
import com.skyfree.speed.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class FragmentResult extends Fragment implements View.OnClickListener {

    private TextView mTvDate, mTvDownload, mTvUpload, mTvPing;
    private ImageView mImgDelete, mImgShare;
    private RecyclerView mRcv;
    private View mView;

    private DataHandler mDb;
    private AdapterResult mAdapter;
    private ArrayList<Result> mListResult;

    public static FragmentResult newInstance() {
        FragmentResult fragment = new FragmentResult();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_result, container, false);
        mDb = new DataHandler(getContext());
        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        addEvent();
    }

    private void initView(){
        mTvDate = (TextView) mView.findViewById(R.id.tv_date_frag_result);
        mTvDownload = (TextView) mView.findViewById(R.id.tv_download_frag_result);
        mTvUpload = (TextView) mView.findViewById(R.id.tv_upload_frag_result);
        mTvPing = (TextView) mView.findViewById(R.id.tv_ping_frag_result);
        mImgDelete = (ImageView) mView.findViewById(R.id.img_delete_frag_result);
        mImgShare = (ImageView) mView.findViewById(R.id.img_share_frag_result);
        mRcv = (RecyclerView) mView.findViewById(R.id.rcv_result);

        mTvDate.setOnClickListener(this);
        mTvDownload.setOnClickListener(this);
        mTvUpload.setOnClickListener(this);
        mTvPing.setOnClickListener(this);
        mImgDelete.setOnClickListener(this);
        mImgShare.setOnClickListener(this);
    }

    private void addEvent(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", MODE_PRIVATE);
        Utils.UNIT = sharedPreferences.getString(Utils.UNIT_STR, Utils.MBPS);
        if (Utils.UNIT.equals(Utils.MBPS)) {
            mTvDownload.setText("Mbps");
            mTvUpload.setText("Mbps");
        } else if (Utils.UNIT.equals(Utils.MBS)) {
            mTvDownload.setText("MB/s");
            mTvUpload.setText("MB/s");
        } else if (Utils.UNIT.equals(Utils.KBS)) {
            mTvDownload.setText("KB/s");
            mTvUpload.setText("KB/s");
        }
        mListResult = new ArrayList<>();
        mListResult.addAll(mDb.getListResult());
        mAdapter = new AdapterResult(mListResult, getContext());
        mRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcv.setHasFixedSize(true);
        mRcv.setAdapter(mAdapter);
    }

    private boolean SORT_DOWNLOAD_UP = false;
    private boolean SORT_UPLOAD_UP = false;
    private boolean SORT_PING_UP = false;
    private boolean SORT_DATE_UP = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_date_frag_result:
                SORT_DATE_UP = !SORT_DATE_UP;
                Collections.sort(mListResult, new Comparator<Result>() {
                    @SuppressLint("NewApi")
                    @Override
                    public int compare(Result o1, Result o2) {
                        if(SORT_DATE_UP){
                            return Long.compare(Long.parseLong(o1.getDate()), Long.parseLong(o2.getDate()));
                        }else {
                            return Long.compare(Long.parseLong(o2.getDate()), Long.parseLong(o1.getDate()));
                        }
                    }
                });
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_download_frag_result:
                SORT_DOWNLOAD_UP = !SORT_DOWNLOAD_UP;
                Collections.sort(mListResult, new Comparator<Result>() {
                    @Override
                    public int compare(Result o1, Result o2) {
                        if(SORT_DOWNLOAD_UP){
                            return Float.compare(Float.parseFloat(o1.getSpeedDownload()), Float.parseFloat(o2.getSpeedDownload()));
                        }else {
                            return Float.compare(Float.parseFloat(o2.getSpeedDownload()), Float.parseFloat(o1.getSpeedDownload()));
                        }
                    }
                });
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_upload_frag_result:
                SORT_UPLOAD_UP = !SORT_UPLOAD_UP;
                Collections.sort(mListResult, new Comparator<Result>() {
                    @Override
                    public int compare(Result o1, Result o2) {
                        if(SORT_UPLOAD_UP){
                            return Float.compare(Float.parseFloat(o1.getSpeedUpload()), Float.parseFloat(o2.getSpeedUpload()));
                        }else {
                            return Float.compare(Float.parseFloat(o2.getSpeedUpload()), Float.parseFloat(o1.getSpeedUpload()));
                        }
                    }
                });
                mAdapter.notifyDataSetChanged();

                break;
            case R.id.tv_ping_frag_result:
                SORT_PING_UP = !SORT_PING_UP;
                Collections.sort(mListResult, new Comparator<Result>() {
                    @Override
                    public int compare(Result o1, Result o2) {
                        if(SORT_PING_UP){
                            return Float.compare(Float.parseFloat(o1.getPing()), Float.parseFloat(o2.getPing()));
                        }else {
                            return Float.compare(Float.parseFloat(o2.getPing()), Float.parseFloat(o1.getPing()));
                        }
                    }
                });
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.img_delete_frag_result:
                mDb.deleteAllResult();
                mListResult = mDb.getListResult();
                mAdapter = new AdapterResult(mListResult, getContext());
                mRcv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.img_share_frag_result:
                Utils.sendPassToMail(getContext(), "https://play.google.com/store/apps/details?id=" + getContext().getPackageName() + "\n" + getString(R.string.share_mail));
                break;
        }
    }
}
