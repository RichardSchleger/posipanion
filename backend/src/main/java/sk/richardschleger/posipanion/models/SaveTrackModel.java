package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class SaveTrackModel {
    
    private String name;

    private long estimatedMovingTime;

    private long stravaId;

    public SaveTrackModel() {
    }

    public SaveTrackModel(String name, long estimatedMovingTime, long stravaId) {
        this.name = name;
        this.estimatedMovingTime = estimatedMovingTime;
        this.stravaId = stravaId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEstimatedMovingTime() {
        return this.estimatedMovingTime;
    }

    public void setEstimatedMovingTime(long estimatedMovingTime) {
        this.estimatedMovingTime = estimatedMovingTime;
    }

    public long getStravaId() {
        return this.stravaId;
    }

    public void setStravaId(long stravaId) {
        this.stravaId = stravaId;
    }

    public SaveTrackModel name(String name) {
        setName(name);
        return this;
    }

    public SaveTrackModel estimatedMovingTime(long estimatedMovingTime) {
        setEstimatedMovingTime(estimatedMovingTime);
        return this;
    }

    public SaveTrackModel stravaId(long stravaId) {
        setStravaId(stravaId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SaveTrackModel)) {
            return false;
        }
        SaveTrackModel saveTrackModel = (SaveTrackModel) o;
        return Objects.equals(name, saveTrackModel.name) && estimatedMovingTime == saveTrackModel.estimatedMovingTime && stravaId == saveTrackModel.stravaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, estimatedMovingTime, stravaId);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", estimatedMovingTime='" + getEstimatedMovingTime() + "'" +
            ", stravaId='" + getStravaId() + "'" +
            "}";
    }

}
