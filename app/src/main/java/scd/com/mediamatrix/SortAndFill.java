package scd.com.mediamatrix;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sdickson on 10/18/14.
 */
public class SortAndFill
{
    static ArrayList<Device> devices;
    static int max_height, max_width;

    public static void sortByHeight()
    {
        Log.d("mm", "SORT BY HEIGHT");
        Collections.sort(devices, new DeviceHeightComparator());
    }

    public static void Pack()
    {
        Log.d("mm", "BEGIN PACK");
        ArrayList<Device> notPositioned = new ArrayList<Device>();
        ArrayList<Device> positioned = new ArrayList<Device>();

        for(Device device : devices)
        {
            notPositioned.add(device);
        }

        int x = 0;
        int y = 0;
        int row_height = 0;

        do
        {
            int next_device = -1;
            for(int i = 0; i < notPositioned.size(); i++)
            {
                if(x + notPositioned.get(i).width <= max_width)
                {
                    next_device = i;
                    break;
                }
            }

            if(next_device < 0)
            {
                y += row_height;
                x = 0;
                row_height = 0;
                next_device = 0;
            }

            Device tmp = notPositioned.get(next_device);
            tmp.coords.add(new Point(x,y));
            x += tmp.width;

            if(row_height < tmp.height)
            {
                row_height = tmp.height;
            }

            positioned.add(tmp);
            notPositioned.remove(next_device);

        } while(!notPositioned.isEmpty());

        max_height = row_height;

        for(int i = 0; i < positioned.size(); i++)
        {
            devices.set(i, positioned.get(i));
            Log.d("mm", devices.get(i).toString());
        }
    }

    public static void setImagePoints(int imageX, int imageY)
    {
        for(Device device : devices)
        {
            int newcoordX = (int)(((double)device.coords.get(0).x/(double)max_width) * imageX);
            int newcoordY = (int)(((double)device.coords.get(0).y/(double)max_height) * imageY);
            device.imagePoint = new Point(newcoordX, newcoordY);
            device.imageWidth = (int)((((double)device.width/(double)max_width)) * imageX);
            device.imageHeight = (int)((((double)device.height/(double)max_height)) * imageY);
            Log.d("mm", "x=" + device.imagePoint.x + ", " + "y=" + device.imagePoint.y + ", w=" + device.imageWidth + ", h=" + device.imageHeight);
        }
    }

    static class DeviceHeightComparator implements Comparator<Device> {
        @Override
        public int compare(Device a, Device b)
        {
            if(a.height < b.height)
            {
                return 1;
            }
            else if(a.height == b.height)
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
    }
}
