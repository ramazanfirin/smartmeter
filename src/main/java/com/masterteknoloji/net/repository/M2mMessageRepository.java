package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.M2mMessage;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Query("select m from M2mMessage m where m.ip =:ip and m.port =:port and m.imageData=false and m.validImage=false and m.insertDate>:insertDate order by m.insertDate")
	List<M2mMessage> getLastMessage(@Param("ip") String ip,@Param("port") Long port,@Param("insertDate") ZonedDateTime insertDate);
	
	@Query("select m from M2mMessage m where m.validImage = true")
	Page<M2mMessage> findAllValidImages(Pageable pageable);
}
