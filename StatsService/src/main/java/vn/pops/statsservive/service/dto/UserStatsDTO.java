package vn.pops.statsservive.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link vn.pops.statsservive.domain.UserStats} entity.
 */
public class UserStatsDTO implements Serializable {

    private String id;

    @NotNull
    private String userId;

    private Long totalView;

    private Long totalLike;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTotalView() {
        return totalView;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    public Long getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Long totalLike) {
        this.totalLike = totalLike;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserStatsDTO userStatsDTO = (UserStatsDTO) o;
        if (userStatsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userStatsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserStatsDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", totalView=" + getTotalView() +
            ", totalLike=" + getTotalLike() +
            "}";
    }
}
