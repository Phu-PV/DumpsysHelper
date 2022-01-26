package phupv.dumpsys;

import android.text.TextUtils;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.TimeZone;

final class TopicLog implements Dumpable {
    public static final String TOPIC_NONE = "TOPIC_NONE";
    public static boolean sFormatTime = false;
    private static final Calendar sCalendar = Calendar.getInstance();

    public final String mTopic;
    private final MessageQueue mMessageQueue = new MessageQueue();

    public TopicLog(String topic) {
        mTopic = topic;
        sCalendar.setTimeZone(TimeZone.getDefault());
    }

    public void addLog(String tag, String msg) {
        mMessageQueue.enqueue(System.currentTimeMillis(), tag, msg);
    }

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println();
        pw.print(" ********** BEGIN ");
        if (!TextUtils.equals(mTopic, TOPIC_NONE)) {
            pw.print("- topic: " + mTopic + " ");
        }
        pw.println("**************************************************** ");
        mMessageQueue.print(pw);
        pw.print(" **********  END  ");
        if (!TextUtils.equals(mTopic, TOPIC_NONE)) {
            pw.print("- topic: " + mTopic + " ");
        }
        pw.println("**************************************************** ");
    }

    private static class MessageQueue {
        private static final int MAX_MESSAGE_QUEUE = 500;
        private final Message[] queue = new Message[MAX_MESSAGE_QUEUE];
        private int latestIndex = -1;

        public void enqueue(long time, String tag, String msg) {
            ++latestIndex;
            if (latestIndex >= MAX_MESSAGE_QUEUE) {
                latestIndex = 0;
            }
            if (queue[latestIndex] == null) {
                queue[latestIndex] = new Message();
            }
            queue[latestIndex].setBody(time, tag, msg);
        }

        public void print(PrintWriter pw) {
            for (int i = latestIndex + 1; i < MAX_MESSAGE_QUEUE; ++i) {
                if (queue[i] == null) {
                    break;
                }
                printMessage(pw, queue[i]);
            }
            for (int j = 0; j <= latestIndex; ++j) {
                if (queue[j] == null) {
                    break;
                }
                printMessage(pw, queue[j]);
            }
        }

        private void printMessage(PrintWriter pw, Message msg) {
            String time;
            if (sFormatTime) {
                sCalendar.setTimeInMillis(msg.time);
                time = sCalendar.get(Calendar.YEAR) + "-"
                        + (sCalendar.get(Calendar.MONTH) + 1) + "-"
                        + sCalendar.get(Calendar.DAY_OF_MONTH) + " "
                        + sCalendar.get(Calendar.HOUR) + ":"
                        + sCalendar.get(Calendar.MINUTE) + ":"
                        + sCalendar.get(Calendar.SECOND) + "."
                        + sCalendar.get(Calendar.MILLISECOND);
            } else {
                time = String.valueOf(msg.time);
            }
            pw.println(time + " " + msg.tag + ": " + msg.msg);
        }
    }

    private static class Message {
        private long time;
        private String tag;
        private String msg;

        private Message() {
        }

        private void setBody(long time, String tag, String msg) {
            this.time = time;
            this.tag = tag;
            this.msg = msg;
        }
    }
}
