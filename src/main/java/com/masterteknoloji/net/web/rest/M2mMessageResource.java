package com.masterteknoloji.net.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.net.domain.M2mMessage;

import com.masterteknoloji.net.repository.M2mMessageRepository;
import com.masterteknoloji.net.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.net.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing M2mMessage.
 */
@RestController
@RequestMapping("/api")
public class M2mMessageResource {

    private final Logger log = LoggerFactory.getLogger(M2mMessageResource.class);

    private static final String ENTITY_NAME = "m2mMessage";

    private final M2mMessageRepository m2mMessageRepository;

    public M2mMessageResource(M2mMessageRepository m2mMessageRepository) {
        this.m2mMessageRepository = m2mMessageRepository;
    }

    /**
     * POST  /m-2-m-messages : Create a new m2mMessage.
     *
     * @param m2mMessage the m2mMessage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new m2mMessage, or with status 400 (Bad Request) if the m2mMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-2-m-messages")
    @Timed
    public ResponseEntity<M2mMessage> createM2mMessage(@Valid @RequestBody M2mMessage m2mMessage) throws URISyntaxException {
        log.debug("REST request to save M2mMessage : {}", m2mMessage);
        if (m2mMessage.getId() != null) {
            throw new BadRequestAlertException("A new m2mMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        M2mMessage result = m2mMessageRepository.save(m2mMessage);
        return ResponseEntity.created(new URI("/api/m-2-m-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-2-m-messages : Updates an existing m2mMessage.
     *
     * @param m2mMessage the m2mMessage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated m2mMessage,
     * or with status 400 (Bad Request) if the m2mMessage is not valid,
     * or with status 500 (Internal Server Error) if the m2mMessage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-2-m-messages")
    @Timed
    public ResponseEntity<M2mMessage> updateM2mMessage(@Valid @RequestBody M2mMessage m2mMessage) throws URISyntaxException {
        log.debug("REST request to update M2mMessage : {}", m2mMessage);
        if (m2mMessage.getId() == null) {
            return createM2mMessage(m2mMessage);
        }
        M2mMessage result = m2mMessageRepository.save(m2mMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, m2mMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-2-m-messages : get all the m2mMessages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of m2mMessages in body
     */
    @GetMapping("/m-2-m-messages")
    @Timed
    public List<M2mMessage> getAllM2mMessages() {
        log.debug("REST request to get all M2mMessages");
        return m2mMessageRepository.findAll();
        }

    /**
     * GET  /m-2-m-messages/:id : get the "id" m2mMessage.
     *
     * @param id the id of the m2mMessage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the m2mMessage, or with status 404 (Not Found)
     */
    @GetMapping("/m-2-m-messages/{id}")
    @Timed
    public ResponseEntity<M2mMessage> getM2mMessage(@PathVariable Long id) {
        log.debug("REST request to get M2mMessage : {}", id);
        M2mMessage m2mMessage = m2mMessageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(m2mMessage));
    }

    /**
     * DELETE  /m-2-m-messages/:id : delete the "id" m2mMessage.
     *
     * @param id the id of the m2mMessage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-2-m-messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteM2mMessage(@PathVariable Long id) {
        log.debug("REST request to delete M2mMessage : {}", id);
        m2mMessageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
