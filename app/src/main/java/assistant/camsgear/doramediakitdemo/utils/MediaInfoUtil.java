package assistant.camsgear.doramediakitdemo.utils;

import java.io.Serializable;


/**
 * Created by camdora on 17-3-14. 10:53
 */

public class MediaInfoUtil {
    private final static String TAG = "MediaInfoUtil";

    public final static String MEDIA_INFO = "MediaInfo";

    //projection	一帧中单幅画面的投影类型	0: Fisheye, 1: Equirectangular; 2: Normal; 3: Wide-Angel
    public final static int CDACamdoraMediaInfoProjectionFisheye = 0;
    public final static int CDACamdoraMediaInfoProjectionEquirectangular = 1;
    public final static int CDACamdoraMediaInfoProjectionNormal = 2;
    public final static int CDACamdoraMediaInfoProjectionWideAngle = 3;

    //mount	一帧中单幅画面的Mount方式	0: Desktop; 1: Wall; 2: Ceiling
    public final static int CDACamdoraMediaInfoMountDesktop = 0;
    public final static int CDACamdoraMediaInfoMountWall = 1;
    public final static int CDACamdoraMediaInfoMountCeiling = 2;

    public static class MediaInfoBean implements Serializable {
        private int camdoraMediaInfoCount;
        private int camdoraMediaInfoHeight;
        private int camdoraMediaInfoWidth;
        private int camdoraMediaInfoFov;
        private int camdoraMediaInfoOrder;
        private int camdoraMediaInfoOrientation;
        private int camdoraMediaInfoMount;
        private int camdoraMediaInfoProjection;
        private boolean camdoraMediaInfoNeedStitch;

        public MediaInfoBean(String mediaInfoString){
            String[] strArr = mediaInfoString.split("-");
            this.camdoraMediaInfoCount = Integer.parseInt(strArr[0]);
            this.camdoraMediaInfoHeight = Integer.parseInt(strArr[1]);
            this.camdoraMediaInfoWidth = Integer.parseInt(strArr[2]);
            this.camdoraMediaInfoFov = Integer.parseInt(strArr[3]);
            this.camdoraMediaInfoOrder = Integer.parseInt(strArr[4]);
            this.camdoraMediaInfoOrientation = Integer.parseInt(strArr[5]);
            this.camdoraMediaInfoMount = Integer.parseInt(strArr[6]);
            this.camdoraMediaInfoProjection = Integer.parseInt(strArr[7]);
            this.camdoraMediaInfoNeedStitch = Boolean.parseBoolean(strArr[8]);
        }

        public MediaInfoBean(int camdoraMediaInfoCount,
                             int camdoraMediaInfoHeight,
                             int camdoraMediaInfoWidth,
                             int camdoraMediaInfoFov,
                             int camdoraMediaInfoOrder,
                             int camdoraMediaInfoOrientation,
                             int camdoraMediaInfoMount,
                             int camdoraMediaInfoProjection,
                             boolean camdoraMediaInfoNeedStitch){

            this.camdoraMediaInfoCount = camdoraMediaInfoCount;
            this.camdoraMediaInfoHeight = camdoraMediaInfoHeight;
            this.camdoraMediaInfoWidth = camdoraMediaInfoWidth;
            this.camdoraMediaInfoFov = camdoraMediaInfoFov;
            this.camdoraMediaInfoOrder = camdoraMediaInfoOrder;
            this.camdoraMediaInfoOrientation = camdoraMediaInfoOrientation;
            this.camdoraMediaInfoMount = camdoraMediaInfoMount;
            this.camdoraMediaInfoProjection = camdoraMediaInfoProjection;
            this.camdoraMediaInfoNeedStitch = camdoraMediaInfoNeedStitch;
        }

        public int getCamdoraMediaInfoCount() {
            return camdoraMediaInfoCount;
        }

        public void setCamdoraMediaInfoCount(int camdoraMediaInfoCount) {
            this.camdoraMediaInfoCount = camdoraMediaInfoCount;
        }

        public int getCamdoraMediaInfoHeight() {
            return camdoraMediaInfoHeight;
        }

        public void setCamdoraMediaInfoHeight(int camdoraMediaInfoHeight) {
            this.camdoraMediaInfoHeight = camdoraMediaInfoHeight;
        }

        public int getCamdoraMediaInfoWidth() {
            return camdoraMediaInfoWidth;
        }

        public void setCamdoraMediaInfoWidth(int camdoraMediaInfoWidth) {
            this.camdoraMediaInfoWidth = camdoraMediaInfoWidth;
        }

        public int getCamdoraMediaInfoFov() {
            return camdoraMediaInfoFov;
        }

        public void setCamdoraMediaInfoFov(int camdoraMediaInfoFov) {
            this.camdoraMediaInfoFov = camdoraMediaInfoFov;
        }

        public int getCamdoraMediaInfoOrder() {
            return camdoraMediaInfoOrder;
        }

        public void setCamdoraMediaInfoOrder(int camdoraMediaInfoOrder) {
            this.camdoraMediaInfoOrder = camdoraMediaInfoOrder;
        }

        public int getCamdoraMediaInfoOrientation() {
            return camdoraMediaInfoOrientation;
        }

        public void setCamdoraMediaInfoOrientation(int camdoraMediaInfoOrientation) {
            this.camdoraMediaInfoOrientation = camdoraMediaInfoOrientation;
        }

        public int getCamdoraMediaInfoMount() {
            return camdoraMediaInfoMount;
        }

        public void setCamdoraMediaInfoMount(int camdoraMediaInfoMount) {
            this.camdoraMediaInfoMount = camdoraMediaInfoMount;
        }

        public int getCamdoraMediaInfoProjection() {
            return camdoraMediaInfoProjection;
        }

        public void setCamdoraMediaInfoProjection(int camdoraMediaInfoProjection) {
            this.camdoraMediaInfoProjection = camdoraMediaInfoProjection;
        }

        public boolean isCamdoraMediaInfoNeedStitch() {
            return camdoraMediaInfoNeedStitch;
        }

        public void setCamdoraMediaInfoNeedStitch(boolean camdoraMediaInfoNeedStitch) {
            this.camdoraMediaInfoNeedStitch = camdoraMediaInfoNeedStitch;
        }

        /**
         *  是否是运动
         * @return
         */
        public boolean isNormal() {
            return this.camdoraMediaInfoCount == 1 &&
                    this.camdoraMediaInfoProjection == CDACamdoraMediaInfoProjectionNormal;
        }

        /**
         *  是否是3D
         * @return
         */
        public boolean isDualNormal() {
            return this.camdoraMediaInfoCount == 2 &&
                    this.camdoraMediaInfoProjection == CDACamdoraMediaInfoProjectionNormal;
        }

        /**
         *  是否是全景
         * @return
         */
        public  boolean isPanorama() {
            return (this.camdoraMediaInfoCount == 2 && this.camdoraMediaInfoNeedStitch) ||
                    (this.camdoraMediaInfoCount == 1 && this.camdoraMediaInfoProjection ==
                            CDACamdoraMediaInfoProjectionEquirectangular) ||
                    (this.camdoraMediaInfoCount == 1 && this.camdoraMediaInfoProjection ==
                            CDACamdoraMediaInfoProjectionFisheye && this.camdoraMediaInfoMount ==
                            CDACamdoraMediaInfoMountDesktop);
        }

        /**
         * 是否是单鱼眼
         * @return
         */
        public  boolean isSingleFisheye() {
            return (!this.camdoraMediaInfoNeedStitch &&
                    this.camdoraMediaInfoCount == 1 &&
                    this.camdoraMediaInfoProjection == CDACamdoraMediaInfoProjectionFisheye);
        }

        /**
         * 是否是双鱼眼
         * @return
         */
        public  boolean isDualFisheyeStitch() {
            return (this.camdoraMediaInfoNeedStitch && this.camdoraMediaInfoCount == 2 &&
                    this.camdoraMediaInfoProjection == CDACamdoraMediaInfoProjectionFisheye);
        }

        /**
         * 是否是 1:1　的普通视频
         * @return
         */
        public  boolean isNormalFisheyeVideo(){
            return (this.camdoraMediaInfoCount == 1 &&
                    this.camdoraMediaInfoWidth == this.camdoraMediaInfoHeight);
        }


        @Override
        public String toString() {
//            return "MediaInfoBean{" +
//                    "camdoraMediaInfoCount=" + camdoraMediaInfoCount +
//                    ", camdoraMediaInfoHeight=" + camdoraMediaInfoHeight +
//                    ", camdoraMediaInfoWidth=" + camdoraMediaInfoWidth +
//                    ", camdoraMediaInfoFov=" + camdoraMediaInfoFov +
//                    ", camdoraMediaInfoOrder=" + camdoraMediaInfoOrder +
//                    ", camdoraMediaInfoOrientation=" + camdoraMediaInfoOrientation +
//                    ", camdoraMediaInfoMount=" + camdoraMediaInfoMount +
//                    ", camdoraMediaInfoProjection=" + camdoraMediaInfoProjection +
//                    ", camdoraMediaInfoNeedStitch=" + camdoraMediaInfoNeedStitch +
//                    '}';
            return camdoraMediaInfoCount + "-" +
                    camdoraMediaInfoHeight + "-" +
                    camdoraMediaInfoWidth + "-" +
                    camdoraMediaInfoFov + "-" +
                    camdoraMediaInfoOrder + "-" +
                    camdoraMediaInfoOrientation + "-" +
                    camdoraMediaInfoMount + "-" +
                    camdoraMediaInfoProjection + "-" +
                    camdoraMediaInfoNeedStitch;
        }
    }

    /**
     * 宽高可以不设置
     */
    public static MediaInfoBean getNormalMediaInfoBean(){
        return new MediaInfoUtil.MediaInfoBean(1, 0, 0, 180, 0, 90, 0, 2, false);
    }

    public static MediaInfoBean getDoubleMediaInfoBean(){
        return new MediaInfoUtil.MediaInfoBean(2,0,0,180,0,90,0,0,true);
    }

    public static MediaInfoBean getEquirectangularMediaInfoBean(){
        return new MediaInfoUtil.MediaInfoBean(1, 0, 0, 360, 0, 0, 0, 1, false);
    }

    public static MediaInfoBean getMonoMediaInfoBean(){
        return new MediaInfoUtil.MediaInfoBean(1, 0, 0, 180, 0, 0, 0, 0, false);
    }
}
