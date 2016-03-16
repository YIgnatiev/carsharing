package shedule.budivnictvo.com.shedule;

import android.graphics.Matrix;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Администратор on 08.12.2014.
 */
public class AnimationUpDown extends Animation{

    private int direction;

    public static int DOWN = 1;
    public static int UP = -1;

    public AnimationUpDown(int direction) {

        this.direction = direction;

    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setRepeatCount(1);
        setRepeatMode(REVERSE);
        setDuration(500);
        setFillAfter(true);
        setInterpolator(new AccelerateInterpolator());
        setFillAfter(true);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        Matrix matrix = t.getMatrix();
        float deltaY = interpolatedTime * 500 * direction;
        matrix.setTranslate(0, deltaY);

    }
}
