package com.masterteknoloji.net.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.net.domain.CurrentMeterMessage;

import com.masterteknoloji.net.repository.CurrentMeterMessageRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CurrentMeterMessage.
 */
@RestController
@RequestMapping("/api")
public class CurrentMeterMessageResource {

    private final Logger log = LoggerFactory.getLogger(CurrentMeterMessageResource.class);

    private static final String ENTITY_NAME = "currentMeterMessage";

    private final CurrentMeterMessageRepository currentMeterMessageRepository;

    public CurrentMeterMessageResource(CurrentMeterMessageRepository currentMeterMessageRepository) {
        this.currentMeterMessageRepository = currentMeterMessageRepository;
    }

    /**
     * POST  /current-meter-messages : Create a new currentMeterMessage.
     *
     * @param currentMeterMessage the currentMeterMessage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new currentMeterMessage, or with status 400 (Bad Request) if the currentMeterMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/current-meter-messages")
    @Timed
    public ResponseEntity<CurrentMeterMessage> createCurrentMeterMessage(@Valid @RequestBody CurrentMeterMessage currentMeterMessage) throws URISyntaxException {
        log.debug("REST request to save CurrentMeterMessage : {}", currentMeterMessage);
        if (currentMeterMessage.getId() != null) {
            throw new BadRequestAlertException("A new currentMeterMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrentMeterMessage result = currentMeterMessageRepository.save(currentMeterMessage);
        return ResponseEntity.created(new URI("/api/current-meter-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /current-meter-messages : Updates an existing currentMeterMessage.
     *
     * @param currentMeterMessage the currentMeterMessage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated currentMeterMessage,
     * or with status 400 (Bad Request) if the currentMeterMessage is not valid,
     * or with status 500 (Internal Server Error) if the currentMeterMessage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/current-meter-messages")
    @Timed
    public ResponseEntity<CurrentMeterMessage> updateCurrentMeterMessage(@Valid @RequestBody CurrentMeterMessage currentMeterMessage) throws URISyntaxException {
        log.debug("REST request to update CurrentMeterMessage : {}", currentMeterMessage);
        if (currentMeterMessage.getId() == null) {
            return createCurrentMeterMessage(currentMeterMessage);
        }
        CurrentMeterMessage result = currentMeterMessageRepository.save(currentMeterMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, currentMeterMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /current-meter-messages : get all the currentMeterMessages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of currentMeterMessages in body
     */
    @GetMapping("/current-meter-messages")
    @Timed
    public ResponseEntity<List<CurrentMeterMessage>> getAllCurrentMeterMessages(Pageable pageable) {
        log.debug("REST request to get a page of CurrentMeterMessages");
        Page<CurrentMeterMessage> page = currentMeterMessageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/current-meter-messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /current-meter-messages/:id : get the "id" currentMeterMessage.
     *
     * @param id the id of the currentMeterMessage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the currentMeterMessage, or with status 404 (Not Found)
     */
    @GetMapping("/current-meter-messages/{id}")
    @Timed
    public ResponseEntity<CurrentMeterMessage> getCurrentMeterMessage(@PathVariable Long id) {
        log.debug("REST request to get CurrentMeterMessage : {}", id);
        CurrentMeterMessage currentMeterMessage = currentMeterMessageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(currentMeterMessage));
    }

    /**
     * DELETE  /current-meter-messages/:id : delete the "id" currentMeterMessage.
     *
     * @param id the id of the currentMeterMessage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/current-meter-messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteCurrentMeterMessage(@PathVariable Long id) {
        log.debug("REST request to delete CurrentMeterMessage : {}", id);
        currentMeterMessageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
