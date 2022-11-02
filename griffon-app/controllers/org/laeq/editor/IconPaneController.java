package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.model.Drawing;
import org.laeq.model.Point;
import org.laeq.model.icon.IconPointColorized;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class IconPaneController extends AbstractGriffonController {
    @MVCMember @Nonnull private IconPaneModel model;
    @MVCMember @Nonnull private IconPaneView view;

    @Inject private DatabaseService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Override
    public void mvcGroupDestroy(){

    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("point.adding", objects ->{
            KeyCode key = (KeyCode) objects[0];
            Duration currentTime = model.getCurrentTime();
            Point2D mousePosition = view.getMousePosition();
            final Point point = model.generatePoint(key.getName(), currentTime, mousePosition);

            if(point != null && dbService.pointDAO.create(point)){
                model.addPoint(point);
                view.addIcon(model.getIcon(point));
                getApplication().getEventRouter().publishEventOutsideUI("point.added", Arrays.asList(point));
            }
        });

        list.put("point.deleted", objects ->{
            IconPointColorized icon = model.deletePoint((Point) objects[0]);
            view.removeIcon(icon);
        });

        list.put("icon.deleted", objects -> {
           IconPointColorized icon = (IconPointColorized) objects[0];
        });

        list.put("currentTime.update", objects -> view.updateCurrentTime((Duration) objects[0]));


        list.put("opacity.change", objects -> {
            model.controls.opacity.set((Double) objects[0]);
            view.refreshOpacity((Double) objects[0]);
        });
        list.put("duration.change", objects -> {
            model.controls.duration.set((Double) objects[0]);
            view.updateCurrentTime(model.getCurrentTime());
        });
        list.put("size.change", objects -> {
            model.controls.size.set((Double) objects[0]);
            view.refreshSize((Double) objects[0]);
        });

        list.put("drawing.line.start", args -> view.drawLineStart((String) args[0]));
        list.put("drawing.rectangle.start", args -> view.drawRectangleStart((String) args[0]));
        list.put("drawing.updated", args -> view.drawingUpdated((List<Drawing>) args[0]));
        list.put("drawing.destroyed", args -> view.drawingDestroy());

        return list;
    }
}