package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.LorawanMessage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LorawanMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LorawanMessageRepository extends JpaRepository<LorawanMessage, Long> {

}
