package phupv.dumpsys;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public interface Dumpable {
    void dump(FileDescriptor fd, PrintWriter pw, String[] args);
}
