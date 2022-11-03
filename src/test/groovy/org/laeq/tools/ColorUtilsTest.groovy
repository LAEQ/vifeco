package org.laeq.tools

import javafx.scene.paint.Color
import spock.lang.Specification

class ColorUtilsTest extends Specification
{
    def "test color" (){
        setup:
        Color red = Color.RED

        when:
        String result = ColorUtils.colorToHex(red)

        String expected = '#FF0000'

        then:
        result == expected
    }
}
