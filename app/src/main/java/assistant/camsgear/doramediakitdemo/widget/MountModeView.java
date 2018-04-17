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
import assistant.camsgear.doramediakitdemo.global.MountMode;


/**
 * Created by edwin on 27/11/2017.
 */

public class MountModeView extends FrameLayout implements View.OnClickListener {

    private ImageView mImageView;
    private TextView mTextView;
    private MountMode mCurrentMountMode = MountMode.MOUNT_MODE_DESKTOP;
    private OnMountModeChangedListener onMountModeChangedListener;

    public MountModeView(@NonNull Context context) {
        this(context, null);
    }

    public MountModeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.mount_mode_view, this);
        mImageView = (ImageView)findViewById(R.id.mount_mode_view_image);
        mTextView = (TextView)findViewById(R.id.mount_mode_view_text);
        View mMountModeView = findViewById(R.id.mount_mode_view);
        mMountModeView.setOnClickListener(this);
    }

    public void setMountMode(MountMode mode) {
        this.mCurrentMountMode = mode;
        this.applyMountMode();
    }

    public MountMode getCurrentMountMode() {
        return this.mCurrentMountMode;
    }

    private void applyMountMode() {
        switch (mCurrentMountMode) {
            case MOUNT_MODE_CEILING:
                mImageView.setImageResource(R.mipmap.ceiling_mount);
                mTextView.setText(R.string.ceiling_mount);
                break;
            case MOUNT_MODE_WALL:
                mImageView.setImageResource(R.mipmap.wall_mount);
                mTextView.setText(R.string.wall_mount);
                break;
            case MOUNT_MODE_DESKTOP:
            default:
                mImageView.setImageResource(R.mipmap.desktop_mount);
                mTextView.setText(R.string.desktop_mount);
                break;
        }
        if (this.onMountModeChangedListener != null) {
            this.onMountModeChangedListener.onMountModeChanged(mCurrentMountMode);
        }

    }

    public interface OnMountModeChangedListener {
        void onMountModeChanged(MountMode mode);
    }

    @Override
    public void onClick(View v) {
        this.setMountMode(mCurrentMountMode.next());
    }

    public void setOnMountModeChangedListener(@Nullable OnMountModeChangedListener l) {
        this.onMountModeChangedListener = l;
    }
}
