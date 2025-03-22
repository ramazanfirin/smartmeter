package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.CurrentMeterMessage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CurrentMeterMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrentMeterMessageRepository extends JpaRepository<CurrentMeterMessage, Long> {

}
