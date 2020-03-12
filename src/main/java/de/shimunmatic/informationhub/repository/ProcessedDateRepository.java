package de.shimunmatic.informationhub.repository;

import de.shimunmatic.informationhub.model.ProcessedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedDateRepository extends JpaRepository<ProcessedDate, Long> {
    ProcessedDate findByProcessedDateFormattedEquals(String processedDateFormatted);
}
