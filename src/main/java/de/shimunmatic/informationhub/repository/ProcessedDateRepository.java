package de.shimunmatic.informationhub.repository;

import de.shimunmatic.informationhub.model.ProcessedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessedDateRepository extends JpaRepository<ProcessedDate, Long> {
    ProcessedDate findByProcessedDateFormattedEquals(String processedDateFormatted);

    ProcessedDate findFirstByOrderByProcessedDateDesc();

    List<ProcessedDate> findByOrderByProcessedDateDesc();
}
