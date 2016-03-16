package pro.theboard.custom_views;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/20/15.
 */
public class RoundedDrawable extends Drawable {

        private Paint paint;
        private float mRadius;
        public RoundedDrawable(int _color ,float _radius) {


            this.mRadius = _radius;
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(_color);

        }

        @Override
        public void draw(Canvas canvas) {
            int height = getBounds().height();
            int width = getBounds().width();
            RectF rect = new RectF(0.0f, 0.0f, width, height);
            RectF rect2 = new RectF(0.0f,0.0f,width,height/2);

            canvas.drawRoundRect(rect, mRadius, mRadius, paint);
            canvas.drawRect(rect2,paint);




        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

    }


