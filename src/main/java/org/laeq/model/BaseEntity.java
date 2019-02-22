package org.laeq.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

abstract class BaseEntity {
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

    public String getCreatedFormatted(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(getCreatedAt());
    }public String getUpdatedFormatted(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(getCreatedAt());
    }


}
