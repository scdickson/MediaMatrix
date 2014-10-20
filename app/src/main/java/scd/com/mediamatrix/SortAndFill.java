package scd.com.mediamatrix;

<<<<<<< HEAD
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
=======
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
>>>>>>> working
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sdickson on 10/18/14.
 */
public class SortAndFill
{
    static int max_height, max_width;

    public static void sortByHeight()
    {
        Log.d("mm", "SORT BY HEIGHT");


        Collections.sort(MatrixInitialization.devices, new DeviceHeightComparator());
    }

    public static void Pack()
    {
        Log.d("mm", "BEGIN PACK");
        ArrayList<Device> notPositioned = new ArrayList<Device>();
        ArrayList<Device> positioned = new ArrayList<Device>();

        for(Device device : MatrixInitialization.devices)
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
            MatrixInitialization.devices.set(i, positioned.get(i));
            Log.d("mm", MatrixInitialization.devices.get(i).toString());
        }
    }

    public static void setImagePoints(int imageX, int imageY)
    {
        for(Device device : MatrixInitialization.devices)
        {
<<<<<<< HEAD

=======
            int mult = 1;

            if(device.deviceID.equals("275502730001147"))
            {
                //mult = 2;
            }
>>>>>>> working
            int newcoordX = (int)(((double)device.coords.get(0).x/(double)max_width) * imageX);
            int newcoordY = (int)(((double)device.coords.get(0).y/(double)max_height) * imageY);
            device.imagePoint = new Point(newcoordX, newcoordY);
            device.imageWidth = (int)((((double)device.width/(double)max_width)) * imageX) * mult;
            device.imageHeight = (int)((((double)device.height/(double)max_height)) * imageY) * mult;
            Log.d("mm", "x=" + device.imagePoint.x + ", " + "y=" + device.imagePoint.y + ", w=" + device.imageWidth + ", h=" + device.imageHeight);
        }
    }

    public static Canvas drawDevices(){
        Canvas canvas = new Canvas();
        int i = 0;
        for(Device device : MatrixInitialization.devices){
            int left = device.coords.get(0).x;
            int top = device.coords.get(0).y;
            int right = device.coords.get(0).x + device.width;
            int bottom = device.coords.get(0).y + device.height;

            Rect rect = new Rect(left, top, right, bottom);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);

            canvas.drawRect(rect, paint);
            canvas.drawText(i + "", (left+right)/2, (top+bottom)/2, paint);
            i++;
        }
        return canvas;
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
