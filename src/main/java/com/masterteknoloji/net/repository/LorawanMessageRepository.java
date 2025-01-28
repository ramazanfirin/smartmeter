package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.domain.Sensor;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the LorawanMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LorawanMessageRepository extends JpaRepository<LorawanMessage, Long> {

	@Query("select lorawanMessage from LorawanMessage lorawanMessage where lorawanMessage.fPort='3' and lorawanMessage.sensor.id = :sensorId and "
			+ "lorawanMessage.fCnt>=:startFcnt and lorawanMessage.fCnt <:endFcnt order by lorawanMessage.fCnt")
	List<LorawanMessage> findByFcnt(@Param("sensorId") Long sensorId,@Param("startFcnt") Long startFcnt,@Param("endFcnt") Long endFcnt);
}
