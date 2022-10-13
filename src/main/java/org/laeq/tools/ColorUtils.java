package org.laeq.tools;

import javafx.scene.paint.Color;

public class ColorUtils {
    public static String colorToHex(Color color) {
        return "#" + colorChanelToHex(color.getRed())
                + colorChanelToHex(color.getGreen())
                + colorChanelToHex(color.getBlue());
    }

    private static String colorChanelToHex(double chanelValue) {
        String rtn = Integer.toHexString((int) Math.min(Math.round(chanelValue * 255), 255));
        if (rtn.length() == 1) {
            rtn = "0" + rtn;
        }
        return rtn.toUpperCase();
    }
}
