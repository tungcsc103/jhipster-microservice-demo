package vn.pops.analyticservice.web.rest;

import vn.pops.analyticservice.domain.ViewStats;
import vn.pops.analyticservice.service.ViewStatsService;
import vn.pops.analyticservice.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link vn.pops.analyticservice.domain.ViewStats}.
 */
@RestController
@RequestMapping("/api")
public class ViewStatsResource {

    private final Logger log = LoggerFactory.getLogger(ViewStatsResource.class);

    private static final String ENTITY_NAME = "analyticServiceViewStats";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViewStatsService viewStatsService;

    public ViewStatsResource(ViewStatsService viewStatsService) {
        this.viewStatsService = viewStatsService;
    }

    /**
     * {@code POST  /view-stats} : Create a new viewStats.
     *
     * @param viewStats the viewStats to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewStats, or with status {@code 400 (Bad Request)} if the viewStats has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/view-stats")
    public ResponseEntity<ViewStats> createViewStats(@RequestBody ViewStats viewStats) throws URISyntaxException {
        log.debug("REST request to save ViewStats : {}", viewStats);
        if (viewStats.getId() != null) {
            throw new BadRequestAlertException("A new viewStats cannot already have an ID", ENTITY_NAME, "idexists");
        }
        viewStats.setId(UUID.randomUUID());
        ViewStats result = viewStatsService.save(viewStats);
        return ResponseEntity.created(new URI("/api/view-stats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /view-stats} : Updates an existing viewStats.
     *
     * @param viewStats the viewStats to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewStats,
     * or with status {@code 400 (Bad Request)} if the viewStats is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewStats couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/view-stats")
    public ResponseEntity<ViewStats> updateViewStats(@RequestBody ViewStats viewStats) throws URISyntaxException {
        log.debug("REST request to update ViewStats : {}", viewStats);
        if (viewStats.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ViewStats result = viewStatsService.save(viewStats);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, viewStats.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /view-stats} : get all the viewStats.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewStats in body.
     */
    @GetMapping("/view-stats")
    public List<ViewStats> getAllViewStats() {
        log.debug("REST request to get all ViewStats");
        return viewStatsService.findAll();
    }

    /**
     * {@code GET  /view-stats/:id} : get the "id" viewStats.
     *
     * @param id the id of the viewStats to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewStats, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/view-stats/{id}")
    public ResponseEntity<ViewStats> getViewStats(@PathVariable UUID id) {
        log.debug("REST request to get ViewStats : {}", id);
        Optional<ViewStats> viewStats = viewStatsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viewStats);
    }

    /**
     * {@code DELETE  /view-stats/:id} : delete the "id" viewStats.
     *
     * @param id the id of the viewStats to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/view-stats/{id}")
    public ResponseEntity<Void> deleteViewStats(@PathVariable UUID id) {
        log.debug("REST request to delete ViewStats : {}", id);
        viewStatsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
