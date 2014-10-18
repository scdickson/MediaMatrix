package scd.com.mediamatrix;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

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
            this.isVert = jsonObject.getBoolean("IS_VERTICAL");
            this.flipped = jsonObject.getBoolean("IS_FLIPPED");
            //Log.d("test", deviceID + " " + width + " " + height + " " + isVert + " " + flipped);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        WorldCoordSystem.addDevice(this);
    }

    public String toString()
    {
        return (deviceID + ", (" + width + "," + height + "), isVertical=" + isVert + ", isFlipped=" + flipped);
    }

//    public Device(int width, int height, boolean isVert) {
//
//        //this.deviceID = deviceID;
//        this.width = width;
//        this.height = height;
//        this.isVert = isVert;
//        //this.flipped = flipped;
//
//        WorldCoordSystem.addDevice(this);
//    }
}