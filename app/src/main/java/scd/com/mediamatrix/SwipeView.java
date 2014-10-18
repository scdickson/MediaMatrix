package scd.com.mediamatrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sdickson on 10/18/14.
 */
public class SwipeView extends View implements View.OnTouchListener
{
    boolean reset = false;
    Context context;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ArrayList<MMPoint> points = new ArrayList<MMPoint>();

    public SwipeView(Context context)
    {
        super(context);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.BLACK);
        this.setOnTouchListener(this);

        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
    }

    public void onDraw(Canvas canvas)
    {
        Path path = new Path();
        boolean first = true;
        for(MMPoint point : points)
        {
            if(first)
            {
                first = false;
                path.moveTo(point.x, point.y);
            }
            else
            {
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }

    public boolean onTouch(View view, MotionEvent event)
    {
        if(event.getAction() != (MotionEvent.ACTION_UP))
        {
            if(reset)
            {
                points.clear();
                reset = false;
            }

            MMPoint point = new MMPoint();
            point.x = event.getX();
            point.y = event.getY();
            points.add(point);
            invalidate();
            return true;
        }
        else
        {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            boolean isVertical = calculate2DOrientation();
            boolean isFlipped = calculateIsFlipped(isVertical);
            JSONObject deviceparams = new JSONObject();
            try
            {
                deviceparams.put("SERIAL", Build.SERIAL);
                deviceparams.put("WIDTH", size.x);
                deviceparams.put("HEIGHT", size.y);
                deviceparams.put("IS_VERTICAL", isVertical);
                deviceparams.put("IS_FLIPPED", isFlipped);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            MatrixInitialization.myFirebaseRef.child("1234").child(Build.SERIAL).child("json").setValue(deviceparams.toString());

            reset = true;
            return true;
        }
        //return super.onTouchEvent(event);
    }

    public boolean calculateIsFlipped(boolean isVertical)
    {
        MMPoint start = points.get(0);

        if(isVertical)
        {

        }
        else
        {

        }

        return false;
    }

    public boolean calculate2DOrientation()
    {
        MMPoint start = points.get(0);
        MMPoint end = points.get(points.size()-1);
        int slope = (int) ((end.y - start.y)/(end.x - start.x));

        if(slope != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    class MMPoint {
        float x, y;
        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}
