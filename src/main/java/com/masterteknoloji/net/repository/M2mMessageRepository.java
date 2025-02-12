package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.M2mMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the M2mMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface M2mMessageRepository extends JpaRepository<M2mMessage, Long> {
	@Query("select m from M2mMessage m where m.sensor.id =:sensorId and m.port =:port and m.imageData=false order by m.insertDate")
	List<M2mMessage> findBySensorId(@Param("sensorId") Long sensorId,@Param("port") Long port);
}
