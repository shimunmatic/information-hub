package de.shimunmatic.informationhub.repository;

import de.shimunmatic.informationhub.model.CountryState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryStateRepository extends JpaRepository<CountryState, Long> {

    List<CountryState> findByCountryNameEquals(String countryName);

    List<CountryState> findByCountryNameEqualsAndProcessedDateIdEquals(String countryName, Long processedDateId);

    List<CountryState> findByProcessedDateIdEquals(Long processedDateId);

    @Query("SELECT DISTINCT cs.countryName FROM CountryState cs")
    List<String> findDistinctCountryNames();
}
