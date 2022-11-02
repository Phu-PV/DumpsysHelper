package phupv.dumpsyshelper;

import android.app.Application;

import phupv.dumpsys.Logger;

public class MyApplication extends Application {

    static {
        Logger.enableAndroidLog(true);
        Logger.enableTimeFormat(true);
    }
}
