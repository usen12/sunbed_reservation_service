package com.makhabatusen.models;

import com.sun.istack.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class Sunbed extends BaseModel {

    private boolean isOccupied;
    private int groupID;
    private LocalDateTime endOfOccupyingTime;

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public LocalDateTime getEndOfOccupyingTime() {
        return endOfOccupyingTime;
    }

    public void setEndOfOccupyingTime(LocalDateTime endOfOccupyingTime) {
        this.endOfOccupyingTime = endOfOccupyingTime;
    }

    public Sunbed(@NotNull Builder builder) {
        this.id = builder.id;
        this.isOccupied = builder.isOccupied;
        this.groupID = builder.groupID;
        this.endOfOccupyingTime = builder.endOfOccupyingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sunbed)) return false;
        Sunbed sunbed = (Sunbed) o;
        return getId() == sunbed.getId() && isOccupied() == sunbed.isOccupied() &&
                getGroupID() == sunbed.getGroupID() &&
                Objects.equals(getEndOfOccupyingTime(), sunbed.getEndOfOccupyingTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), isOccupied(), getGroupID(), getEndOfOccupyingTime());
    }

    public static class Builder {
        private int id;
        private boolean isOccupied;
        private int groupID;
        private LocalDateTime endOfOccupyingTime;


        public Sunbed build() {
            return new Sunbed(this);
        }


        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder isOccupied(boolean isOccupied) {
            this.isOccupied = isOccupied;
            return this;
        }



        public static Builder builder() {
            return new Builder();
        }
    }

    @Override
    public String toString() {

        return "Sunbed: " +
                "id=" + id +
                ", isOccupied=" + isOccupied +
                ", groupID=" + groupID +
                ", endOfOccupyingTime=" + getFormattedTime(endOfOccupyingTime);
    }
}
