package org.laeq.db;

import griffon.core.test.GriffonUnitRule;
import griffon.core.test.TestFor;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@TestFor(DatabaseController.class)
public class DatabaseControllerTest {
    static {
        // force initialization JavaFX Toolkit
        new javafx.embed.swing.JFXPanel();
    }

    private DatabaseController controller;

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();

    @Test
    public void smokeTest() {
        assertTrue(true);
    }
}