package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.beans.property.SimpleIntegerProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.CategoryService;
import org.laeq.model.VideoPoint;
import org.laeq.video.category.CategoryView;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CategoryController extends AbstractGriffonController {

    @MVCMember @Nonnull private CategoryModel model;
    @MVCMember @Nonnull private CategoryView view;



    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener("video.point.create", videoPoints -> {
            getLog().info("video.point.create");
            runInsideUIAsync(() -> {
                VideoPoint vp = (VideoPoint) videoPoints[0];
                SimpleIntegerProperty property = model.getCategoryProperty(vp.getCategory());
                property.set(property.getValue() + 1);
//                model.setTotalCount(model.getTotalCount() + 1);
            });
        });
    }

    @MVCMember
    public void setModel(@Nonnull CategoryModel model) {
        this.model = model;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void click() {

    }
}