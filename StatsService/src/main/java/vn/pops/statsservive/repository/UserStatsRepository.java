package vn.pops.statsservive.repository;

import vn.pops.statsservive.domain.UserStats;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the UserStats entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserStatsRepository extends MongoRepository<UserStats, String> {

	Optional<UserStats> findOneByUserId(String userId);
}
