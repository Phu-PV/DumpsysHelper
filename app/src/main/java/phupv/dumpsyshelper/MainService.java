package phupv.dumpsyshelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import phupv.dumpsys.Logger;

public class MainService extends Service {
    private static final String TOPIC = "SERVICE";
    private static final String TAG = "MainService";

    @Override
    public void onCreate() {
        Logger.log(TOPIC, TAG, "service onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Logger.log(TOPIC, TAG, "service onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.log(TOPIC, TAG, "service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.log(TOPIC, TAG, "service onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Logger.log(TOPIC, TAG, "service onRebind");
        super.onRebind(intent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Logger.dump(fd, writer, new String[]{"--topic", TOPIC, "--formatTime", "true"});
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.log(TOPIC, TAG, "service onBind");
        return null;
    }
}
