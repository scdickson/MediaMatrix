package scd.com.mediamatrix;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class Device {
    String deviceID;
    int width;
    int height;
    boolean isVert;
    boolean flipped;

    public Device(JSONObject jsonObject) {
        try {
            this.deviceID = jsonObject.getString("SERIAL");
            this.width = jsonObject.getInt("WIDTH");
            this.height = jsonObject.getInt("HEIGHT");
            this.isVert = jsonObject.getBoolean("IS_VERT");
            this.flipped = jsonObject.getBoolean("IS_FLIPPED");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        this.width = width;
//        this.height = height;
//        this.flipped = flipped;
        WorldCoordSystem.addDevice(this);
    }
}