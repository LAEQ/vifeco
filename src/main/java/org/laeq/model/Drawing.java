package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
@JsonIgnoreProperties({""})
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
        return id.equals(drawing.id) && type.equals(drawing.type) && color.equals(drawing.color) && start.equals(drawing.start) && end.equals(drawing.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, color, start, end);
    }

    public void draw(GraphicsContext gc) {
        double x = start.getX();
        double y = start.getY();
        double x2 = end.getX();
        double y2 = end.getY();

        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeLine(x, y, x2, y2);
    }
}
