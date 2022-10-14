package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.validator.constraints.Length;
import org.laeq.editor.DrawingType;
import org.laeq.model.converter.hibernate.Point2DConverter;
import org.laeq.model.converter.jackson.Point2DConverterDeserialize;
import org.laeq.model.converter.jackson.Point2DConverterSerialize;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(name = "drawing")
@JsonIgnoreProperties({"canvas", "isVisible"})
public class Drawing {
    @Id
    @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DrawingType type;

    @Column(nullable = false)
    @Length(min = 7, max = 7)
    @Pattern(regexp = "^#[0-9A-F]{6}$")
    private String color;

    @Column(nullable = false)
    @JsonSerialize(converter = Point2DConverterSerialize.class)
    @JsonDeserialize(converter = Point2DConverterDeserialize.class)
    @Convert(converter = Point2DConverter.class)
    private Point2D start;

    @Column(nullable = false)
    @JsonSerialize(converter = Point2DConverterSerialize.class)
    @JsonDeserialize(converter = Point2DConverterDeserialize.class)
    @Convert(converter = Point2DConverter.class)
    private Point2D end;

    @Transient
    private SimpleBooleanProperty active = new SimpleBooleanProperty(false);

    public Drawing() {
        super();
    }

    public Drawing(DrawingType type, String color, Point2D start, Point2D end) {
        this.type = type;
        this.color = color;
        this.start = start;
        this.end = end;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DrawingType getType() {
        return type;
    }

    public void setType(DrawingType type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public Point2D getStart() {
        return start;
    }

    public void setStart(Point2D start) {
        this.start = start;
    }

    public Point2D getEnd() {
        return end;
    }

    public void setEnd(Point2D end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drawing drawing = (Drawing) o;
        return id.equals(drawing.id) && type.equals(drawing.type) && color.equals(drawing.color)
                && start.equals(drawing.start) && end.equals(drawing.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, color, start, end);
    }

    public Canvas getCanvas(double width, double height, double dash, double dashOffset) {
        if(type == DrawingType.LINE){
            return getLineCanvas(width,height,dash,dashOffset);
        } else if(type == DrawingType.RECTANGLE){
            return getRectangleCanvas(width,height,dash,dashOffset);
        }

        throw new NotImplementedException("Draw type is not implemented");
    }

    public Canvas getLineCanvas(double width, double height, double dash, double dashOffset) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf(color));
        gc.setStroke(Color.valueOf(color));
        gc.setLineWidth(2);

        gc.setLineDashes(dash);
        gc.setLineDashOffset(dashOffset);

        gc.strokeLine(
                start.getX() * width,
                start.getY() * height,
                end.getX() * width,
                end.getY() * height);

        return canvas;
    }

    public Canvas getRectangleCanvas(double width, double height, double dash, double dashOffset) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.valueOf(color));
        gc.setLineWidth(2);

        gc.setLineDashes(dash);
        gc.setLineDashOffset(dashOffset);

        gc.strokeRoundRect(start.getX() * width, start.getY() * height,
                (end.getX() - start.getX()) * width, (end.getY() - start.getY()) * height,
                0, 0);

        return canvas;
    }

    public SimpleBooleanProperty activeProperty() {
        return active ;
    }

    public final boolean isActive() {
        return activeProperty().get();
    }

    public final void setActive(boolean active) {
        activeProperty().set(active);
    }
}
