
package com.makhabatusen.models;

import com.sun.istack.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class Guest extends BaseModel {

    private final int rentHours;
    private final LocalDateTime startTime;
    private final LocalDateTime endOfOccupyingTime;
    private final int groupID;

    public int getGroupId() {
        return this.groupID;
    }

    public int getRentHours() {
        return rentHours;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndOfOccupyingTime() {
        return this.endOfOccupyingTime;
    }

    private Guest(@NotNull Builder builder) {
        this.id = builder.id;
        this.groupID = builder.groupId;
        this.rentHours = builder.rentHours;
        this.startTime = builder.startTime;
        this.endOfOccupyingTime = builder.endTime;
    }

    @Override
    public String toString() {

        return "Guest: " +
                "id=" + id +
                ", groupId=" + groupID +
                ", rentHours=" + rentHours +
                ", StartOfOccupyingTime=" + getFormattedTime(startTime) +
                ", endOfOccupyingTime=" + getFormattedTime(this.endOfOccupyingTime);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guest)) return false;
        Guest guest = (Guest) o;
        return id == guest.id && getGroupId() == guest.getGroupId() && rentHours == guest.rentHours
                && Objects.equals(getStartTime(), guest.getStartTime()) && Objects.equals(getEndOfOccupyingTime(),
                guest.getEndOfOccupyingTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getGroupId(), rentHours, getStartTime(), getEndOfOccupyingTime());
    }

    public static class Builder {
        private int id;
        private int groupId;
        private int rentHours;
        private LocalDateTime startTime;
        private LocalDateTime endTime;


        public Guest build() {
            return new Guest(this);
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder groupId(int groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder rentHours(int rentHours) {
            this.rentHours = rentHours;
            return this;
        }

        public Builder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public static Builder builder() {
            return new Builder();
        }

    }
}
