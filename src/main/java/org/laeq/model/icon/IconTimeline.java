package org.laeq.model.icon;

public class IconTimeline extends IconPointColorized {
    protected final int id;

    public IconTimeline(IconSize iconSize, int id) {
        super(iconSize);

        this.id = id;
    }

    public int getIdentifier() {
        return id;
    }
}
