package assistant.camsgear.doramediakitdemo.http;

import java.util.ArrayList;

import assistant.camsgear.doramediakitdemo.bean.CalibrationBean;
import assistant.camsgear.doramediakitdemo.bean.DeviceSerialBean;
import assistant.camsgear.doramediakitdemo.bean.HoursImageStreamBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by camdora on 17-1-7. 19:04
 */

public class HttpUtils {
    private final static String TAG = "HttpUtils";
    private final static String STAGE_URL = "http://stage.api.camdora.me/v3/";

    private static Retrofit retrofit;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(STAGE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void obtainDaysBySerialNumber(final DeviceImageDayCallback deviceImageStreamCallback ,final String serialNumber) {
        ApiManager obtainDaysService = retrofit.create(ApiManager.class);
        Call<ArrayList<DeviceSerialBean>> call = obtainDaysService.obtainDaysBySerialNumber(serialNumber);
        call.enqueue(new Callback<ArrayList<DeviceSerialBean>>() {
            @Override
            public void onResponse(Call<ArrayList<DeviceSerialBean>> call, Response<ArrayList<DeviceSerialBean>> response) {
                if(response != null){
                    ArrayList<DeviceSerialBean> body = response.body();
                    if(body != null && body.size() > 0){
                        deviceImageStreamCallback.onSuccess(body);
                    }else{
                        deviceImageStreamCallback.onFailure("数据为空.");
                    }
                }else {
                    if(deviceImageStreamCallback != null){
                        deviceImageStreamCallback.onFailure("response == null");
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DeviceSerialBean>> call, Throwable t) {
                if(deviceImageStreamCallback != null){
                    deviceImageStreamCallback.onFailure(t.toString());
                }
            }
        });
    }

    public static void obtainHoursByPath(final DeviceImageHoursCallback deviceImageHoursCallback,final String path) {
        ApiManager obtainHoursService = retrofit.create(ApiManager.class);
        Call<ArrayList<DeviceSerialBean>> responseBodyCall = obtainHoursService.obtainHoursByPath(path);
        responseBodyCall.enqueue(new Callback<ArrayList<DeviceSerialBean>>() {
            @Override
            public void onResponse(Call<ArrayList<DeviceSerialBean>> call, Response<ArrayList<DeviceSerialBean>> response) {
                if(response != null){
                    ArrayList<DeviceSerialBean> body = response.body();
                    if(deviceImageHoursCallback != null && body != null && body.size() > 0){
                        deviceImageHoursCallback.onSuccess(body);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DeviceSerialBean>> call, Throwable t) {
                if(deviceImageHoursCallback != null){
                    deviceImageHoursCallback.onFailure(t.toString());
                }
            }
        });
    }


    public static void obtainFilesByPath(final DeviceImageFilesCallback deviceImageFilesCallback , final String path) {
        ApiManager obtainFilesService = retrofit.create(ApiManager.class);
        Call<ArrayList<DeviceSerialBean>> responseBodyCall = obtainFilesService.obtainFilesByPath(path);
        responseBodyCall.enqueue(new Callback<ArrayList<DeviceSerialBean>>() {
            @Override
            public void onResponse(Call<ArrayList<DeviceSerialBean>> call, Response<ArrayList<DeviceSerialBean>> response) {
                if(response != null){
                    ArrayList<DeviceSerialBean> body = response.body();
                    if(deviceImageFilesCallback != null && body != null && body.size() > 0){
                        deviceImageFilesCallback.onSuccess(body);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DeviceSerialBean>> call, Throwable t) {
                if(deviceImageFilesCallback != null){
                    deviceImageFilesCallback.onFailure(t.toString());
                }
            }
        });
    }

    public static void obtainAllHourFilesByPath(final AllHoursImagesCallback allHoursImagesCallback,final String path) {
        ApiManager obtainAllHourFilesService = retrofit.create(ApiManager.class);
        Call<HoursImageStreamBean> responseBodyCall = obtainAllHourFilesService.obtainAllHourFilesByPath(path);
        responseBodyCall.enqueue(new Callback<HoursImageStreamBean>() {
            @Override
            public void onResponse(Call<HoursImageStreamBean> call, Response<HoursImageStreamBean> response) {
                if(response != null){
                    HoursImageStreamBean body = response.body();
                    if(allHoursImagesCallback != null && body != null){
                        allHoursImagesCallback.onSuccess(body);
                    }
                }
            }

            @Override
            public void onFailure(Call<HoursImageStreamBean> call, Throwable t) {
                if(allHoursImagesCallback != null ){
                    allHoursImagesCallback.onFailure(t.toString());
                }
            }
        });
    }

    public static void obtainCalibrationBySerialNumber(final CalibrationCallback calibrationCallback,final String serialNumber) {
        ApiManager obtainCalibrationService = retrofit.create(ApiManager.class);
        Call<CalibrationBean> responseBodyCall = obtainCalibrationService.obtainCalibrationBySerialNumber(serialNumber);
        responseBodyCall.enqueue(new Callback<CalibrationBean>() {
            @Override
            public void onResponse(Call<CalibrationBean> call, Response<CalibrationBean> response) {
                if(response != null){
                    CalibrationBean body = response.body();
                    if(calibrationCallback != null && body != null){
                        calibrationCallback.onSuccess(body);
                    }
                }
            }

            @Override
            public void onFailure(Call<CalibrationBean> call, Throwable t) {
                if(calibrationCallback != null ){
                    calibrationCallback.onFailure(t.toString());
                }
            }
        });
    }

    public interface CalibrationCallback{
        void onSuccess(CalibrationBean body);
        void onFailure(String error);
    }

    public interface AllHoursImagesCallback{
        void onSuccess(HoursImageStreamBean body);
        void onFailure(String error);
    }

    public interface DeviceImageFilesCallback{
        void onSuccess(ArrayList<DeviceSerialBean> body);
        void onFailure(String error);
    }

    public interface DeviceImageHoursCallback{
        void onSuccess(ArrayList<DeviceSerialBean> body);
        void onFailure(String error);
    }

    public interface DeviceImageDayCallback{
        void onSuccess(ArrayList<DeviceSerialBean> body);
        void onFailure(String error);
    }
}
