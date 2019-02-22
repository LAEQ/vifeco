package org.laeq.icon;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.VideoPoint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ThreadLocalRandom;


@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class IconService extends AbstractGriffonService {
    private int width = 100;
    private int height = 100;
    private float opacity = 0.65f;
    private int duration = 10;
    private int size = 100;
    private String fillColor = "#EEEEEE";
    private String defaultPath = "icons/iconmonstr-help-3-64.png";

    private final String[] icons = new String[]{
            "icons/truck-mvt-blk-64.png",
            "icons/truck-mvt-red-64.png",
            "icons/icon-bicycle-mvt-64.png",
            "icons/icon-car-co2-black-64.png",
            "icons/icon-constr-black-64.png",
            "icons/iconmonstr-car-23-64.png",
            "icons/icon-car-elec-blk-64.png",
    };

    public VideoPoint generatePoint(Point2D point, Duration start) throws FileNotFoundException {
        int rand = (int) (Math.random() * 10) % icons.length;

        Icon icon = generateIcon(rand);

        String name = icons[rand].substring(6, icons[rand].lastIndexOf('.') - 1);
        String path = getApplication().getResourceHandler().getResourceAsURL(icons[rand]).getPath();
        Category category = new Category(name, path, "F000000", "1");

        return null;
    }

    public Icon generateIcon(int rand)  {
        return generateIcon(new Category("", icons[rand], "#000000", ""), this.size, this.opacity);
    }

    public Icon generateIcon(Category category) {
      return generateIcon(category, this.size, this.opacity);
    }

    public Icon generateIcon(Category category, int size, double opacity) {



        return new Icon(category, size);
    }


    public Icon generateRandomIcon() {
        return generateIcon(new Category("", icons[getRandom()], "#000000", ""), this.size, this.opacity);
    }

    public Icon generateRandomIcon(int size) {
        return generateIcon(new Category("", icons[getRandom()], "#000000", ""), size, this.opacity);
    }

    private int getRandom(){
        return ThreadLocalRandom.current().nextInt(0, icons.length - 1);
    }
}
