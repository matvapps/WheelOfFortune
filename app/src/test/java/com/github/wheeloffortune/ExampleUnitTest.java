package com.github.wheeloffortune;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {


        assertEquals(getSpeedBy(3, vectorLength(0, 0, 9, 0)), 7, 0.01);
    }


    private float vectorLength(float x_start, float y_start, float x_end, float y_end) {
        return (float) Math.sqrt(Math.pow(x_end - x_start, 2) + Math.pow(y_end - y_start, 2));
    }

    private float getSpeedBy(long time, float length) {
        float speed;

        speed = length / time;

        return speed;
    }

}