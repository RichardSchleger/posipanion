package sk.richardschleger.posipanion.keys;

import java.util.Objects;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TrackPointKey {
    
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int userId;

    @PrimaryKeyColumn(name = "ord", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private int order;

    public TrackPointKey() {
    }

    public TrackPointKey(int userId, int order) {
        this.userId = userId;
        this.order = order;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public TrackPointKey userId(int userId) {
        setUserId(userId);
        return this;
    }

    public TrackPointKey order(int order) {
        setOrder(order);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TrackPointKey)) {
            return false;
        }
        TrackPointKey trackPointKey = (TrackPointKey) o;
        return userId == trackPointKey.userId && order == trackPointKey.order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, order);
    }

    @Override
    public String toString() {
        return "{" +
            " userId='" + getUserId() + "'" +
            ", order='" + getOrder() + "'" +
            "}";
    }

}
