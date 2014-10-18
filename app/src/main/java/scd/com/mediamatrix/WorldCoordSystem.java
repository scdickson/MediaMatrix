package scd.com.mediamatrix;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class WorldCoordSystem{

    public static ArrayList<Device> devices = new ArrayList<Device>();
    public static ArrayList<ArrayList<Device>> rows = new ArrayList<ArrayList<Device>>();
    public static int worldWidth = 0;
    public static int worldHeight = 0;

    public WorldCoordSystem() {

    }

    public static void addDevice(Device device){
        devices.add(device);
    }

    public static void addRow(ArrayList<Device> row){
        rows.add(row);
    }

    public static void setWorldCoordSystem() {

        for(ArrayList <Device> row : rows){
            int maxHeight = 0;
            int maxWidth = 0;

            for(Device device : row){
                if(device.isVert){
                    maxWidth += device.width;
                    if(maxHeight < device.height) maxHeight = device.height;
                }
                else {
                    maxWidth += device.height;
                    if(maxHeight < device.width) maxHeight = device.width;
                }
            }
            worldHeight += maxHeight;
            if(maxWidth > worldWidth) worldWidth = maxWidth;

        }
    }
}