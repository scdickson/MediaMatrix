package scd.com.mediamatrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
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

    public SwipeView(final Context context)
    {
        super(context);
        this.context = context;
        init();
    }

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SwipeView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public void init()
    {
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

            boolean isVertical = calculateIsVertical();
            boolean isFlipped = calculateIsFlipped(isVertical, size);

            //Toast.makeText(context, test, Toast.LENGTH_SHORT).show();

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

            MatrixInitialization.myFirebaseRef.child(MatrixInitialization.SESSION_ID).child(Build.SERIAL).child("json").setValue(deviceparams.toString());

            reset = true;
            return true;
        }
        //return super.onTouchEvent(event);
    }

    public boolean calculateIsFlipped(boolean isVertical, Point dimens)
    {
        Point start = new Point((int) points.get(0).x, (int) points.get(0).y);

        if(isVertical)
        {
            Point LEFT_EDGE = new Point(0, dimens.y / 2);
            Point RIGHT_EDGE = new Point(dimens.x, dimens.y / 2);

            if(distance(start, LEFT_EDGE) < distance(start, RIGHT_EDGE))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            Point TOP_EDGE = new Point(dimens.x / 2, 0);
            Point BOTTOM_EDGE = new Point(dimens.x / 2, dimens.y);

            if(distance(start, TOP_EDGE) < distance(start, BOTTOM_EDGE))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    public boolean calculateIsVertical()
    {
        MMPoint start = points.get(0);
        MMPoint end = points.get(points.size()-1);
        int slope = (int) ((end.y - start.y)/(end.x - start.x));

        if(slope != 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public double distance(Point p1, Point p2)
    {
        return Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow((p2.y - p1.y), 2));
    }

    class MMPoint {
        float x, y;
        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}
