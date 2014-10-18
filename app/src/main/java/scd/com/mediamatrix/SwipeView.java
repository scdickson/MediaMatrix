package scd.com.mediamatrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by sdickson on 10/18/14.
 */
public class SwipeView extends View implements View.OnTouchListener
{
    boolean reset = false;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ArrayList<Point> points = new ArrayList<Point>();

    public SwipeView(Context context)
    {
        super(context);
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
        for(Point point : points)
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

            Point point = new Point();
            point.x = event.getX();
            point.y = event.getY();
            points.add(point);
            invalidate();
            return true;
        }
        else
        {
            reset = true;
            return true;
        }
        //return super.onTouchEvent(event);
    }

    class Point {
        float x, y;
        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}
