package smoothie;

import processing.core.PApplet;

public class PUtils {
    /** utility funtion to convert hex color to int rgb */
    public static int fromHex(int hex, PApplet processing) {
        // shifting by bit, 8 bits for each channel
        int red = hex >> 16;

        // "& 0xff" bitwise &, to filter lowest 8 bit only
        int green = (hex >> 8) & 0xFF;
        int blue = hex & 0xFF;
        return processing.color(red, green, blue);
    }
}
