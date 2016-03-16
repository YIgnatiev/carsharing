package com.example.randompainter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class SingleTouchView extends View{
    private Paint paint = new Paint();
    private Path path = new Path();
    private Random rand;
    public SingleTouchView(Context context , AttributeSet attrs){
        super(context,attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        rand = new Random();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String time = String.valueOf(System.currentTimeMillis());
                canvas.drawText(time, getWidth()/2, getHeight()/2 ,paint);
        canvas.drawPath(path,paint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
       switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                    randomColor();
                    randomShape((int)eventX, (int)eventY);
                return true;
            case MotionEvent.ACTION_MOVE:

                break;
           case MotionEvent.ACTION_UP:
               break;
            default:
                return false;
        }
            invalidate();
            return true;

    }
    private void randomShape(int x , int y) {

        switch (rand.nextInt(3)){

        case 0:path.addRect(x, y, x + 30, y + 30, Path.Direction.CCW);
            break;
        case 1:path.addCircle(x, y, 15, Path.Direction.CCW);
            break;
        case 2:

        path.moveTo(x, y);
        path.lineTo(x + 30, y + 30);
        path.lineTo(x - 30 , y + 30);
        path.close();
            break;
        default:
            Toast.makeText(getContext(), "Kakajato huinia" , Toast.LENGTH_SHORT).show();
        }

    }
    private void randomColor (){

        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        paint.setColor(Color.rgb(r,g,b));
    }
}
