package org.laeq.icon;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.PointIcon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class IconService extends AbstractGriffonService {

    public Group generateIcon(PointIcon pointIcon) throws FileNotFoundException {

        String path = getApplication().getResourceHandler().getResourceAsURL(pointIcon.getImagePath()).getPath();
        FileInputStream inputStream = new FileInputStream(path);

        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.6);
        imageView.setX((pointIcon.getWidth() - image.getWidth()) / 2);
        imageView.setY((pointIcon.getHeight() - image.getHeight()) / 2);

        Canvas canvas = new Canvas(pointIcon.getWidth(), pointIcon.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#EEEEEE"));
        gc.fillOval(0,0, pointIcon.getWidth(), pointIcon.getHeight());

        pointIcon.getChildren().addAll(canvas, imageView);

        return pointIcon;
    }

}
