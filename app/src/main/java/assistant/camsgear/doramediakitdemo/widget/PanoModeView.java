package assistant.camsgear.doramediakitdemo.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import assistant.camsgear.doramediakitdemo.R;
import assistant.camsgear.doramediakitdemo.global.PanoMode;


/**
 * Created by edwin on 25/11/2017.
 */

public class PanoModeView extends FrameLayout implements View.OnClickListener {

    private ImageView mImageView;
    private TextView mTextView;
    private PanoMode mCurrentPanoMode = PanoMode.PANO_MODE_FISHEYE;
    private OnPanoModeChangedListener onPanoModeChangedListener;

    public PanoModeView(@NonNull Context context) {
        this(context, null);
    }

    public PanoModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pano_mode_view, this);
        mImageView = (ImageView)findViewById(R.id.pano_mode_view_image);
        mTextView = (TextView)findViewById(R.id.pano_mode_view_text);
        View mPanoModeView = findViewById(R.id.pano_mode_view);
        mPanoModeView.setOnClickListener(this);

    }

    public void setPanoMode(PanoMode mode) {
        mCurrentPanoMode = mode;
        this.applyPanoMode();
    }

    public PanoMode getCurrentPanoMode() {
        return mCurrentPanoMode;
    }

    private void applyPanoMode() {
        switch (mCurrentPanoMode) {
            case PANO_MODE_NORMAL:
                mImageView.setImageResource(R.mipmap.icon_normal);
                mTextView.setText(R.string.normal);
                break;
            case PANO_MODE_LITTLE_PLANET:
                mImageView.setImageResource(R.mipmap.icon_planet);
                mTextView.setText(R.string.planet);
                break;
            case PANO_MODE_FISHEYE:
            default:
                mImageView.setImageResource(R.mipmap.icon_fisheye);
                mTextView.setText(R.string.fisheye);
                break;
        }
        if (this.onPanoModeChangedListener != null) {
            this.onPanoModeChangedListener.onPanoModeChange(mCurrentPanoMode);
        }

    }

    @Override
    public void onClick(View v) {
        this.setPanoMode(mCurrentPanoMode.next());
    }

    public interface OnPanoModeChangedListener {
        void onPanoModeChange(PanoMode mode);
    }

    public void setOnPanoModeChangedListener(@Nullable OnPanoModeChangedListener l) {
        this.onPanoModeChangedListener = l;
    }
}
