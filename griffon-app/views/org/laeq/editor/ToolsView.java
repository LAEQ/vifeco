package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.image.Image;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Category;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconButton;
import org.laeq.model.icon.IconSVG;
import org.laeq.model.icon.IconSquare;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@ArtifactProviderFor(GriffonView.class)
public class ToolsView extends AbstractJavaFXGriffonView {

    @Inject @Nonnull EditorView parentView;

    @Override
    public void mvcGroupDestroy(){

    }

    @Override
    public void initUI() {
        System.out.println("Init UI") ;

        IconButton btn = generateButton(IconSVG.video_plus,"Add video", "org.laeq.menu.tooltip.video_add", "tools.open");
        parentView.tools.getChildren().add(btn);

        btn = generateButton(IconSVG.controls,"Video controls", "org.laeq.menu.tooltip.controls", "tools.controls");
        btn.setLayoutY(60);
        parentView.tools.getChildren().add(btn);

        btn = generateButton(IconSVG.imageControls,"Video controls", "org.laeq.menu.tooltip.image_controls", "tools.image_controls");
        btn.setLayoutY(110);
        parentView.tools.getChildren().add(btn);

        btn = generateButton(IconSVG.draw,"Drawing", "org.laeq.menu.tooltip.drawing", "tools.drawing");
        btn.setLayoutY(160);
        parentView.tools.getChildren().add(btn);

//        btn = generateButton(IconSVG.zoom,"Zoom", "org.laeq.menu.tooltip.drawing", "tools.zoom");
//        btn.setLayoutY(210);
//        parentView.tools.getChildren().add(btn);
    }

    private IconButton generateButton(String path, String name, String help, String eventName){
        Category category = new Category();
        category.setIcon(path);
        category.setColor(Color.gray_dark);
        IconButton btn = new IconButton(new IconSquare(category), 40);
        btn.decorate();

        btn.setOnMouseClicked(event -> {
            getApplication().getEventRouter().publishEvent(eventName);
        });

        btn.setLayoutY(10);

        return btn;
    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }
}
