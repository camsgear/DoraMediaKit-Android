package assistant.camsgear.doramediakitdemo.global;

import java.io.Serializable;

/**
 * Created by edwin on 25/11/2017.
 */

public enum PanoMode implements Serializable {
    PANO_MODE_FISHEYE,
    PANO_MODE_LITTLE_PLANET,
    PANO_MODE_NORMAL {
        @Override
        public PanoMode next() {
            return values()[0];
        }
    };

    public PanoMode next() {
        return values()[ordinal() + 1];
    }
}
