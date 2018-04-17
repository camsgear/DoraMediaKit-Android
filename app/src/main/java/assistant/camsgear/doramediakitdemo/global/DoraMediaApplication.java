package assistant.camsgear.doramediakitdemo.global;

import android.app.Application;
import android.content.Context;

public class DoraMediaApplication extends Application {
    private final static String TAG = "DoraMediaApplication";

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getAppContext(){
        return context;
    }
}
