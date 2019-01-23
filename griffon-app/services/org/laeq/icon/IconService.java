package org.laeq.icon;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.PointIcon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class IconService extends AbstractGriffonService {
    private int width = 100;
    private int height = 100;
    private float opacity = 0.65f;
    private String fillColor = "#EEEEEE";

    private final String[] icons = new String[]{
            "icons/truck-mvt-blk.png",
            "icons/truck-mvt-red.png",
            "icons/icon-bicycle-mvt-64.png",
            "icons/icon-car-co2-black-64.png",
            "icons/icon-constr-black-64.png",
            "icons/iconmonstr-car-23-64.png",
            "icons/icon-car-elec-black-64.png",
    };

    public Group generateIcon(MouseEvent mouseEvent) throws FileNotFoundException {
        int rand = (int)(Math.random() * 10) % icons.length;

        PointIcon pointIcon = new PointIcon(width, height,icons[rand]);
        String path = getApplication().getResourceHandler().getResourceAsURL(pointIcon.getImagePath()).getPath();
        FileInputStream inputStream = new FileInputStream(path);

        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(opacity);
        imageView.setX((pointIcon.getWidth() - image.getWidth()) / 2);
        imageView.setY((pointIcon.getHeight() - image.getHeight()) / 2);

        Canvas canvas = new Canvas(pointIcon.getWidth(), pointIcon.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf(fillColor));
        gc.fillOval(0,0, pointIcon.getWidth(), pointIcon.getHeight());

        pointIcon.getChildren().addAll(canvas, imageView);

        pointIcon.setLayoutX(mouseEvent.getX() - pointIcon.getWidth() / 2);
        pointIcon.setLayoutY(mouseEvent.getY() - pointIcon.getHeight() / 2);
        pointIcon.setOpacity(opacity);

        return pointIcon;
    }
}
