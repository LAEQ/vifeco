package org.laeq.category;

import griffon.core.test.GriffonUnitRule;
import griffon.core.test.TestFor;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.fail;

@TestFor(ContainerController.class)
public class ContainerControllerTest {
    static {
        // force initialization JavaFX Toolkit
        new javafx.embed.swing.JFXPanel();
    }

    private ContainerController controller;

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();

    @Test
    public void smokeTest() {
        fail("Not yet implemented!");
    }
}