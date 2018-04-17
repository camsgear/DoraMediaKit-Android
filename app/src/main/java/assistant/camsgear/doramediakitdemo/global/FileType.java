package assistant.camsgear.doramediakitdemo.global;

import java.io.Serializable;

/**
 * Created by camdora on 17-11-20.
 */

public enum FileType implements Serializable{
    FILE_TYPE_H265,
    FILE_TYPE_SINGLE_EYE,
    FILE_TYPE_DOUBLE_EYE,
    FILE_TYPE_DOUBLE_FISHEYE_IMAGE,
    FILE_TYPE_IMAGE_STREAM,
    FILE_TYPE_LOCAL_RAW_H264,
    FILE_TYPE_SINGLE_FISHEYE_IMAGE,
    FILE_TYPE_CLOUD_MKV,
    FILE_TYPE_RTSP
}
