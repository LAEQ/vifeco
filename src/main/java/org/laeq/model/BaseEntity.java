package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@JsonIgnoreProperties({ "createdAt", "updatedAt" })
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

    @JsonIgnore
    public String getCreatedFormatted(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(getCreatedAt());
    }
    @JsonIgnore
    public String getUpdatedFormatted(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(getCreatedAt());
    }


}
