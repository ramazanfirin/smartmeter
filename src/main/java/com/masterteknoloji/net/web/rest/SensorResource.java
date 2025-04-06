package com.masterteknoloji.net.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.net.domain.Sensor;

import com.masterteknoloji.net.repository.SensorRepository;
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
 * REST controller for managing Sensor.
 */
@RestController
@RequestMapping("/api")
public class SensorResource {

    private final Logger log = LoggerFactory.getLogger(SensorResource.class);

    private static final String ENTITY_NAME = "sensor";

    private final SensorRepository sensorRepository;

    public SensorResource(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * POST  /sensors : Create a new sensor.
     *
     * @param sensor the sensor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sensor, or with status 400 (Bad Request) if the sensor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sensors")
    @Timed
    public ResponseEntity<Sensor> createSensor(@Valid @RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensor);
        if (sensor.getId() != null) {
            throw new BadRequestAlertException("A new sensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sensor.setImei(sensor.getDevEui());
        Sensor result = sensorRepository.save(sensor);
        return ResponseEntity.created(new URI("/api/sensors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sensors : Updates an existing sensor.
     *
     * @param sensor the sensor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sensor,
     * or with status 400 (Bad Request) if the sensor is not valid,
     * or with status 500 (Internal Server Error) if the sensor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sensors")
    @Timed
    public ResponseEntity<Sensor> updateSensor(@Valid @RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to update Sensor : {}", sensor);
        if (sensor.getId() == null) {
            return createSensor(sensor);
        }
        Sensor result = sensorRepository.save(sensor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sensor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sensors : get all the sensors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sensors in body
     */
    @GetMapping("/sensors")
    @Timed
    public ResponseEntity<List<Sensor>> getAllSensors(
            @RequestParam(required = false) String type,
            Pageable pageable) {
        log.debug("REST request to get a page of Sensors with type: {}", type);
        Page<Sensor> page;
        if (type != null) {
            page = sensorRepository.findByType( com.masterteknoloji.net.domain.enumeration.Type.valueOf(type), pageable);
        } else {
            page = sensorRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sensors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sensors/:id : get the "id" sensor.
     *
     * @param id the id of the sensor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sensor, or with status 404 (Not Found)
     */
    @GetMapping("/sensors/{id}")
    @Timed
    public ResponseEntity<Sensor> getSensor(@PathVariable Long id) {
        log.debug("REST request to get Sensor : {}", id);
        Sensor sensor = sensorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sensor));
    }

    /**
     * DELETE  /sensors/:id : delete the "id" sensor.
     *
     * @param id the id of the sensor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sensors/{id}")
    @Timed
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
