package scd.com.mediamatrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.firebase.client.Firebase;

/**
 * Created by sdickson on 10/18/14.
 */
public class DeviceLayoutView extends View
{
    boolean reset = false;
    Context context;
    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    public DeviceLayoutView(final Context context)
    {
        super(context);
        this.context = context;
        init();
    }

    public DeviceLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DeviceLayoutView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public void init()
    {
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint.setColor(Color.DKGRAY);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        paint.setStrokeWidth(5);
    }

    public void onDraw(Canvas canvas)
    {
        int i = 1;
        RectangleLayout deviceLayout;
        for(Device device : MatrixInitialization.devices)
        {

            float left = device.coords.get(0).x/10f+2;
            float top = device.coords.get(0).y/10f+2;
            float right = (device.coords.get(0).x + device.width)/10f+2;
            float bottom = (device.coords.get(0).y + device.height)/10f+2;

            MatrixInitialization.myFirebaseRef.child(device.deviceID).child("order").setValue(i);
            deviceLayout = new RectangleLayout(left, top, right, bottom, i);
            deviceLayout.draw(canvas);
            i++;
        }
        canvas.drawRect(0, 0, 500, 500, paint);
    }

    private class RectangleLayout {
        float startX=0;
        float startY=0;
        float endX=0;
        float endY=0;
        int orderNum = 0;
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);


        public RectangleLayout(float startX, float startY, float endX, float endY, int orderNum) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.orderNum = orderNum;
            p.setColor(Color.DKGRAY);
            p.setDither(true);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeJoin(Paint.Join.ROUND);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setAntiAlias(true);
        }

        public void setOrderNum(int num) {
            this.orderNum = num;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void draw(Canvas c) {
            // Draw the order number in the center of the rectangle
            p.setTextSize(30f);
            p.setStrokeWidth(2);
            p.setTextAlign(Paint.Align.CENTER);
            p.setStyle(Paint.Style.FILL);
            c.drawText(orderNum+"", startX+Math.abs((endX-startX)/2f), startY+Math.abs((endY-startY)/2f), p);
            p.setStyle(Paint.Style.STROKE);

            // Draw the rectangle
            p.setStrokeWidth(5);
            //c.drawRect(startX, startY, endX, endY, p);

            Rect rect = new Rect((int)startX, (int)startY, (int)endX, (int)endY);
            RectF rectF = new RectF(rect);
            c.drawRoundRect(rectF, 10, 10, p);
        }
    }
}
