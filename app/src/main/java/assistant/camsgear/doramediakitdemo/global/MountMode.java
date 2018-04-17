package assistant.camsgear.doramediakitdemo.global;

/**
 * Created by edwin on 27/11/2017.
 */

public enum MountMode {
    MOUNT_MODE_DESKTOP,
    MOUNT_MODE_WALL,
    MOUNT_MODE_CEILING {
        @Override
        public MountMode next() {
            return values()[0];
        }
    };

    public MountMode next() {
        return values()[ordinal() + 1];
    }
}
