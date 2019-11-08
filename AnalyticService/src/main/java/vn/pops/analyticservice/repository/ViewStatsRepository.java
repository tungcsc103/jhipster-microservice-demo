package vn.pops.analyticservice.repository;

import vn.pops.analyticservice.domain.ViewStats;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data Cassandra repository for the ViewStats entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViewStatsRepository extends CassandraRepository<ViewStats, UUID> {

}
