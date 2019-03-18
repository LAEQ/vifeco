package org.laeq.menu;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import org.laeq.model.icon.Color;

public class Button extends Group {
    private final String svgPath;
    private final String color;
    private final String name;
    private final String help;
    private final String eventName;
    private final Rectangle rectangle;

    private final EventHandler<MouseEvent> mouseEnter;
    private final EventHandler<MouseEvent> mouseExited;

    /**
     * Constructs a group.
     */
    public Button(String svgPath, String color, String name, String help, String eventName) {
        this.svgPath = svgPath;
        this.color = color;
        this.name = name;
        this.help = help;
        this.eventName = eventName;

        rectangle = new Rectangle(0,0,40,40);
        rectangle.setFill(Paint.valueOf(Color.white));

        SVGPath svg = new SVGPath();
        svg.setContent(svgPath);
        svg.setSmooth(true);
        svg.setFill(Paint.valueOf(color));
        svg.setScaleX(1);
        svg.setScaleY(1);
        svg.setLayoutX(7);
        svg.setLayoutY(7);

        mouseEnter = event -> {
            svg.setFill(Paint.valueOf(Color.white));
            rectangle.setFill(Paint.valueOf(Color.cyan));
        };

        mouseExited = event -> {
            svg.setFill(Paint.valueOf(Color.gray_dark));
            rectangle.setFill(Paint.valueOf(Color.white));
        };


        getChildren().addAll(rectangle, svg);
        listen();
    }

    public String getEventName() {
        return eventName;
    }

    public void listen(){
        setOnMouseEntered(mouseEnter);
        setOnMouseExited(mouseExited);
    }

    public void destroy(){
        removeEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnter);
        removeEventHandler(MouseEvent.MOUSE_EXITED, mouseExited);
    }
}
