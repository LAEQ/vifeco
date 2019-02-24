package org.laeq.model;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import org.laeq.graphic.Color;

public class IconButton extends Icon {
    private final EventHandler<MouseEvent> mouseEnter;
    private final EventHandler<MouseEvent> mouseExited;

    public IconButton(String path, String color) {
        super(path, color);

        mouseEnter = event -> {
            this.svg.setFill(Paint.valueOf(org.laeq.graphic.Color.cyan));
        };

        mouseExited = event -> {
            this.svg.setFill(Paint.valueOf(org.laeq.graphic.Color.gray_dark));
        };

        Canvas canvas = new Canvas(25, 25);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf(Color.white));
        gc.fillRect(0,0, 25,25);
        canvas.setOpacity(0);

        getChildren().add(0, canvas);

        listen();
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
