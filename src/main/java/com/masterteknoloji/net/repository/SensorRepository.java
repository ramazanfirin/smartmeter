package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.Sensor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Spring Data JPA repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
	
	Sensor findOneByDevEui(String devEui);
	
	Sensor findOneByImei(String imei);
	
	Page<Sensor> findByType( com.masterteknoloji.net.domain.enumeration.Type type, Pageable pageable);

}
