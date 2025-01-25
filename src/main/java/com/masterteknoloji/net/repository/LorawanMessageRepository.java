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

	@Query("select lorawanMessage from LorawanMessage lorawanMessage where lorawanMessage.imageId = :imageId order by lorawanMessage.index")
	List<LorawanMessage> findByImageId(@Param("imageId") String imageId);
}
