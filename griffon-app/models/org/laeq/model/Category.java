package org.laeq.model;

public class Category {
    private String name;
    private String icon;
    private String shortcut;

    public Category() {
    }

    public Category(String name, String icon, String shortcut) {
        this.name = name;
        this.icon = icon;
        this.shortcut = shortcut;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getShortcut() {
        return shortcut;
    }
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
}
