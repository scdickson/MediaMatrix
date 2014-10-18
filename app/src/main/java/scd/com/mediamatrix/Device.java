package scd.com.mediamatrix;

import android.app.Application;
import android.graphics.Point;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class Device {
    String deviceID;
    int width;
    int height;
    boolean isVert;
    boolean flipped;
    ArrayList<Point> coords = new ArrayList<Point>();


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

    public Device(String deviceID, int width, int height, boolean isVert, boolean isFlipped)
    {
        this.deviceID = deviceID;
        this.width = width;
        this.height = height;
        this.isVert = isVert;
        this.flipped = isFlipped;
    }

    public void setCoords(Point p, int x, int y) {
    }
    public String toString()
    {
        return (deviceID + ", (" + width + "," + height + "), isVertical=" + isVert + ", isFlipped=" + flipped + ", AT (" + coords.get(0).x + ", " + coords.get(0).y + ")");
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