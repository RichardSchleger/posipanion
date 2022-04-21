package sk.richardschleger.posipanion.models;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class StravaRouteModel {
    
	private Integer id;

	private String name;

	private String description;

	private String athlete;

	private Float distance;

	private Float elevationGain;

	private int type;

	private int subType;

	@SerializedName("private")
	private Boolean isPrivate;

	private Boolean starred;

	private Long timestamp;

	private Integer estimatedMovingTime;


	public StravaRouteModel() {
	}

	public StravaRouteModel(Integer id, String name, String description, String athlete, Float distance, Float elevationGain, int type, int subType, Boolean isPrivate, Boolean starred, Long timestamp, Integer estimatedMovingTime) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.athlete = athlete;
		this.distance = distance;
		this.elevationGain = elevationGain;
		this.type = type;
		this.subType = subType;
		this.isPrivate = isPrivate;
		this.starred = starred;
		this.timestamp = timestamp;
		this.estimatedMovingTime = estimatedMovingTime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAthlete() {
		return this.athlete;
	}

	public void setAthlete(String athlete) {
		this.athlete = athlete;
	}

	public Float getDistance() {
		return this.distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Float getElevationGain() {
		return this.elevationGain;
	}

	public void setElevationGain(Float elevationGain) {
		this.elevationGain = elevationGain;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSubType() {
		return this.subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public Boolean isIsPrivate() {
		return this.isPrivate;
	}

	public Boolean getIsPrivate() {
		return this.isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public Boolean isStarred() {
		return this.starred;
	}

	public Boolean getStarred() {
		return this.starred;
	}

	public void setStarred(Boolean starred) {
		this.starred = starred;
	}

	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getEstimatedMovingTime() {
		return this.estimatedMovingTime;
	}

	public void setEstimatedMovingTime(Integer estimatedMovingTime) {
		this.estimatedMovingTime = estimatedMovingTime;
	}

	public StravaRouteModel id(Integer id) {
		setId(id);
		return this;
	}

	public StravaRouteModel name(String name) {
		setName(name);
		return this;
	}

	public StravaRouteModel description(String description) {
		setDescription(description);
		return this;
	}

	public StravaRouteModel athlete(String athlete) {
		setAthlete(athlete);
		return this;
	}

	public StravaRouteModel distance(Float distance) {
		setDistance(distance);
		return this;
	}

	public StravaRouteModel elevationGain(Float elevationGain) {
		setElevationGain(elevationGain);
		return this;
	}

	public StravaRouteModel type(int type) {
		setType(type);
		return this;
	}

	public StravaRouteModel subType(int subType) {
		setSubType(subType);
		return this;
	}

	public StravaRouteModel isPrivate(Boolean isPrivate) {
		setIsPrivate(isPrivate);
		return this;
	}

	public StravaRouteModel starred(Boolean starred) {
		setStarred(starred);
		return this;
	}

	public StravaRouteModel timestamp(Long timestamp) {
		setTimestamp(timestamp);
		return this;
	}

	public StravaRouteModel estimatedMovingTime(Integer estimatedMovingTime) {
		setEstimatedMovingTime(estimatedMovingTime);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof StravaRouteModel)) {
			return false;
		}
		StravaRouteModel stravaRouteModel = (StravaRouteModel) o;
		return Objects.equals(id, stravaRouteModel.id) && Objects.equals(name, stravaRouteModel.name) && Objects.equals(description, stravaRouteModel.description) && Objects.equals(athlete, stravaRouteModel.athlete) && Objects.equals(distance, stravaRouteModel.distance) && Objects.equals(elevationGain, stravaRouteModel.elevationGain) && type == stravaRouteModel.type && subType == stravaRouteModel.subType && Objects.equals(isPrivate, stravaRouteModel.isPrivate) && Objects.equals(starred, stravaRouteModel.starred) && Objects.equals(timestamp, stravaRouteModel.timestamp) && Objects.equals(estimatedMovingTime, stravaRouteModel.estimatedMovingTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, athlete, distance, elevationGain, type, subType, isPrivate, starred, timestamp, estimatedMovingTime);
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", name='" + getName() + "'" +
			", description='" + getDescription() + "'" +
			", athlete='" + getAthlete() + "'" +
			", distance='" + getDistance() + "'" +
			", elevationGain='" + getElevationGain() + "'" +
			", type='" + getType() + "'" +
			", subType='" + getSubType() + "'" +
			", isPrivate='" + isIsPrivate() + "'" +
			", starred='" + isStarred() + "'" +
			", timestamp='" + getTimestamp() + "'" +
			", estimatedMovingTime='" + getEstimatedMovingTime() + "'" +
			"}";
	}


}
