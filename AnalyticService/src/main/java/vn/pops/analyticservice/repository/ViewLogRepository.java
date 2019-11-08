package vn.pops.analyticservice.repository;

import vn.pops.analyticservice.domain.ViewLog;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data Cassandra repository for the ViewLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViewLogRepository extends CassandraRepository<ViewLog, UUID> {

}
