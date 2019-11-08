package vn.pops.analyticservice.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

/**
 * A ViewStats.
 */
@Table("viewStats")
public class ViewStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    private Long totalView;

    private Long totalLike;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getTotalView() {
        return totalView;
    }

    public ViewStats totalView(Long totalView) {
        this.totalView = totalView;
        return this;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    public Long getTotalLike() {
        return totalLike;
    }

    public ViewStats totalLike(Long totalLike) {
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
        if (!(o instanceof ViewStats)) {
            return false;
        }
        return id != null && id.equals(((ViewStats) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ViewStats{" +
            "id=" + getId() +
            ", totalView=" + getTotalView() +
            ", totalLike=" + getTotalLike() +
            "}";
    }
}
