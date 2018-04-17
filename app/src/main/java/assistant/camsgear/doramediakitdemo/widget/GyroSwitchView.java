package assistant.camsgear.doramediakitdemo.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import assistant.camsgear.doramediakitdemo.R;


/**
 * Created by edwin on 09/12/2017.
 */

public class GyroSwitchView extends FrameLayout implements View.OnClickListener {

    private TextView mTextView;
    private boolean mCurrentGyroActive;
    private OnGyroActiveChangedListener onGyroActiveChangedListener;

    public GyroSwitchView(@NonNull Context context) {
        this(context, null);
    }

    public GyroSwitchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.gyro_switch_view, this);
        mTextView = (TextView)findViewById(R.id.gyro_switch_view_text);
        View mGyroSwitchView = findViewById(R.id.gyro_switch_view);
        mGyroSwitchView.setOnClickListener(this);
    }

    public void setGyroActive(boolean isActive) {
        mCurrentGyroActive = isActive;
        this.applyGyroActive();
    }

    public boolean getCurrentGyroActive() {
        return this.mCurrentGyroActive;
    }

    private void applyGyroActive() {
        if (mCurrentGyroActive) {
            mTextView.setText(R.string.gyro_on);
        } else {
            mTextView.setText(R.string.gyro_off);
        }

        if (this.onGyroActiveChangedListener != null) {
            this.onGyroActiveChangedListener.onGyroActiveChanged(mCurrentGyroActive);
        }

    }

    public interface OnGyroActiveChangedListener {
        void onGyroActiveChanged(boolean isActive);
    }

    @Override
    public void onClick(View v) {
        this.setGyroActive(!mCurrentGyroActive);
    }

    public void setOnGyroActiveChangedListener(@Nullable OnGyroActiveChangedListener l) {
        this.onGyroActiveChangedListener = l;
    }
}
