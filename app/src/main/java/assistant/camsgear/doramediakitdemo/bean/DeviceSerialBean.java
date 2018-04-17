package assistant.camsgear.doramediakitdemo.bean;

/**
 * Created by camdora on 17-11-21.
 */

public class DeviceSerialBean {

    /**
     * path : PDS11Q79K0044/20171121/
     * host : http://camdora-joowing-demo.oss-cn-hangzhou.aliyuncs.com
     * day : 2017-11-20T16:00:00.000Z
     * bucket : camdora-joowing-demo
     */

    private String path;
    private String host;
    private String day;
    private String bucket;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Override
    public String toString() {
        return "DeviceSerialBean{" +
                "path='" + path + '\'' +
                ", host='" + host + '\'' +
                ", day='" + day + '\'' +
                ", bucket='" + bucket + '\'' +
                '}';
    }
}
