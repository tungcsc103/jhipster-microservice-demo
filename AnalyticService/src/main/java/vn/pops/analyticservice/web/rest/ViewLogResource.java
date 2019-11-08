package vn.pops.analyticservice.web.rest;

import vn.pops.analyticservice.domain.ViewLog;
import vn.pops.analyticservice.service.ViewLogService;
import vn.pops.analyticservice.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link vn.pops.analyticservice.domain.ViewLog}.
 */
@RestController
@RequestMapping("/api")
public class ViewLogResource {

    private final Logger log = LoggerFactory.getLogger(ViewLogResource.class);

    private static final String ENTITY_NAME = "analyticServiceViewLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViewLogService viewLogService;

    public ViewLogResource(ViewLogService viewLogService) {
        this.viewLogService = viewLogService;
    }

    /**
     * {@code POST  /view-logs} : Create a new viewLog.
     *
     * @param viewLog the viewLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewLog, or with status {@code 400 (Bad Request)} if the viewLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/view-logs")
    public ResponseEntity<ViewLog> createViewLog(@Valid @RequestBody ViewLog viewLog) throws URISyntaxException {
        log.debug("REST request to save ViewLog : {}", viewLog);
        if (viewLog.getId() != null) {
            throw new BadRequestAlertException("A new viewLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        viewLog.setId(UUID.randomUUID());
        ViewLog result = viewLogService.save(viewLog);
        return ResponseEntity.created(new URI("/api/view-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code GET  /view-logs} : get all the viewLogs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewLogs in body.
     */
    @GetMapping("/view-logs")
    public List<ViewLog> getAllViewLogs() {
        log.debug("REST request to get all ViewLogs");
        return viewLogService.findAll();
    }

    /**
     * {@code GET  /view-logs/:id} : get the "id" viewLog.
     *
     * @param id the id of the viewLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/view-logs/{id}")
    public ResponseEntity<ViewLog> getViewLog(@PathVariable UUID id) {
        log.debug("REST request to get ViewLog : {}", id);
        Optional<ViewLog> viewLog = viewLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viewLog);
    }

}
