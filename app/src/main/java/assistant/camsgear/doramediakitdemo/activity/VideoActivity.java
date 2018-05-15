package assistant.camsgear.doramediakitdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import assistant.camsgear.doramediakitdemo.R;
import assistant.camsgear.doramediakitdemo.global.FileType;
import assistant.camsgear.doramediakitdemo.global.GlobalDefine;
import assistant.camsgear.doramediakitdemo.global.MountMode;
import assistant.camsgear.doramediakitdemo.global.PanoMode;
import assistant.camsgear.doramediakitdemo.utils.MediaInfoUtil;
import assistant.camsgear.doramediakitdemo.widget.GyroSwitchView;
import assistant.camsgear.doramediakitdemo.widget.MountModeView;
import assistant.camsgear.doramediakitdemo.widget.PanoModeView;
import me.camdora.dora.media.player.DoraMediaPlayer;
import me.camdora.dora.media.player.IMediaPlayer;
import me.camdora.dora.media.player.utils.PlayerOptionCategory;
import me.camdora.dora.media.player.widget.DoraVideoView;

public class VideoActivity extends AppCompatActivity implements PanoModeView.OnPanoModeChangedListener, MountModeView.OnMountModeChangedListener, GyroSwitchView.OnGyroActiveChangedListener {
    static {
        DoraMediaPlayer.loadLibrariesOnce(null);
        DoraMediaPlayer.native_profileBegin("libdoraplayer.so");
    }
    private DoraVideoView mDoraVideoView;

    private PanoModeView mPanoModeView;
    private MountModeView mMountModeView;
    private GyroSwitchView mGyroSwitchView;

    private FileType mFileType;
    private MediaInfoUtil.MediaInfoBean mMediaInfoBean;
    private String mCalibration;
    private String mVideoPath;
    private boolean isPlayPanoramaFile = false;
    private PlayerOptionCategory mPlayerOption = PlayerOptionCategory.CATEGORY_NORMAL;

    private Button mResetAngleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mFileType = (FileType)getIntent().getSerializableExtra("fileType");

        if(null == mFileType){
            finish();
            return;
        }

        parseFileType();

        mPanoModeView = (PanoModeView) this.findViewById(R.id.video_pano_mode_view);
        mPanoModeView.setOnPanoModeChangedListener(this);

        mMountModeView = (MountModeView) this.findViewById(R.id.video_mount_mode_view);
        mMountModeView.setOnMountModeChangedListener(this);

        mGyroSwitchView = (GyroSwitchView) this.findViewById(R.id.video_gyro_switch_view);
        mGyroSwitchView.setOnGyroActiveChangedListener(this);

        mResetAngleButton = (Button) this.findViewById(R.id.video_reset_angle);
        mResetAngleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDoraVideoView.resetAngle();
            }
        });

        if(isPlayPanoramaFile){
            mPanoModeView.setVisibility(View.VISIBLE);
            mMountModeView.setVisibility(View.VISIBLE);
            mGyroSwitchView.setVisibility(View.VISIBLE);
            mResetAngleButton.setVisibility(View.VISIBLE);
        }else{
            mPanoModeView.setVisibility(View.GONE);
            mMountModeView.setVisibility(View.GONE);
            mGyroSwitchView.setVisibility(View.GONE);
            mResetAngleButton.setVisibility(View.GONE);
        }

        mDoraVideoView = (DoraVideoView) this.findViewById(R.id.video_view);
        mDoraVideoView.setOptionCategory(mPlayerOption);
        mDoraVideoView.setCalibrationData(mCalibration);
        mDoraVideoView.setMediaInfo(mMediaInfoBean.toString());
        mDoraVideoView.setVideoPath(mVideoPath);
        mDoraVideoView.initTimeStampView(Gravity.TOP|Gravity.RIGHT);
        mDoraVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mDoraVideoView.start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        saveBitmap(mDoraVideoView.snapshot());
                    }
                }).start();

            }
        });
    }

    private void parseFileType() {
        switch (mFileType) {
            case FILE_TYPE_H265:
                mMediaInfoBean = MediaInfoUtil.getDoubleMediaInfoBean();
                mCalibration = GlobalDefine.CALIBRATION_DOUBLE_FISHEYE_265;
                mVideoPath = GlobalDefine.FILE_PATH_H265;
                isPlayPanoramaFile = true;
                break;
            case FILE_TYPE_SINGLE_EYE:
                mMediaInfoBean = null;
                mCalibration = null;
                mVideoPath = null;
                isPlayPanoramaFile = true;
                break;
            case FILE_TYPE_DOUBLE_EYE:
                mMediaInfoBean = MediaInfoUtil.getDoubleMediaInfoBean();
                mCalibration = GlobalDefine.CALIBRATION_DOUBLE_FISHEYE;
                mVideoPath = GlobalDefine.FILE_PATH_DOUBLE_FISHEYE;
                isPlayPanoramaFile = true;
                break;
            case FILE_TYPE_DOUBLE_FISHEYE_IMAGE:
                mMediaInfoBean = MediaInfoUtil.getDoubleMediaInfoBean();
                mCalibration = GlobalDefine.CALIBRATION_DOUBLE_FISHEYE;
                mVideoPath = GlobalDefine.FILE_PATH_DOUBLE_FISHEYE_IMAGE;
                isPlayPanoramaFile = true;
                break;
            case FILE_TYPE_SINGLE_FISHEYE_IMAGE:
                mMediaInfoBean = MediaInfoUtil.getMonoMediaInfoBean();
                mCalibration = GlobalDefine.CALIBRATION_MONO_FISHEYE;
                mVideoPath = GlobalDefine.FILE_PATH_MONO_FISHEYE;
                isPlayPanoramaFile = true;
                break;
            case FILE_TYPE_CLOUD_MKV:
                mMediaInfoBean = MediaInfoUtil.getNormalMediaInfoBean();
                mCalibration = null;
                mVideoPath = GlobalDefine.FILE_PATH_MKV;
                isPlayPanoramaFile = false;
                break;
            case FILE_TYPE_RTSP:
                mMediaInfoBean = MediaInfoUtil.getDoubleMediaInfoBean();
                mCalibration = GlobalDefine.CALIBRATION_RTSP;
                mVideoPath = GlobalDefine.FILE_RTSP_PATH;
                isPlayPanoramaFile = true;
                mPlayerOption = PlayerOptionCategory.CATEGORY_RTSP;
                break;
        }
    }
    private boolean timestampEnable = true;
    public void isShowTimestamp(View view){
        timestampEnable  = timestampEnable ? false : true;
        mDoraVideoView.isShowTimeStamp(timestampEnable);
    }

    public void startRecord(){
        String path = Environment.getExternalStorageDirectory() + "/" + "Download" + "/";
        File folder = new File(path);
        if (!folder.exists() && !folder.mkdirs()) {
            return;
        }
        long dataTake = System.currentTimeMillis();
        final String name  = path + dataTake + ".mp4";
        Log.i(TAG, "jpegName = " + name);
        mDoraVideoView.startRecordFile(name);
    }

    public void stopRecord(){
        mDoraVideoView.stopRecordFile();
    }
    //save video frame to jpg
    public void snapshot(){
        String path = Environment.getExternalStorageDirectory() + "/" + "Download" + "/";
        File folder = new File(path);
        if (!folder.exists() && !folder.mkdirs()) {
            return;
        }
        long dataTake = System.currentTimeMillis();
        final String name  = path + dataTake + ".jpg";
        mDoraVideoView.screenShot(name);
    }

    @Override
    protected void onDestroy() {
        if(mDoraVideoView != null) {
            mDoraVideoView.release(true);
            mDoraVideoView.stopPlayback();
            mDoraVideoView.stopBackgroundPlay();
        }
        DoraMediaPlayer.native_profileEnd();
        super.onDestroy();
    }

    @Override
    public void onPanoModeChange(PanoMode mode) {
        mDoraVideoView.setPerspectiveMode(mPanoModeView.getCurrentPanoMode().ordinal());

    }

    @Override
    public void onMountModeChanged(MountMode mode) {
        mDoraVideoView.setMountMode(mode.ordinal());
    }

    @Override
    public void onGyroActiveChanged(boolean isActive) {
        mDoraVideoView.setGyroActive(isActive);
    }
    private static final String TAG = VideoActivity.class.getName();

    public void saveBitmap(Bitmap bitmap){
        if(bitmap == null){
            Log.i(TAG,"bitmap == null");
            return;
        }
        String mPath = Environment.getExternalStorageDirectory().getPath();
        OutputStream fout = null;
        File file = new File(mPath+"/AssistantScreen");
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建<span style="color:#FF0000;">目录中包含却不存在</span>的文件夹
            file.mkdirs();
        }
        String filePath = file.getPath() + File.separator + "ScreenBitmap" + ".png";
        File imageFile = new File(filePath);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();
        }
    }
}
