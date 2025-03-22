package com.masterteknoloji.net.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.net.domain.VibrationEcoMessage;

import com.masterteknoloji.net.repository.VibrationEcoMessageRepository;
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
 * REST controller for managing VibrationEcoMessage.
 */
@RestController
@RequestMapping("/api")
public class VibrationEcoMessageResource {

    private final Logger log = LoggerFactory.getLogger(VibrationEcoMessageResource.class);

    private static final String ENTITY_NAME = "vibrationEcoMessage";

    private final VibrationEcoMessageRepository vibrationEcoMessageRepository;

    public VibrationEcoMessageResource(VibrationEcoMessageRepository vibrationEcoMessageRepository) {
        this.vibrationEcoMessageRepository = vibrationEcoMessageRepository;
    }

    /**
     * POST  /vibration-eco-messages : Create a new vibrationEcoMessage.
     *
     * @param vibrationEcoMessage the vibrationEcoMessage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vibrationEcoMessage, or with status 400 (Bad Request) if the vibrationEcoMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vibration-eco-messages")
    @Timed
    public ResponseEntity<VibrationEcoMessage> createVibrationEcoMessage(@Valid @RequestBody VibrationEcoMessage vibrationEcoMessage) throws URISyntaxException {
        log.debug("REST request to save VibrationEcoMessage : {}", vibrationEcoMessage);
        if (vibrationEcoMessage.getId() != null) {
            throw new BadRequestAlertException("A new vibrationEcoMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VibrationEcoMessage result = vibrationEcoMessageRepository.save(vibrationEcoMessage);
        return ResponseEntity.created(new URI("/api/vibration-eco-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vibration-eco-messages : Updates an existing vibrationEcoMessage.
     *
     * @param vibrationEcoMessage the vibrationEcoMessage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vibrationEcoMessage,
     * or with status 400 (Bad Request) if the vibrationEcoMessage is not valid,
     * or with status 500 (Internal Server Error) if the vibrationEcoMessage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vibration-eco-messages")
    @Timed
    public ResponseEntity<VibrationEcoMessage> updateVibrationEcoMessage(@Valid @RequestBody VibrationEcoMessage vibrationEcoMessage) throws URISyntaxException {
        log.debug("REST request to update VibrationEcoMessage : {}", vibrationEcoMessage);
        if (vibrationEcoMessage.getId() == null) {
            return createVibrationEcoMessage(vibrationEcoMessage);
        }
        VibrationEcoMessage result = vibrationEcoMessageRepository.save(vibrationEcoMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vibrationEcoMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vibration-eco-messages : get all the vibrationEcoMessages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of vibrationEcoMessages in body
     */
    @GetMapping("/vibration-eco-messages")
    @Timed
    public ResponseEntity<List<VibrationEcoMessage>> getAllVibrationEcoMessages(Pageable pageable) {
        log.debug("REST request to get a page of VibrationEcoMessages");
        Page<VibrationEcoMessage> page = vibrationEcoMessageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vibration-eco-messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vibration-eco-messages/:id : get the "id" vibrationEcoMessage.
     *
     * @param id the id of the vibrationEcoMessage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vibrationEcoMessage, or with status 404 (Not Found)
     */
    @GetMapping("/vibration-eco-messages/{id}")
    @Timed
    public ResponseEntity<VibrationEcoMessage> getVibrationEcoMessage(@PathVariable Long id) {
        log.debug("REST request to get VibrationEcoMessage : {}", id);
        VibrationEcoMessage vibrationEcoMessage = vibrationEcoMessageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vibrationEcoMessage));
    }

    /**
     * DELETE  /vibration-eco-messages/:id : delete the "id" vibrationEcoMessage.
     *
     * @param id the id of the vibrationEcoMessage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vibration-eco-messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteVibrationEcoMessage(@PathVariable Long id) {
        log.debug("REST request to delete VibrationEcoMessage : {}", id);
        vibrationEcoMessageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
