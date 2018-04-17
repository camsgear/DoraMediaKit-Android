package assistant.camsgear.doramediakitdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by camdora on 17-11-21.
 */

public class FormatUtil {
    public static String dateConvert(String date){
        String formatData = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            formatData = new SimpleDateFormat("yyyy年MM月dd日").format(format.parse(date.replace("Z", " UTC")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatData;
    }

    public static int nameParseMinute(String imageName){
        if(null == imageName || "".equals(imageName)){
            return -1;
        }
        String[] split = imageName.split("/");
        String substring = split[split.length - 1].substring(0, split[split.length - 1].indexOf("."));
        return Integer.valueOf(substring);
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
    }

    public static int getCurrentHour(){
        return getCalendar().get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute(){
        return getCalendar().get(Calendar.MINUTE);
    }

    private static Calendar getCalendar(){
        return Calendar.getInstance();
    }
}
