package com.makhabatusen.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseModel {

    protected int id;

    public int getId() {
        return id;
    }

    protected String getFormattedTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
