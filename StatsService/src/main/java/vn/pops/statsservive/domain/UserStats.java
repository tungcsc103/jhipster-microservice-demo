package vn.pops.statsservive.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A UserStats.
 */
@Document(collection = "user_stats")
public class UserStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user_id")
    private String userId;

    @Field("total_view")
    private Long totalView;

    @Field("total_like")
    private Long totalLike;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public UserStats userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTotalView() {
        return totalView;
    }

    public UserStats totalView(Long totalView) {
        this.totalView = totalView;
        return this;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    public Long getTotalLike() {
        return totalLike;
    }

    public UserStats totalLike(Long totalLike) {
        this.totalLike = totalLike;
        return this;
    }

    public void setTotalLike(Long totalLike) {
        this.totalLike = totalLike;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserStats)) {
            return false;
        }
        return id != null && id.equals(((UserStats) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserStats{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", totalView=" + getTotalView() +
            ", totalLike=" + getTotalLike() +
            "}";
    }
}
