package org.laeq.model.icon;


import org.laeq.model.Category;

public class IconSize extends BaseIcon {
    protected final int size;
    protected final double svgRatio;
    protected final double scale;
    protected final double x;
    protected final double y;

    /**
     * Constructs a group.
     *
     * @param category
     */
    public IconSize(Category category, int size) {
        super(category);
        this.size = size;
        this.svgRatio = 0.5f;
        this.scale = this.svgRatio / (this.svgPath.getLayoutBounds().getHeight() / this.size);
        this.x = this.svgPath.getLayoutBounds().getWidth() / 2;
        this.y = this.svgPath.getLayoutBounds().getWidth() / 2;

        this.svgPath.setLayoutX(- x);
        this.svgPath.setLayoutY(- y);
        this.svgPath.setScaleX(this.scale);
        this.svgPath.setScaleY(this.scale);
    }

    @Override
    public void decorate(){
        getChildren().add(this.svgPath);
    }
}
