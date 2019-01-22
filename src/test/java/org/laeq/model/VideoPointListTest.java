package org.laeq.model;

import griffon.core.test.GriffonUnitRule;
import griffon.core.test.TestFor;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.fail;

@TestFor(VideoPointList.class)
public class VideoPointListTest {
    static {
        // force initialization JavaFX Toolkit
        new javafx.embed.swing.JFXPanel();
    }

    private VideoPointList model;

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();

    @Test
    public void smokeTest() {
        fail("Not yet implemented!");
    }
}