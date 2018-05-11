package com.skyfree.speed.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.ImageSpeedometer;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.components.Indicators.ImageIndicator;
import com.skyfree.speed.R;
import com.skyfree.speed.database.DataHandler;
import com.skyfree.speed.model.Result;
import com.skyfree.speed.utils.GetSpeedTestHostsHandler;
import com.skyfree.speed.utils.HttpDownloadTest;
import com.skyfree.speed.utils.HttpUploadTest;
import com.skyfree.speed.utils.PingTest;
import com.skyfree.speed.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by KienBeu on 4/20/2018.
 */

public class FragmentSpeed extends Fragment implements View.OnClickListener {

    private TextView mTvResultDownload, mTvResultUpload, mTvUnitDownload, mTvUnitUpload;
    private TextView mTvPing, mTvJitter, mTvLoss, mTvAlert;
    private ImageView mImgGo;
    private ImageSpeedometer mSpeedView;
    private ProgressBar mProgressBar;
    private View v;
    private String mUrlDownload = "";
    private String mUrlUpload = "";

    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;

    private boolean mProgramRunning = false;
    private boolean focus = true;
    private float mPing, mDown, mUp;
    private DataHandler mDb;

    private Boolean pingTestStarted = false;
    private Boolean pingTestFinished = false;
    private Boolean downloadTestStarted = false;
    private Boolean downloadTestFinished = false;
    private Boolean uploadTestStarted = false;
    private Boolean uploadTestFinished = false;

    private int mCountUpdateProgress;
    private int mCountPing = 0;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mCountUpdateProgress += 1;
            mProgressBar.setProgress(mCountUpdateProgress);
            mHandler.postDelayed(runnable, 40);
        }
    };

    public static FragmentSpeed newInstance() {
        FragmentSpeed fragment = new FragmentSpeed();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_speed, container, false);
        mDb = new DataHandler(getContext());
        initView();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mSpeedView.setWithTremble(false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("setting", MODE_PRIVATE);
        Utils.UNIT = sharedPreferences.getString(Utils.UNIT_STR, Utils.MBPS);

        if (Utils.UNIT.equals(Utils.MBPS)) {
            mSpeedView.setUnit("MBPS");
            mSpeedView.setMinMaxSpeed(0, 90);
            mTvUnitDownload.setText("Mbps");
            mTvUnitUpload.setText("Mbps");
        } else if (Utils.UNIT.equals(Utils.MBS)) {
            mSpeedView.setUnit("MB/S");
            mSpeedView.setMinMaxSpeed(0, 9f);
            mTvUnitDownload.setText("MB/s");
            mTvUnitUpload.setText("MB/s");
        } else if (Utils.UNIT.equals(Utils.KBS)) {
            mSpeedView.setUnit("KB/s");
            mSpeedView.setMinMaxSpeed(0, 9000f);
            mTvUnitDownload.setText("KB/s");
            mTvUnitUpload.setText("KB/s");
        }

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler(getActivity());
        getSpeedTestHostsHandler.start();
    }

    private void initView() {
        mTvResultDownload = (TextView) v.findViewById(R.id.tv_result_download_frag_speed);
        mTvResultUpload = (TextView) v.findViewById(R.id.tv_result_upload_frag_speed);
        mTvUnitDownload = (TextView) v.findViewById(R.id.tv_unit_download_frag_speed);
        mTvUnitUpload = (TextView) v.findViewById(R.id.tv_unit_upload_frag_speed);
        mTvPing = (TextView) v.findViewById(R.id.tv_ping_frag_speed);
        mTvJitter = (TextView) v.findViewById(R.id.tv_jitter_frag_speed);
        mTvLoss = (TextView) v.findViewById(R.id.tv_loss_frag_speed);
        mTvAlert = (TextView) v.findViewById(R.id.tv_alert_frag_speed);
        mImgGo = (ImageView) v.findViewById(R.id.img_go_frag_speed);
        mSpeedView = (ImageSpeedometer) v.findViewById(R.id.speed_view);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        AnimationDrawable ad = getProgressBarAnimation();
        mProgressBar.setBackgroundDrawable(ad);
        mImgGo.setOnClickListener(this);
        ImageIndicator imageIndicator = new ImageIndicator(getContext(), R.drawable.ic_kim);
        mSpeedView.setIndicator(imageIndicator);
    }

    private AnimationDrawable getProgressBarAnimation() {
        GradientDrawable rainbow1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor("#4091bc"), Color.parseColor("#4f81b4"), Color.parseColor("#685398"),
                        Color.parseColor("#7b2a77"), Color.parseColor("#7b2775"), Color.parseColor("#74276f")});
        GradientDrawable[] gds = new GradientDrawable[]{rainbow1};
        AnimationDrawable animation = new AnimationDrawable();
        for (GradientDrawable gd : gds) {
            animation.addFrame(gd, 100);
        }
        animation.setOneShot(false);
        return animation;
    }

    private boolean mCheckPing = false;

    private void addEvent() {
        mProgramRunning = true;
        tempBlackList = new HashSet<>();
        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler(getActivity());
        getSpeedTestHostsHandler.start();

        if (getSpeedTestHostsHandler == null) {
            getSpeedTestHostsHandler = new GetSpeedTestHostsHandler(getActivity());
            getSpeedTestHostsHandler.start();
        }

        if (focus) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvAlert.setText(getString(R.string.looking_for_server));
                        }
                    });

                    int timeCount = 300;
                    while (!getSpeedTestHostsHandler.isFinished()) {
                        timeCount--;
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                        }
                    }

                    //Find closest server
                    HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                    HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                    double selfLat = getSpeedTestHostsHandler.getSelfLat();
                    double selfLon = getSpeedTestHostsHandler.getSelfLon();
                    double tmp = 19349458;
                    double dist = 0.0;
                    int findServerIndex = 0;
                    for (int index : mapKey.keySet()) {
                        if (tempBlackList.contains(mapValue.get(index).get(5))) {
                            continue;
                        }

                        Location source = new Location("Source");
                        source.setLatitude(selfLat);
                        source.setLongitude(selfLon);

                        List<String> ls = mapValue.get(index);
                        Location dest = new Location("Dest");
                        dest.setLatitude(Double.parseDouble(ls.get(0)));
                        dest.setLongitude(Double.parseDouble(ls.get(1)));

                        double distance = source.distanceTo(dest);
                        if (tmp > distance) {
                            tmp = distance;
                            dist = distance;
                            findServerIndex = index;
                        }
                    }
                    String uploadAddr = mapKey.get(findServerIndex);
                    final List<String> info = mapValue.get(findServerIndex);
                    final double distance = dist;

                    //Reset value, graphics
                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvResultDownload.setText("0.00");
                            mTvResultUpload.setText("0.00");
                        }
                    });
                    final List<Double> pingRateList = new ArrayList<>();

                    //Init Test
                    final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 3);

                    while (true) {
                        if (!pingTestStarted) {
                            pingTest.start();
                            pingTestStarted = true;
                        }
                        if (pingTestFinished && !downloadTestStarted) {
                            mUrlDownload = uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], "") + "random4000x4000.jpg";
                            new SpeedDownload().execute();
                            downloadTestStarted = true;

                        }
                        if (downloadTestFinished && !uploadTestStarted) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSpeedView.speedTo(0, 1000);
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mUrlUpload = uploadAddr;
                            new SpeedUpload().execute();
                            uploadTestStarted = true;
                        }

                        //Ping Test
                        if (pingTestFinished) {
                            if (getActivity() == null) {
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (getActivity() == null) {
                                        return;
                                    }
                                    mTvPing.setText(getString(R.string.ping) + " " + Math.round(pingTest.getInstantRtt()) + " ms");
                                    mTvJitter.setText(getString(R.string.jitter) + " " + Math.round(pingTest.getInstantRtt()) + " ms");
                                    mTvLoss.setText(getString(R.string.loss) + " 0%");
                                    mPing = Utils.round((float) pingTest.getInstantRtt(), 0);
                                }
                            });
                        } else {
                            pingRateList.add(pingTest.getInstantRtt());
                            if (getActivity() == null) {
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mHandler.removeCallbacks(runnable);
                                    mProgressBar.setVisibility(View.GONE);
                                    mTvAlert.setText("");
                                    mTvPing.setText(getString(R.string.ping) + " " + Math.round(pingTest.getInstantRtt()) + " ms");
                                    mTvJitter.setText(getString(R.string.jitter) + " " + Math.round(pingTest.getInstantRtt()) + " ms");
                                }
                            });
                        }

                        if (pingTestFinished && downloadTestFinished) {
                            break;
                        }

                        if (pingTest.isFinished()) {
                            pingTestFinished = true;
                        }
                        if (downloadTestFinished) {
                            downloadTestFinished = true;
                        }
                        if (uploadTestFinished) {
                            uploadTestFinished = true;
                        }

                        if (pingTestStarted && !pingTestFinished) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                            }
                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        focus = false;
        mProgramRunning = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_go_frag_speed:

                mTvPing.setText(getString(R.string.ping));
                mTvJitter.setText(getString(R.string.jitter));
                mTvResultDownload.setText("0.00");
                mTvResultUpload.setText("0.00");
                mTvLoss.setText(getString(R.string.loss) + " -%");
                if (Utils.isConnected(getContext())) {
                    mImgGo.setVisibility(View.GONE);
                    pingTestStarted = false;
                    pingTestFinished = false;
                    downloadTestStarted = false;
                    downloadTestFinished = false;
                    uploadTestStarted = false;
                    uploadTestFinished = false;
                    mProgressBar.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(runnable, 40);
                    addEvent();
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public class SpeedDownload extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            SpeedTestSocket speedTestSocket = new SpeedTestSocket();
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    downloadTestFinished = true;
                    uploadTestStarted = false;
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {

                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    final float mBitDown = (float) (Float.parseFloat(String.valueOf(report.getTransferRateBit())) * 0.000001);
                    mDown = mBitDown;
                    if(getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Utils.UNIT.equals(Utils.MBPS)) {
                                mTvResultDownload.setText(Utils.round(mBitDown, 2) + "");
                                mSpeedView.speedTo(mBitDown, 0);
                            } else if (Utils.UNIT.equals(Utils.MBS)) {
                                mTvResultDownload.setText(Utils.round((float) (mBitDown * 0.125), 2) + "");
                                mSpeedView.speedTo((float) (mBitDown * 0.125), 0);
                            } else if (Utils.UNIT.equals(Utils.KBS)) {
                                mTvResultDownload.setText(Utils.round((float) (mBitDown * 125), 2) + "");
                                mSpeedView.speedTo((float) (mBitDown * 125), 0);
                            }
                        }
                    });
                }

            });
            Random random = new Random();
            int n = random.nextInt(5000);
            speedTestSocket.startFixedDownload(mUrlDownload, 10000 + n);
            return null;
        }
    }

    public class SpeedUpload extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            SpeedTestSocket speedTestSocket = new SpeedTestSocket();
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    if(getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSpeedView.speedTo(0f, 1000);
                            Calendar mCa = Calendar.getInstance();
                            if (Utils.getTypeInternet(getContext()).equals(Utils.WIFI)) {
                                mDb.addResult(new Result(R.drawable.ic_wifi, mCa.getTimeInMillis() + "", mDown + "", mUp + "", mPing + ""));
                            } else if (Utils.getTypeInternet(getContext()).equals(Utils.BA_G)) {
                                mDb.addResult(new Result(R.drawable.ic_ba_g, mCa.getTimeInMillis() + "", mDown + "", mUp + "", mPing + ""));
                            }
                            mHandler.removeCallbacks(runnable);
                            mProgramRunning = false;
                            pingTestFinished = false;
                            mCountUpdateProgress = 0;
                            mImgGo.setVisibility(View.VISIBLE);
                        }
                    });

                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {

                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    final float mBitUp = (float) (Float.parseFloat(String.valueOf(report.getTransferRateBit())) * 0.000001);
                    mUp = mBitUp;
                    if(getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Utils.UNIT.equals(Utils.MBPS)) {
                                mTvResultUpload.setText(Utils.round(mBitUp, 2) + "");
                                mSpeedView.speedTo(mBitUp, 0);
                            } else if (Utils.UNIT.equals(Utils.MBS)) {
                                mTvResultUpload.setText(Utils.round((float) (mBitUp * 0.125), 2) + "");
                                mSpeedView.speedTo((float) (mBitUp * 0.125), 0);
                            } else if (Utils.UNIT.equals(Utils.KBS)) {
                                mTvResultUpload.setText(Utils.round((float) (mBitUp * 125), 2) + "");
                                mSpeedView.speedTo((float) (mBitUp * 125), 0);
                            }
                        }
                    });
                }
            });
            Random random = new Random();
            int n = random.nextInt(5000);
            speedTestSocket.startFixedUpload(mUrlUpload, 10000000, 10000 + n);
            return null;
        }
    }
}