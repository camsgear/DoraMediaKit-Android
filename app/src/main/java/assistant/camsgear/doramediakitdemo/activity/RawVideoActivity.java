package assistant.camsgear.doramediakitdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import assistant.camsgear.doramediakitdemo.R;
import assistant.camsgear.doramediakitdemo.global.FileType;
import assistant.camsgear.doramediakitdemo.global.GlobalDefine;
import assistant.camsgear.doramediakitdemo.global.PanoMode;
import assistant.camsgear.doramediakitdemo.utils.MediaInfoUtil;
import assistant.camsgear.doramediakitdemo.widget.GyroSwitchView;
import assistant.camsgear.doramediakitdemo.widget.PanoModeView;
import me.camdora.dora.media.player.DoraMediaPlayer;
import me.camdora.dora.media.player.widget.DoraVideoView;

public class RawVideoActivity extends AppCompatActivity implements View.OnClickListener, PanoModeView.OnPanoModeChangedListener, GyroSwitchView.OnGyroActiveChangedListener {
    static {
        DoraMediaPlayer.loadLibrariesOnce(null);
        DoraMediaPlayer.native_profileBegin("libdoraplayer.so");
    }

    private String TAG= "RawVideoActivity";
    private DoraVideoView mDoraVideoView;
    private FileType mFileType;
    private MediaInfoUtil.MediaInfoBean mMediaInfoBean;
    private String mVideoPath;
    private Button mBtn;
    private PanoModeView mPanoModeView;
    private GyroSwitchView mGyroSwitchView;
    private boolean mIsPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_video);

        mMediaInfoBean = MediaInfoUtil.getEquirectangularMediaInfoBean();
        mVideoPath = GlobalDefine.FILE_PATH_H265;

        mDoraVideoView = (DoraVideoView) this.findViewById(R.id.raw_video_view);
        mDoraVideoView.setCalibrationData(null);
        mDoraVideoView.setMediaInfo(mMediaInfoBean.toString());
        mDoraVideoView.prepareH264Player();
        mBtn = (Button)this.findViewById(R.id.play_264_btn);

        mBtn.setOnClickListener(this);

        mPanoModeView = (PanoModeView)findViewById(R.id.raw_video_pano_mode_view);
        mPanoModeView.setOnPanoModeChangedListener(this);

        mGyroSwitchView = (GyroSwitchView)findViewById(R.id.raw_video_gyro_switch_view);
        mGyroSwitchView.setOnGyroActiveChangedListener(this);

//        mDoraVideoView.setVideoPath(mVideoPath);


    }

    private void parseLocalVideoFile() throws IOException {
        InputStream inStream = getApplicationContext().getResources().openRawResource(R.raw.finals);
        byte[] byteData = convertStreamToByteArray(inStream);
        inStream.close();

        int pos = 0;
        int spsStart = -1;
        int spsLength = -1;
        boolean isSPS = false;
        int ppsStart = -1;
        int ppsLength = -1;
        boolean isPPS = false;
        int seiStart = -1;
        int seiLength = -1;
        boolean isSEI = false;
        byte[] ppsBytes;
        byte[] spsBytes;
        byte[] seiBytes;
        boolean isFirstIFrame = true;
        int frameStart = -1;
        int frameLength = -1;
        boolean isFrame = false;

        byte[] dataToPlay;

        while (pos < byteData.length && mIsPlaying) {

            if (byteData[pos] == 0x00 && byteData[pos+1] == 0x00 && byteData[pos + 2] == 0x00 && byteData[pos + 3] == 0x01) {
                if (isPPS) {
                    ppsLength = pos;
                    dataToPlay = Arrays.copyOfRange(byteData, ppsStart, ppsLength);
                    playFrame(dataToPlay);
                    isPPS = false;
                }
                if (isSPS) {
                    spsLength = pos;
                    dataToPlay = Arrays.copyOfRange(byteData, spsStart, spsLength);
                    playFrame(dataToPlay);
                    isSPS = false;
                }
                if (isSEI) {
                    seiLength = pos;
                    dataToPlay = Arrays.copyOfRange(byteData, seiLength, seiLength);
                    playFrame(dataToPlay);
                    isSEI = false;
                }
                if (isFrame) {
                    dataToPlay = Arrays.copyOfRange(byteData, frameStart, pos);
                    playFrame(dataToPlay);
                    isFrame = false;
                }
                switch (byteData[pos + 4] & 0x1f) {
                    case 6://SEI
                        isSEI = true;
                        seiStart = pos;
                        break;
                    case 7://SPS
                        isSPS = true;
                        spsStart = pos;
                        break;
                    case 8://PPS
                        isPPS = true;
                        ppsStart = pos;
                        break;
                    default://Frame
                        frameStart = pos;
                        isFrame = true;
                        break;

                }
                pos += 5;
            } else {
                pos++;
            }
        }
    }

    private byte[] convertStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[10240];
        int i = Integer.MAX_VALUE;
        while ((i = is.read(buff, 0, buff.length)) > 0) {
            baos.write(buff, 0, i);
        }

        return baos.toByteArray();
    }

    private void playFrame(byte[] data) {
        mDoraVideoView.playH264(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsPlaying = false;
        if(mDoraVideoView != null) {
            mDoraVideoView.stopPlayback();
            mDoraVideoView.stopBackgroundPlay();
            mDoraVideoView.destroyH264Player();
        }
        DoraMediaPlayer.native_profileEnd();
    }

    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIsPlaying = true;
                try {
                    parseLocalVideoFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onPanoModeChange(PanoMode mode) {
        mDoraVideoView.setPerspectiveMode(mode.ordinal());
    }

    @Override
    public void onGyroActiveChanged(boolean isActive) {
        mDoraVideoView.setGyroActive(isActive);
    }
}
