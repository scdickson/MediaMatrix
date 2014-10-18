package scd.com.mediamatrix;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class Device {
    int deviceID;
    int width;
    int height;
    boolean flipped;

    public Device(int width, int height, boolean flipped) {
        this.width = width;
        this.height = height;
        this.flipped = flipped;
        WorldCoordSystem.addDevice(this);
    }
}