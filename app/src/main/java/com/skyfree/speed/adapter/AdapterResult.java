package com.skyfree.speed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyfree.speed.R;
import com.skyfree.speed.model.Result;
import com.skyfree.speed.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by KienBeu on 4/26/2018.
 */

public class AdapterResult extends RecyclerView.Adapter<AdapterResult.ViewHolder>{

    private ArrayList<Result> mListResult;
    private Context mContext;

    public AdapterResult(ArrayList<Result> mListResult, Context mContext) {
        this.mListResult = mListResult;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_result, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Calendar mCa = Calendar.getInstance();
        mCa.setTimeInMillis(Long.parseLong(mListResult.get(position).getDate()));

        holder.mImgType.setImageResource(mListResult.get(position).getType());
        holder.mTvDate.setText(dateFormat.format(mCa.getTime()));
//        holder.mTvPing.setText(Utils.round(Float.parseFloat(mListResult.get(position).getPing()), 0) + "ms");
        holder.mTvPing.setText(Math.round(Float.parseFloat(mListResult.get(position).getPing())) + "ms");
        if(Utils.UNIT.equals(Utils.MBPS)){
            holder.mTvDownload.setText(Utils.round((float) (Float.parseFloat(mListResult.get(position).getSpeedDownload())), 2) + "");
            holder.mTvUpload.setText(Utils.round((float) (Float.parseFloat(mListResult.get(position).getSpeedUpload())), 2) + "");
        }else if(Utils.UNIT.equals(Utils.MBS)){
            holder.mTvDownload.setText(Utils.round((float) (Float.parseFloat(mListResult.get(position).getSpeedDownload()) * 0.125), 2) + "");
            holder.mTvUpload.setText(Utils.round((float) (Float.parseFloat(mListResult.get(position).getSpeedUpload()) * 0.125), 2) + "");
        }else if(Utils.UNIT.equals(Utils.KBS)){
            holder.mTvDownload.setText(Utils.round((float) (Float.parseFloat(mListResult.get(position).getSpeedDownload()) * 125), 2) + "");
            holder.mTvUpload.setText(Utils.round((float) (Float.parseFloat(mListResult.get(position).getSpeedUpload()) * 125), 2) + "");
        }
    }

    @Override
    public int getItemCount() {
        return mListResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImgType;
        private TextView mTvDate, mTvDownload, mTvUpload, mTvPing;

        public ViewHolder(View itemView) {
            super(itemView);
            mImgType = (ImageView) itemView.findViewById(R.id.img_type_item_result);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date_item_result);
            mTvDownload = (TextView) itemView.findViewById(R.id.tv_download_item_result);
            mTvUpload = (TextView) itemView.findViewById(R.id.tv_upload_item_result);
            mTvPing = (TextView) itemView.findViewById(R.id.tv_ping_item_result);
        }
    }
}
