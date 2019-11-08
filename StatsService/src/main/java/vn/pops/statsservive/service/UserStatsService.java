package vn.pops.statsservive.service;

import vn.pops.statsservive.domain.UserStats;
import vn.pops.statsservive.repository.UserStatsRepository;
import vn.pops.statsservive.service.dto.UserStatsDTO;
import vn.pops.statsservive.service.mapper.UserStatsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserStats}.
 */
@Service
public class UserStatsService {

    private final Logger log = LoggerFactory.getLogger(UserStatsService.class);

    private final UserStatsRepository userStatsRepository;

    private final UserStatsMapper userStatsMapper;

    public UserStatsService(UserStatsRepository userStatsRepository, UserStatsMapper userStatsMapper) {
        this.userStatsRepository = userStatsRepository;
        this.userStatsMapper = userStatsMapper;
    }

    /**
     * Save a userStats.
     *
     * @param userStatsDTO the entity to save.
     * @return the persisted entity.
     */
    public UserStatsDTO save(UserStatsDTO userStatsDTO) {
        log.debug("Request to save UserStats : {}", userStatsDTO);
        UserStats userStats = userStatsMapper.toEntity(userStatsDTO);
        userStats = userStatsRepository.save(userStats);
        return userStatsMapper.toDto(userStats);
    }

    /**
     * Get all the userStats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<UserStatsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserStats");
        return userStatsRepository.findAll(pageable)
            .map(userStatsMapper::toDto);
    }


    /**
     * Get one userStats by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UserStatsDTO> findOne(String id) {
        log.debug("Request to get UserStats : {}", id);
        return userStatsRepository.findById(id)
            .map(userStatsMapper::toDto);
    }
    
    public Optional<UserStatsDTO> findOneByUserId(String id) {
        log.debug("Request to get UserStats userid : {}", id);
        return userStatsRepository.findOneByUserId(id)
            .map(userStatsMapper::toDto);
    }
    
    

    /**
     * Delete the userStats by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete UserStats : {}", id);
        userStatsRepository.deleteById(id);
    }
}
