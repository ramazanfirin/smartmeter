package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.Sensor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
	
	Sensor findOneByDevEui(String devEui);
	
	Sensor findOneByImei(String imei);

}
