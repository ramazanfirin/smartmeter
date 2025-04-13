package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.Meter;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Meter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {

}
