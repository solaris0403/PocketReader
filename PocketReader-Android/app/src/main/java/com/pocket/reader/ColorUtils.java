package com.pocket.reader;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by tony on 5/7/18.
 */

public class ColorUtils {
    public static int randomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }
}
