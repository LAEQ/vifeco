package org.laeq.model;

import javax.swing.text.html.ImageView;

@griffon.transform.ResourcesAware
public class Icon {
    private String iconPath;
    private ImageView imageView;

    public Icon(String iconPath) {
        this.iconPath = iconPath;

    }
}
