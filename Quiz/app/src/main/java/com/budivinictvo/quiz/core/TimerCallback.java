package com.budivinictvo.quiz.core;

/**
 * Created by Администратор on 08.01.2015.
 */
public interface TimerCallback {
    public void onTick(long milliseconds);
    public void onFinish();
}
