package com.masterteknoloji.net.repository;

import com.masterteknoloji.net.domain.VibrationEcoMessage;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the VibrationEcoMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VibrationEcoMessageRepository extends JpaRepository<VibrationEcoMessage, Long> {

    @Query("SELECT v FROM VibrationEcoMessage v WHERE " +
           "(:sensorId IS NULL OR v.loraMessage.sensor.id = :sensorId) AND " +
           "(:startDate IS NULL OR v.loraMessage.insertDate >= :startDate) AND " +
           "(:endDate IS NULL OR v.loraMessage.insertDate <= :endDate)")
    List<VibrationEcoMessage> search(
        @Param("sensorId") Long sensorId,
        @Param("startDate") ZonedDateTime startDate,
        @Param("endDate") ZonedDateTime endDate
    );
}
