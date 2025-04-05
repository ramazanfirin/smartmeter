package com.masterteknoloji.net.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.net.domain.VibrationProMessage;

import com.masterteknoloji.net.repository.VibrationProMessageRepository;
import com.masterteknoloji.net.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.net.web.rest.util.HeaderUtil;
import com.masterteknoloji.net.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VibrationProMessage.
 */
@RestController
@RequestMapping("/api")
public class VibrationProMessageResource {

    private final Logger log = LoggerFactory.getLogger(VibrationProMessageResource.class);

    private static final String ENTITY_NAME = "vibrationProMessage";

    private final VibrationProMessageRepository vibrationProMessageRepository;

    public VibrationProMessageResource(VibrationProMessageRepository vibrationProMessageRepository) {
        this.vibrationProMessageRepository = vibrationProMessageRepository;
    }

    /**
     * POST  /vibration-pro-messages : Create a new vibrationProMessage.
     *
     * @param vibrationProMessage the vibrationProMessage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vibrationProMessage, or with status 400 (Bad Request) if the vibrationProMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vibration-pro-messages")
    @Timed
    public ResponseEntity<VibrationProMessage> createVibrationProMessage(@RequestBody VibrationProMessage vibrationProMessage) throws URISyntaxException {
        log.debug("REST request to save VibrationProMessage : {}", vibrationProMessage);
        if (vibrationProMessage.getId() != null) {
            throw new BadRequestAlertException("A new vibrationProMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VibrationProMessage result = vibrationProMessageRepository.save(vibrationProMessage);
        return ResponseEntity.created(new URI("/api/vibration-pro-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vibration-pro-messages : Updates an existing vibrationProMessage.
     *
     * @param vibrationProMessage the vibrationProMessage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vibrationProMessage,
     * or with status 400 (Bad Request) if the vibrationProMessage is not valid,
     * or with status 500 (Internal Server Error) if the vibrationProMessage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vibration-pro-messages")
    @Timed
    public ResponseEntity<VibrationProMessage> updateVibrationProMessage(@RequestBody VibrationProMessage vibrationProMessage) throws URISyntaxException {
        log.debug("REST request to update VibrationProMessage : {}", vibrationProMessage);
        if (vibrationProMessage.getId() == null) {
            return createVibrationProMessage(vibrationProMessage);
        }
        VibrationProMessage result = vibrationProMessageRepository.save(vibrationProMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vibrationProMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vibration-pro-messages : get all the vibrationProMessages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of vibrationProMessages in body
     */
    @GetMapping("/vibration-pro-messages")
    @Timed
    public ResponseEntity<List<VibrationProMessage>> getAllVibrationProMessages(Pageable pageable) {
        log.debug("REST request to get a page of VibrationProMessages");
        Page<VibrationProMessage> page = vibrationProMessageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vibration-pro-messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vibration-pro-messages/:id : get the "id" vibrationProMessage.
     *
     * @param id the id of the vibrationProMessage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vibrationProMessage, or with status 404 (Not Found)
     */
    @GetMapping("/vibration-pro-messages/{id}")
    @Timed
    public ResponseEntity<VibrationProMessage> getVibrationProMessage(@PathVariable Long id) {
        log.debug("REST request to get VibrationProMessage : {}", id);
        VibrationProMessage vibrationProMessage = vibrationProMessageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vibrationProMessage));
    }

    /**
     * DELETE  /vibration-pro-messages/:id : delete the "id" vibrationProMessage.
     *
     * @param id the id of the vibrationProMessage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vibration-pro-messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteVibrationProMessage(@PathVariable Long id) {
        log.debug("REST request to delete VibrationProMessage : {}", id);
        vibrationProMessageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /vibration-pro-messages/search : search vibrationProMessages.
     *
     * @param sensorId the sensor ID to filter by
     * @param startDate the start date to filter by
     * @param endDate the end date to filter by
     * @return the ResponseEntity with status 200 (OK) and the list of vibrationProMessages in body
     */
    @GetMapping("/vibration-pro-messages/search")
    @Timed
    public ResponseEntity<List<VibrationProMessage>> searchVibrationProMessages(
        @RequestParam(required = false) Long sensorId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        log.debug("REST request to search VibrationProMessages with sensorId: {}, startDate: {}, endDate: {}", 
            sensorId, startDate, endDate);
        List<VibrationProMessage> result = vibrationProMessageRepository.search(sensorId, startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
