package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.VibrationEcoMessage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VibrationEcoMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VibrationEcoMessageRepository extends JpaRepository<VibrationEcoMessage, Long> {

}
