package assistant.camsgear.doramediakitdemo.http;


import java.util.ArrayList;

import assistant.camsgear.doramediakitdemo.bean.CalibrationBean;
import assistant.camsgear.doramediakitdemo.bean.DeviceSerialBean;
import assistant.camsgear.doramediakitdemo.bean.HoursImageStreamBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by camdora on 16-12-23. 15:25
 */

public interface ApiManager {

    @GET("ipcams/image/demo/days")
    Call<ArrayList<DeviceSerialBean>> obtainDaysBySerialNumber(@Query("serialNumber") String serialNumber);

    @GET("ipcams/image/demo/hours")
    Call<ArrayList<DeviceSerialBean>> obtainHoursByPath(@Query("path") String path);

    @GET("ipcams/image/demo/files")
    Call<ArrayList<DeviceSerialBean>> obtainFilesByPath(@Query("path") String path);

    @GET("ipcams/image/demo/hoursWithMinutes")
    Call<HoursImageStreamBean> obtainAllHourFilesByPath(@Query("path") String path);

    @GET("ipcams/image/demo/calibration")
    Call<CalibrationBean> obtainCalibrationBySerialNumber(@Query("serialNumber") String serialNumber);
}
