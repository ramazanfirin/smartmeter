package com.masterteknoloji.net.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.domain.enumeration.Type;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
import com.masterteknoloji.net.repository.SensorRepository;
import com.masterteknoloji.net.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.net.web.rest.util.HeaderUtil;
import com.masterteknoloji.net.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing LorawanMessage.
 */
@RestController
@RequestMapping("/api")
public class LorawanMessageResource {

    private final Logger log = LoggerFactory.getLogger(LorawanMessageResource.class);

    private static final String ENTITY_NAME = "lorawanMessage";

    private final LorawanMessageRepository lorawanMessageRepository;
    
    private final SensorRepository sensorRepository;
    
    private ObjectMapper objectMapper= new ObjectMapper();

    public LorawanMessageResource(LorawanMessageRepository lorawanMessageRepository,SensorRepository sensorRepository) {
        this.lorawanMessageRepository = lorawanMessageRepository;
        this.sensorRepository = sensorRepository;
    }

    /**
     * POST  /lorawan-messages : Create a new lorawanMessage.
     *
     * @param lorawanMessage the lorawanMessage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lorawanMessage, or with status 400 (Bad Request) if the lorawanMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lorawan-messages")
    @Timed
    public ResponseEntity<LorawanMessage> createLorawanMessage(@RequestBody LorawanMessage lorawanMessage) throws URISyntaxException {
        log.debug("REST request to save LorawanMessage : {}", lorawanMessage);
        if (lorawanMessage.getId() != null) {
            throw new BadRequestAlertException("A new lorawanMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LorawanMessage result = lorawanMessageRepository.save(lorawanMessage);
        return ResponseEntity.created(new URI("/api/lorawan-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lorawan-messages : Updates an existing lorawanMessage.
     *
     * @param lorawanMessage the lorawanMessage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lorawanMessage,
     * or with status 400 (Bad Request) if the lorawanMessage is not valid,
     * or with status 500 (Internal Server Error) if the lorawanMessage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lorawan-messages")
    @Timed
    public ResponseEntity<LorawanMessage> updateLorawanMessage(@RequestBody LorawanMessage lorawanMessage) throws URISyntaxException {
        log.debug("REST request to update LorawanMessage : {}", lorawanMessage);
        if (lorawanMessage.getId() == null) {
            return createLorawanMessage(lorawanMessage);
        }
        LorawanMessage result = lorawanMessageRepository.save(lorawanMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lorawanMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lorawan-messages : get all the lorawanMessages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lorawanMessages in body
     */
    @GetMapping("/lorawan-messages")
    @Timed
    public ResponseEntity<List<LorawanMessage>> getAllLorawanMessages(Pageable pageable) {
        log.debug("REST request to get a page of LorawanMessages");
        Page<LorawanMessage> page = lorawanMessageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lorawan-messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lorawan-messages/:id : get the "id" lorawanMessage.
     *
     * @param id the id of the lorawanMessage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lorawanMessage, or with status 404 (Not Found)
     */
    @GetMapping("/lorawan-messages/{id}")
    @Timed
    public ResponseEntity<LorawanMessage> getLorawanMessage(@PathVariable Long id) {
        log.debug("REST request to get LorawanMessage : {}", id);
        LorawanMessage lorawanMessage = lorawanMessageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lorawanMessage));
    }

    /**
     * DELETE  /lorawan-messages/:id : delete the "id" lorawanMessage.
     *
     * @param id the id of the lorawanMessage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lorawan-messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteLorawanMessage(@PathVariable Long id) {
        log.debug("REST request to delete LorawanMessage : {}", id);
        lorawanMessageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @PostMapping("/lorawan-messages/receive")
    @Timed
    public void receive(@RequestBody String String) throws Exception {
        
    	JsonNode jsonObject = objectMapper.readTree(String);
        Sensor sensor = findSensor(jsonObject);
        
        LorawanMessage lorawanMessage = new LorawanMessage();
        
        String base64Message = jsonObject.get("data").asText();
        String hexMessage = base64ToHex(base64Message);
        lorawanMessage.setBase64Message(base64Message);
        lorawanMessage.setHexMessage(hexMessage);
        lorawanMessage.setInsertDate(ZonedDateTime.now());
        lorawanMessage.sensor(sensor);
        parseSensorSpecificData(sensor, lorawanMessage, jsonObject);
        
        lorawanMessageRepository.save(lorawanMessage);
        sensor.setLastMessage(hexMessage);
        sensor.setLastSeenDate(ZonedDateTime.now());
        sensorRepository.save(sensor);
       
    }
    
    public Sensor findSensor(JsonNode jsonObject) {
    	String devEui = getDeviceEui(jsonObject);
    	Sensor sensor = sensorRepository.findOneByDevEui(devEui.toUpperCase());
		return sensor;
    	
    }
    
    public String getDeviceEui(JsonNode jsonObject) {
    	
    	JsonNode deviceInfo = jsonObject.get("deviceInfo");
    	JsonNode devEui = deviceInfo.get("devEui");
    	
    	return devEui.asText();
    }
    
    public String base64ToHex(String base64Value) {

        byte[] decodedBytes = Base64.getDecoder().decode(base64Value);
        StringBuilder hexString = new StringBuilder();
        for (byte b : decodedBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }
    
    public void parseSensorSpecificData( Sensor sensor,LorawanMessage lorawanMessage,JsonNode jsonObject) {
    	if(sensor.getType().equals(Type.WATER_METER)) {
    		JsonNode object = jsonObject.get("object");
    	}
    }
}
