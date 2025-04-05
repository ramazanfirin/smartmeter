package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.VibrationProMessage;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the VibrationProMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VibrationProMessageRepository extends JpaRepository<VibrationProMessage, Long> {

    @Query("SELECT v FROM VibrationProMessage v WHERE " +
           "(:sensorId IS NULL OR v.loraMessage.sensor.id = :sensorId) AND " +
           "(:startDate IS NULL OR v.loraMessage.insertDate >= :startDate) AND " +
           "(:endDate IS NULL OR v.loraMessage.insertDate <= :endDate)")
    List<VibrationProMessage> search(
        @Param("sensorId") Long sensorId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );
}
