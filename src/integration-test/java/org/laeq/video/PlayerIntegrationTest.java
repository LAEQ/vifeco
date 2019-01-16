package org.laeq.video;

import griffon.javafx.test.GriffonTestFXRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PlayerIntegrationTest {
    @Rule
    public GriffonTestFXRule testfx = new GriffonTestFXRule("player");

    @Test
    public void smokeTest(){
        assertTrue(true);
    }
}