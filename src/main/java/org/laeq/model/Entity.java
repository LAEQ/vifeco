package org.laeq.model;

import java.sql.Timestamp;

abstract class Entity {
    protected Timestamp createdAt;
    protected Timestamp updatedAt;

    public abstract int getId();
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
