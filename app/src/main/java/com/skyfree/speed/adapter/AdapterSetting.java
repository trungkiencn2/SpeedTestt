package com.skyfree.speed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skyfree.speed.R;

import java.util.ArrayList;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class AdapterSetting extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mListSetting;

    public AdapterSetting(Context mContext, ArrayList<String> mListSetting) {
        this.mContext = mContext;
        this.mListSetting = mListSetting;
    }

    @Override
    public int getCount() {
        return mListSetting.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mRow = inflater.inflate(R.layout.item_setting, null);
        TextView mTv = (TextView) mRow.findViewById(R.id.tv_name_item_setting);
        mTv.setText(mListSetting.get(position));
        return mRow;
    }
}
