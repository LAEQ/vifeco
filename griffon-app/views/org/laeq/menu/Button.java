package org.laeq.menu;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.laeq.graphic.Color;

public class Button extends Group {
    private String svgPath;
    private String color;

    private String name;
    private String help;
    private String eventName;

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

        Canvas canvas = new Canvas(40, 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(javafx.scene.paint.Color.valueOf(Color.white));
        gc.fillRect(0,0,40,40);
        canvas.setOpacity(1);

        SVGPath svg = new SVGPath();
        svg.setContent(svgPath);
        svg.setSmooth(true);
        svg.setFill(Paint.valueOf(color));
        svg.setScaleX(1);
        svg.setScaleY(1);
        svg.setLayoutX(7);
        svg.setLayoutY(7);

        mouseEnter = event -> {
            svg.setFill(Paint.valueOf(org.laeq.graphic.Color.cyan));
        };

        mouseExited = event -> {
            svg.setFill(Paint.valueOf(org.laeq.graphic.Color.gray_dark));
        };


        getChildren().addAll(canvas, svg);
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
