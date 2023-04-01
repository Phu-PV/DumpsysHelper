package phupv.dumpsys;

import android.text.TextUtils;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;

public class Logger {
    private static boolean mAndroidLogEnabled = true;
    private static final HashMap<String, TopicLog> sTopicLogs = new HashMap<>();

    public static void enableAndroidLog(boolean enable) {
        mAndroidLogEnabled = enable;
    }

    public static void enableTimeFormat(boolean enable) {
        TopicLog.sFormatTime = enable;
    }

    public static void log(String tag, String msg) {
        log(TopicLog.TOPIC_NONE, tag, msg);
    }

    public static void log(String topic, String tag, String msg) {
        if (mAndroidLogEnabled) {
            Log.d(tag, msg);
        }
        if (topic == null) {
            topic = TopicLog.TOPIC_NONE;
        }
        TopicLog topicLog = sTopicLogs.get(topic);
        if (topicLog == null) {
            topicLog = new TopicLog(topic);
            sTopicLogs.put(topic, topicLog);
        }
        topicLog.addLog(tag, msg);
    }

    public static void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        String formatTime = null;
        String enableAndroidLog = null;
        String topic = null;

        // extract options
        int i = 0;
        String option;
        String param;
        while (i < args.length) {
            option = args[i];
            switch (option) {
                case "--formatTime":
                    param = (i + 1) < args.length ? args[i + 1] : null;
                    if (TextUtils.equals(param, "true") || TextUtils.equals(param, "false")) {
                        formatTime = param;
                    }
                    i++;
                    break;
                case "--enableAndroidLog":
                    param = (i + 1) < args.length ? args[i + 1] : null;
                    if (TextUtils.equals(param, "true") || TextUtils.equals(param, "false")) {
                        enableAndroidLog = param;
                    }
                    i++;
                    break;
                case "--topic":
                    param = (i + 1) < args.length ? args[i + 1] : null;
                    if (param != null) {
                        topic = param;
                    }
                    i++;
                    break;
                default:
                    i++;
            }
        }

        // option: enable android.util.Log
        if (enableAndroidLog != null) {
            if (TextUtils.equals(enableAndroidLog, "true")) {
                mAndroidLogEnabled = true;
            } else if (TextUtils.equals(enableAndroidLog, "false")) {
                mAndroidLogEnabled = false;
            }
            pw.println("AndroidLogEnabled=" + mAndroidLogEnabled);
        }

        // option: format time
        if (formatTime != null) {
            if (TextUtils.equals(formatTime, "true")) {
                TopicLog.sFormatTime = true;
            } else if (TextUtils.equals(formatTime, "false")) {
                TopicLog.sFormatTime = false;
            }
        }

        // option: dump specified topic
        if (topic != null) {
            String topicName = topic;
            Collection<TopicLog> topicLogs = sTopicLogs.values();
            topicLogs.forEach(topicLog -> {
                if (TextUtils.equals(topicLog.mTopic, topicName)) {
                    topicLog.dump(fd, pw, args);
                }
            });
            return;
        }

        // no specified option, dump all logs
        Collection<TopicLog> topicLogs = sTopicLogs.values();
        topicLogs.forEach(topicLog -> topicLog.dump(fd, pw, args));
    }
}
