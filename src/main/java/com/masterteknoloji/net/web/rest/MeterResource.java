package com.masterteknoloji.net.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.net.domain.Meter;

import com.masterteknoloji.net.repository.MeterRepository;
import com.masterteknoloji.net.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.net.web.rest.util.HeaderUtil;
import com.masterteknoloji.net.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Meter.
 */
@RestController
@RequestMapping("/api")
public class MeterResource {

    private final Logger log = LoggerFactory.getLogger(MeterResource.class);

    private static final String ENTITY_NAME = "meter";

    private final MeterRepository meterRepository;

    public MeterResource(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    /**
     * POST  /meters : Create a new meter.
     *
     * @param meter the meter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meter, or with status 400 (Bad Request) if the meter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meters")
    @Timed
    public ResponseEntity<Meter> createMeter(@RequestBody Meter meter) throws URISyntaxException {
        log.debug("REST request to save Meter : {}", meter);
        if (meter.getId() != null) {
            throw new BadRequestAlertException("A new meter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Meter result = meterRepository.save(meter);
        return ResponseEntity.created(new URI("/api/meters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meters : Updates an existing meter.
     *
     * @param meter the meter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meter,
     * or with status 400 (Bad Request) if the meter is not valid,
     * or with status 500 (Internal Server Error) if the meter couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meters")
    @Timed
    public ResponseEntity<Meter> updateMeter(@RequestBody Meter meter) throws URISyntaxException {
        log.debug("REST request to update Meter : {}", meter);
        if (meter.getId() == null) {
            return createMeter(meter);
        }
        Meter result = meterRepository.save(meter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, meter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meters : get all the meters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of meters in body
     */
    @GetMapping("/meters")
    @Timed
    public ResponseEntity<List<Meter>> getAllMeters(Pageable pageable) {
        log.debug("REST request to get a page of Meters");
        Page<Meter> page = meterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /meters/:id : get the "id" meter.
     *
     * @param id the id of the meter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meter, or with status 404 (Not Found)
     */
    @GetMapping("/meters/{id}")
    @Timed
    public ResponseEntity<Meter> getMeter(@PathVariable Long id) {
        log.debug("REST request to get Meter : {}", id);
        Meter meter = meterRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(meter));
    }

    /**
     * DELETE  /meters/:id : delete the "id" meter.
     *
     * @param id the id of the meter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meters/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeter(@PathVariable Long id) {
        log.debug("REST request to delete Meter : {}", id);
        meterRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
