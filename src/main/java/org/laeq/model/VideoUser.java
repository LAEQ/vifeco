package org.laeq.model;

import javafx.beans.property.SimpleIntegerProperty;

public class VideoUser {
    private Video video;
    private User user;
    private SimpleIntegerProperty total;

    public VideoUser() {
        this.total = new SimpleIntegerProperty(this, "total", 0);
    }

    public Video getVideo() {
        return video;
    }
    public void setVideo(Video video) {
        this.video = video;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public int getTotal() {
        return total.get();
    }
    public SimpleIntegerProperty totalProperty() {
        return total;
    }
    public void setTotal(int total) {
        this.total.set(total);
    }
}
