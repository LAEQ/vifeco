package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.collections.ListChangeListener;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.model.Drawing;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;


@ArtifactProviderFor(GriffonController.class)
public class DrawingController extends AbstractGriffonController {
    @MVCMember @Nonnull private DrawingView view;
    @MVCMember @Nonnull private DrawingModel model;

    @Inject DatabaseService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());

        model.drawingList.addListener((ListChangeListener<Drawing>) c -> {
            while(c.next()){
                updateDrawing();
            }
        });

        try {
            model.drawingList.addAll(dbService.drawingDAO.findAll());
            getApplication().getEventRouter().publishEventAsync("status.info", Arrays.asList("db.draw.fetch.success"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("db.draw.fetch.error"));
        }
    }

    @Override
    public void mvcGroupDestroy(){

    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void line() throws Exception {
        getApplication().getEventRouter().publishEvent("drawing.line.start", Arrays.asList(model.color.get()));
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void rectangle() throws Exception {
        getApplication().getEventRouter().publishEvent("drawing.rectangle.start", Arrays.asList(model.color.get()));
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("drawing.created", objets -> {
            Drawing drawing = (Drawing) objets[0];
            drawing.setColor(model.color.get());
            if(dbService.drawingDAO.create(drawing)){
                drawing.setActive(true);
                model.drawingList.add((Drawing) objets[0]);
                getApplication().getEventRouter().publishEvent("drawing.updated", Arrays.asList(model.getVisibleDrawing()));
            } else {
                getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("editor.drawing.create.error"));
            }
        });
        return list;
    }

    public void delete(Drawing drawing) {
        if(dbService.drawingDAO.delete(drawing)){
            model.drawingList.remove(drawing);
        }
    }

    private void updateDrawing() {
        List<Drawing> list = model.getVisibleDrawing();

        getApplication().getEventRouter().publishEvent("drawing.updated", Arrays.asList(model.getVisibleDrawing()));
    }

    public void update(Drawing drawing){
        if(dbService.drawingDAO.create(drawing)){
            model.drawingList.remove(drawing);
            model.drawingList.add(drawing);
        }
    }

    public void updateSelection() {
        getApplication().getEventRouter().publishEvent("drawing.updated", Arrays.asList(model.getVisibleDrawing()));
    }
}
