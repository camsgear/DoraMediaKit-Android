package assistant.camsgear.doramediakitdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import assistant.camsgear.doramediakitdemo.R;
import assistant.camsgear.doramediakitdemo.bean.CalibrationBean;
import assistant.camsgear.doramediakitdemo.bean.DeviceSerialBean;
import assistant.camsgear.doramediakitdemo.bean.HoursImageStreamBean;
import assistant.camsgear.doramediakitdemo.global.GlobalDefine;
import assistant.camsgear.doramediakitdemo.global.PanoMode;
import assistant.camsgear.doramediakitdemo.http.HttpUtils;
import assistant.camsgear.doramediakitdemo.utils.FormatUtil;
import assistant.camsgear.doramediakitdemo.utils.MediaInfoUtil;
import assistant.camsgear.doramediakitdemo.widget.DeviceSerialDialog;
import assistant.camsgear.doramediakitdemo.widget.PanoModeView;
import me.camdora.dora.media.player.DoraMediaPlayer;
import me.camdora.dora.media.player.IMediaPlayer;
import me.camdora.dora.media.player.widget.DoraVideoView;


public class ImageStreamActivity extends AppCompatActivity implements View.OnClickListener,DeviceSerialDialog.DeviceSerialCallback, PanoModeView.OnPanoModeChangedListener {
    private static final String TAG = "ImageStreamActivity";

    private RecyclerView mRecyclerView;
    private DoraVideoView mVideoView;
    private MenuAdapter mMenuAdapter;
    private TextView mMessageView;
    private TextView mJumpCurrentView;
    private SeekBar mSeekView;
    private View mBottomView;
    private ProgressBar mProgressBar;
    private PanoModeView mPanoModeView;


    private ArrayList<MenuBean> data = new ArrayList<>();
    private LinkedHashMap<Integer, LinkedHashMap<Integer, String>> mImageStreamDatas = new LinkedHashMap<>();

    private DeviceSerialDialog mDeviceSerialDialog;
    private int mCurrentHour;
    private int mCurrentMinute;
    private int mMaxHour;
    private int mMaxMinute;
    private String currentDate = null;
    private boolean isToday = false;
    private String mCalibration = null;
    private String mSerialNumber = null;

    static {
        DoraMediaPlayer.loadLibrariesOnce(null);
        DoraMediaPlayer.native_profileBegin("libdoraplayer.so");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_stream);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) this.findViewById(R.id.mList);
        mVideoView = (DoraVideoView) this.findViewById(R.id.video_view);
        mMessageView = (TextView) this.findViewById(R.id.tv_message_image_time);
        mJumpCurrentView = (TextView) this.findViewById(R.id.tv_jump_current);
        mSeekView = (SeekBar) this.findViewById(R.id.sb_image_stream_seek);
        mBottomView = this.findViewById(R.id.bottom_view);
        mProgressBar = (ProgressBar) this.findViewById(R.id.pb_progress);
        mPanoModeView = (PanoModeView) this.findViewById(R.id.image_stream_pano_mode_view);
        mPanoModeView.setOnPanoModeChangedListener(this);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        mMenuAdapter = new MenuAdapter();
        mRecyclerView.setAdapter(mMenuAdapter);

        mDeviceSerialDialog = new DeviceSerialDialog(this,this);
        mDeviceSerialDialog.show();

        mSeekView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float fHour = progress / 60f;
                int iHour = (int) fHour;
                float fDecimal = fHour - iHour;
                int iMinute = (int) (fDecimal * 60);
                mMessageView.setText(currentDate + " " + iHour + ":" + ((iMinute < 10) ? "0" + iMinute : iMinute));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                Log.d(TAG, "progress:"+progress);
                float fHour = progress / 60f;
                int iHour = (int) fHour;
                float fDecimal = fHour - iHour;
                int iMinute = (int) (fDecimal * 60);
                updateImageStream(iHour,iMinute);
            }
        });

        mJumpCurrentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitingView();
                updateImageStream(mMaxHour, mMaxMinute);
                mSeekView.setProgress(mMaxHour * 60 + mMaxMinute);
            }
        });
    }

    private void releaseDoraPlayer() {
        if(mVideoView != null) {
            mVideoView.release(true);
            mVideoView.stopPlayback();
            mVideoView.stopBackgroundPlay();
        }
    }

    public void updateImageStream(int hour, int minute){
        String imageUrl = null;
        Log.d(TAG, "Hour:" + hour);
        Log.d(TAG, "Minute:" + minute);

        if(mImageStreamDatas.containsKey(hour)){
            LinkedHashMap<Integer, String> hoursMap = mImageStreamDatas.get(hour);
            if(hoursMap.containsKey(minute)){
                imageUrl = mImageStreamDatas.get(hour).get(minute);
            }
        }

        if(imageUrl != null){
            mVideoView.changeImageURIOnAir(imageUrl);

            mCurrentHour = hour;
            mCurrentMinute = minute;
            mMessageView.setText(currentDate + " " + mCurrentHour + ":" + ((mCurrentMinute < 10) ? "0" + mCurrentMinute : mCurrentMinute));
        } else {
            Toast.makeText(ImageStreamActivity.this, "图片流获取失败!", Toast.LENGTH_SHORT).show();
            hideWaitingView();
        }
    }


    private void add(String name, String path) {
        MenuBean bean = new MenuBean();
        bean.name = name;
        bean.path = path;
        data.add(bean);
    }

    @Override
    public void onPanoModeChange(PanoMode mode) {
        mVideoView.setPerspectiveMode(mode.ordinal());
    }

    private class MenuBean{
        String name;
        String path;
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{

        public void updateData(){
            if (data.size() > 0) {
                this.notifyDataSetChanged();
            }
        }
        @Override
        public MenuAdapter.MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuAdapter.MenuHolder(getLayoutInflater().inflate(R.layout.item_image_stream,parent,false));
        }

        @Override
        public void onBindViewHolder(MenuAdapter.MenuHolder holder, int position) {
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder{

            private View mImageStreamView;
            private TextView mImageStreamDay;

            MenuHolder(View itemView) {
                super(itemView);
                mImageStreamView = itemView.findViewById(R.id.image_stream);
                mImageStreamDay = (TextView) itemView.findViewById(R.id.image_stream_day);
                mImageStreamView.setOnClickListener(ImageStreamActivity.this);
            }

            public void setPosition(int position){
                MenuBean bean=data.get(position);
                mImageStreamDay.setText(bean.name);
                mImageStreamView.setTag(position);
            }
        }

    }


    private void updateUI(){
        showWaitingView();
        String imageUrl = null;
        if(isToday){
            //显示当前时间点的图片
            imageUrl = obtainCurrentImageURL();
        }else{
            int firstHourData = mImageStreamDatas.keySet().iterator().next();
            int firstMimuteData = mImageStreamDatas.get(firstHourData).keySet().iterator().next();
            imageUrl = mImageStreamDatas.get(firstHourData).get(firstMimuteData);
            mCurrentHour = firstHourData;
            mCurrentMinute = firstMimuteData;
            Log.d(TAG, "firstHourData:"+firstHourData);
            Log.d(TAG, "firstMimuteData:" + firstMimuteData);
            Log.d(TAG, "imageurl:" + imageUrl);
            mJumpCurrentView.setVisibility(View.GONE);
        }

        if(imageUrl != null){
            mVideoView.setCalibrationData(mCalibration);
            mVideoView.setMediaInfo(MediaInfoUtil.getDoubleMediaInfoBean().toString());
            mVideoView.setVideoPath(imageUrl);

            mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    mVideoView.start();
                    Log.d(TAG, "图片流显示.");
                    hideWaitingView();
                }
            });
            mRecyclerView.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            mBottomView.setVisibility(View.VISIBLE);
            mMessageView.setVisibility(View.VISIBLE);
            mPanoModeView.setVisibility(View.VISIBLE);
            mMessageView.setText(currentDate + " " + mCurrentHour + ":" + ((mCurrentMinute < 10) ? "0" + mCurrentMinute : mCurrentMinute));
            mSeekView.setProgress(mCurrentHour * 60 + mCurrentMinute);
        }else{
            Toast.makeText(ImageStreamActivity.this, "图片流获取失败!",Toast.LENGTH_SHORT).show();
            hideWaitingView();
        }

    }

    private String obtainCurrentImageURL() {
        String imageUrl = null;
        int lastHourData = 0;
        Iterator<Integer> hoursSet = mImageStreamDatas.keySet().iterator();
        while(hoursSet.hasNext()){
            lastHourData = hoursSet.next();
        }
        Iterator<Integer> minutesSet = mImageStreamDatas.get(lastHourData).keySet().iterator();
        int lastMinuteData = 0;
        while(minutesSet.hasNext()){
            lastMinuteData = minutesSet.next();
        }

        Log.d(TAG, "lastHourData:" + lastHourData);
        Log.d(TAG, "laseMinuteData:" + lastMinuteData);
        imageUrl = mImageStreamDatas.get(lastHourData).get(lastMinuteData);
        mCurrentHour = lastHourData;
        mCurrentMinute = lastMinuteData;
        mMaxHour = mCurrentHour;
        mMaxMinute = mCurrentMinute;
        Log.d(TAG, "imageURl:" + imageUrl);
        return imageUrl;
    }


    @Override
    public void onClick(View view){
        int position= (int)view.getTag();
        MenuBean bean=data.get(position);
        Log.d(TAG, "name:"+bean.name);
        Log.d(TAG, "path:" + bean.path);
        currentDate = bean.name;
        HttpUtils.obtainAllHourFilesByPath(new HttpUtils.AllHoursImagesCallback() {
            @Override
            public void onSuccess(HoursImageStreamBean body) {
                String host = body.getHost();
                LinkedHashMap minuteFileData = null;
                for (HoursImageStreamBean.HoursBean bean : body.getHours()) {
                    int hour = Integer.valueOf(bean.getHour());
                    minuteFileData = mImageStreamDatas.get(hour);
                    if(null == minuteFileData){
                        minuteFileData = new LinkedHashMap<>();
                    }
                    for (String file : bean.getFiles()) {
                        int minute = FormatUtil.nameParseMinute(file);
                        if(minute == -1){
                            continue;
                        }
                        String imagePath = host + "/" + file;
                        minuteFileData.put(minute, imagePath);
                    }
                    mImageStreamDatas.put(hour,minuteFileData);
                }

                isToday = currentDate.equals(FormatUtil.getCurrentDate());
                Log.d(TAG, "isToday:" + isToday);

                HttpUtils.obtainCalibrationBySerialNumber(new HttpUtils.CalibrationCallback() {
                    @Override
                    public void onSuccess(CalibrationBean body) {
                        mCalibration = body.getCalibration();
                        updateUI();
                    }

                    @Override
                    public void onFailure(String error) {
                        mCalibration = GlobalDefine.CALIBRATION_DOUBLE_FISHEYE;
                        updateUI();

                    }
                },mSerialNumber);
            }

            @Override
            public void onFailure(String error) {
                hideWaitingView();
                Toast.makeText(ImageStreamActivity.this, "图片流获取失败!",Toast.LENGTH_SHORT).show();
                Log.e(TAG, error);
            }
        }, bean.path);
    }

    private void showWaitingView() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideWaitingView(){
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCancel() {
        mDeviceSerialDialog.cancel();
        finish();
    }

    @Override
    public void onOk(String serialNumber) {
        mDeviceSerialDialog.cancel();
        this.mSerialNumber = serialNumber;

        showWaitingView();

        HttpUtils.obtainDaysBySerialNumber(new HttpUtils.DeviceImageDayCallback() {
            @Override
            public void onSuccess(ArrayList<DeviceSerialBean> body) {
                for(DeviceSerialBean bean : body){
                    Log.d(TAG, "Day:"+bean.getDay());
                    Log.d(TAG, "Path" + bean.getPath());
                    Log.d(TAG, "Host:"+bean.getHost());
                    Log.d(TAG, "Bucket:" + bean.getBucket());
                    add(FormatUtil.dateConvert(bean.getDay()),bean.getPath());
                }
                hideWaitingView();
                mMenuAdapter.updateData();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ImageStreamActivity.this,"数据获取失败.",Toast.LENGTH_SHORT).show();
                hideWaitingView();
                ImageStreamActivity.this.finish();
            }
        },mSerialNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mVideoView != null) {
            mVideoView.release(true);
            mVideoView.stopPlayback();
            mVideoView.stopBackgroundPlay();
        }
        DoraMediaPlayer.native_profileEnd();
    }
}
