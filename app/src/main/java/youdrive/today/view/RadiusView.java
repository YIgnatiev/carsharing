package youdrive.today.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import youdrive.today.R;


public class RadiusView extends View {
    private int circleCol, circleRad;
    private Paint circlePaint, LinePaint;

    public RadiusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        circlePaint = new Paint();
        LinePaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RadiusView, 0, 0);
        try {
            circleCol = a.getInteger(R.styleable.RadiusView_circleColor, 0);
            circleRad = a.getInteger(R.styleable.RadiusView_circleRadius, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int Width = this.getMeasuredWidth();
        int Height = this.getMeasuredHeight();

        LinePaint.setColor(Color.GRAY);
        LinePaint.setStrokeWidth(1f);
        canvas.drawLine(0, Height / 2, Width, Height / 2, LinePaint);
        canvas.drawLine(Width / 2, 0, Width / 2, Height, LinePaint);

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(circleCol);
        canvas.drawCircle(Width / 2, Height / 2, circleRad, circlePaint);
    }

    public void setCircleColor(int newColor) {
        //update the instance variable
        circleCol = newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public void setCircleRadius(int newRadius) {
        //update the instance variable
        circleRad = newRadius;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public int getCircleRadius() {
        return circleRad;
    }
}
