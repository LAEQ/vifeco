package org.laeq;

import griffon.javafx.test.GriffonTestFXRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestIntegrationTest {
    @Rule
    public GriffonTestFXRule testfx = new GriffonTestFXRule("test");

    @Test
    public void smokeTest(){
        assertTrue(true);
    }
}