package vn.pops.analyticservice.service;

import vn.pops.analyticservice.domain.ViewStats;
import vn.pops.analyticservice.repository.ViewStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link ViewStats}.
 */
@Service
public class ViewStatsService {

    private final Logger log = LoggerFactory.getLogger(ViewStatsService.class);

    private final ViewStatsRepository viewStatsRepository;

    public ViewStatsService(ViewStatsRepository viewStatsRepository) {
        this.viewStatsRepository = viewStatsRepository;
    }

    /**
     * Save a viewStats.
     *
     * @param viewStats the entity to save.
     * @return the persisted entity.
     */
    public ViewStats save(ViewStats viewStats) {
        log.debug("Request to save ViewStats : {}", viewStats);
        return viewStatsRepository.save(viewStats);
    }

    /**
     * Get all the viewStats.
     *
     * @return the list of entities.
     */
    public List<ViewStats> findAll() {
        log.debug("Request to get all ViewStats");
        return viewStatsRepository.findAll();
    }


    /**
     * Get one viewStats by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ViewStats> findOne(UUID id) {
        log.debug("Request to get ViewStats : {}", id);
        return viewStatsRepository.findById(id);
    }

    /**
     * Delete the viewStats by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ViewStats : {}", id);
        viewStatsRepository.deleteById(id);
    }
}
