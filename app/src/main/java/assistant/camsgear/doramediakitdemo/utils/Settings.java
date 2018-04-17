/*
 * Created By Edwin Cen <edwin.cen@camdora.me>
 *
 * Copyright (C) 2016 Camdora Inc. All rights reserved.
 *
 */

package assistant.camsgear.doramediakitdemo.utils;

import android.content.Context;

public class Settings {
    private Context mAppContext;

    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__DoraMediaPlayer = 2;
    public static final int PV_PLAYER__DoraExoMediaPlayer = 3;

    public Settings(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public boolean getEnableBackgroundPlay() {
        String key = mAppContext.getString(me.camdora.dora.media.player.R.string.pref_key_enable_background_play);
        return false;
    }

    public int getPlayer() {
        //Use Dora Media Player by default
        return PV_PLAYER__DoraMediaPlayer;
    }

    public boolean getUsingMediaCodec() {
        //DON'T use MediaCodec by default
        return false;
    }

    public boolean getUsingMediaCodecAutoRotate() {
        //DON'T use MediaCodec by default
        return false;
    }

    public boolean getUsingOpenSLES() {
        //Don't Use OpenSLES by default
        return false;
    }

    public String getPixelFormat() {
        //Use OpenGL by default
        return "fcc-_es2";
    }

    public boolean getEnableNoView() {
        return false;
    }

    public boolean getEnableSurfaceView() {
        return true;
    }

    public boolean getEnableTextureView() {
        return false;
    }

    public boolean getEnableDetachedSurfaceTextureView() {
        return false;
    }

}
