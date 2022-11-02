package phupv.dumpsyshelper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import phupv.dumpsys.Logger;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.log(TAG, "activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startMainServiceButton = findViewById(R.id.start_service_btn);
        startMainServiceButton.setOnClickListener(v -> {
            ComponentName componentName = new ComponentName(MainActivity.this.getPackageName(),
                    MainService.class.getName());
            Intent intent = new Intent().setComponent(componentName);
            MainActivity.this.startService(intent);
        });
    }

    @Override
    protected void onDestroy() {
        Logger.log(TAG, "activity onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Logger.log(TAG, "activity onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Logger.log(TAG, "activity onStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Logger.log(TAG, "activity onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Logger.log(TAG, "activity onPause");
        super.onPause();
    }

    // adb shell dumpsys activity phupv.dumpsyshelper
    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        Logger.dump(fd, writer, args);
    }
}