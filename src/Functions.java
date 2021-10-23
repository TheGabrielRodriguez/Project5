import java.util.*;

import processing.core.PImage;
import processing.core.PApplet;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions
{
    private static final Random rand = new Random();

    private static final int COLOR_MASK = 0xffffff;


    private static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right",
            "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));




    public static int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }

    /*
      Called with color for which alpha should be set and alpha value.
      setAlpha(img, color(255, 255, 255), 0));
    */
    public static void setAlpha(PImage img, int maskColor, int alpha) {
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */


    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    // create methods: keep it, decide whether to leave it in function or move it (leave it in functions)

}
