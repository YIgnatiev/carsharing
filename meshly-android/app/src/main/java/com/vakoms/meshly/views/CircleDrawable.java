package com.vakoms.meshly.views;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by Oleh Makhobey on 22.04.2015.
 * tajcig@ya.ru
 */
public class CircleDrawable extends Drawable {

    private Paint mPaint;
    private Paint mStrokePaint;
    private boolean isCropped = false;
    private Bitmap mBitmap;

    public CircleDrawable(Bitmap _bitmap) {
        mBitmap = _bitmap;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        int height = getBounds().height();
        int width = getBounds().width();


        if (!isCropped) {

            if(mBitmap.getWidth()> mBitmap.getHeight()) {
                float offset = mBitmap.getWidth()/(float)mBitmap.getHeight();
                mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(width*offset), height, true);
            }else{
                float offset = mBitmap.getHeight()/(float)mBitmap.getWidth();
                mBitmap = Bitmap.createScaledBitmap(mBitmap, width, (int)(height*offset), true);
            }



            BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            int heightOffset = mBitmap.getHeight() - height;
            int widthOffset = mBitmap.getWidth() - width;



                Matrix matrix = new Matrix();
                matrix.reset();
                matrix.preTranslate(0, 0);
                matrix.postTranslate(-widthOffset / 2, -heightOffset / 2);
                shader.setLocalMatrix(matrix);


            mPaint.setShader(shader);

            isCropped = true;
        }


        if (height > width) {
            canvas.drawCircle(width / 2, height / 2, width / 2, mPaint);
            if(mStrokePaint != null)
            canvas.drawCircle(width / 2, height / 2, (width-mStrokePaint.getStrokeWidth()) / 2, mStrokePaint);

        } else {
            canvas.drawCircle(width / 2, height / 2, height / 2, mPaint);
            if(mStrokePaint != null)
            canvas.drawCircle(width / 2, height / 2, (height-mStrokePaint.getStrokeWidth()) / 2 , mStrokePaint);
        }


    }

    public void setStroke(int color, float width){
        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(width);
        mStrokePaint.setColor(color);
      //  mStrokePaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL));
    }
    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 255;
    }
}
