package assistant.camsgear.doramediakitdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import assistant.camsgear.doramediakitdemo.R;
import assistant.camsgear.doramediakitdemo.utils.ScreenUtil;


/**
 * Created by camdora on 17-3-20. 17:19
 */

public class DeviceSerialDialog extends Dialog implements View.OnClickListener{
    private final static String TAG = "DeviceSerialDialog";

    private Context context ;

    public DeviceSerialDialog(Context context,DeviceSerialCallback deviceSerialCallback) {
        super(context);
        this.context = context;
        this.mDeviceSerialCallback = deviceSerialCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    private EditText mDeviceSerialNumberView;
    private Button mCancelView;
    private Button mOkView;

    public void init() {
        View settingView = View.inflate(context, R.layout.dialog_device_serial_number, null);
        settingView.setMinimumWidth((int) (ScreenUtil.getScreenWidth(context) * 0.8));
        setContentView(settingView);

        mDeviceSerialNumberView = (EditText) settingView.findViewById(R.id.et_device_serial_number);
        mCancelView = (Button) settingView.findViewById(R.id.bt_cancel);
        mOkView = (Button) settingView.findViewById(R.id.bt_ok);
        mCancelView.setOnClickListener(this);
        mOkView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                if(mDeviceSerialCallback != null){
                    mDeviceSerialCallback.onCancel();
                }
                break;
            case R.id.bt_ok:
                if(mDeviceSerialCallback != null){
                    String serialNumber = mDeviceSerialNumberView.getText().toString().trim();
                    mDeviceSerialCallback.onOk(serialNumber);
                }
                break;
        }
    }

    private DeviceSerialCallback mDeviceSerialCallback;
    public interface DeviceSerialCallback{
        void onCancel();
        void onOk(String serialNumber);
    }
}

