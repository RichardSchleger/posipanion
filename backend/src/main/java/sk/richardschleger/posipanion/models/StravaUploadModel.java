package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class StravaUploadModel {

    private Long id;

	private String externalId;

	private String error;

	private String	status;

	private Long activityId;

    public StravaUploadModel() {
    }

    public StravaUploadModel(Long id, String externalId, String error, String status, Long activityId) {
        this.id = id;
        this.externalId = externalId;
        this.error = error;
        this.status = status;
        this.activityId = activityId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public StravaUploadModel id(Long id) {
        setId(id);
        return this;
    }

    public StravaUploadModel externalId(String externalId) {
        setExternalId(externalId);
        return this;
    }

    public StravaUploadModel error(String error) {
        setError(error);
        return this;
    }

    public StravaUploadModel status(String status) {
        setStatus(status);
        return this;
    }

    public StravaUploadModel activityId(Long activityId) {
        setActivityId(activityId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StravaUploadModel)) {
            return false;
        }
        StravaUploadModel stravaUploadModel = (StravaUploadModel) o;
        return Objects.equals(id, stravaUploadModel.id) && Objects.equals(externalId, stravaUploadModel.externalId) && Objects.equals(error, stravaUploadModel.error) && Objects.equals(status, stravaUploadModel.status) && Objects.equals(activityId, stravaUploadModel.activityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, error, status, activityId);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", externalId='" + getExternalId() + "'" +
            ", error='" + getError() + "'" +
            ", status='" + getStatus() + "'" +
            ", activityId='" + getActivityId() + "'" +
            "}";
    }

}
