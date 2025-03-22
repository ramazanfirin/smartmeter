package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.VibrationProMessage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VibrationProMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VibrationProMessageRepository extends JpaRepository<VibrationProMessage, Long> {

}
