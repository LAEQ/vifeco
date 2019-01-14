package org.laeq;

import griffon.core.artifact.ArtifactManager;
import griffon.core.test.GriffonUnitRule;
import griffon.core.test.TestFor;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

@TestFor(VifecoController.class)
public class VifecoControllerTest {
    static {
        // force initialization JavaFX Toolkit
        new javafx.embed.swing.JFXPanel();
    }

    @Inject
    private ArtifactManager artifactManager;

    private VifecoController controller;

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();

    @Test
    public void executeClickAction() {
        // given:
        VifecoModel model = artifactManager.newInstance(VifecoModel.class);
        controller.setModel(model);

        // when:
        controller.invokeAction("click");
        await().atMost(2, SECONDS);

        // then:
        assertEquals("1", model.getClickCount());
    }
}
