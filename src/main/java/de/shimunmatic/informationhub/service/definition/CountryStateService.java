package de.shimunmatic.informationhub.service.definition;

import de.shimunmatic.informationhub.model.CountryState;

import java.util.List;

public interface CountryStateService extends CRUDService<CountryState, Long> {

    List<CountryState> getAllForProcessedDate(Long processedDateId);

    List<CountryState> getAllForCountry(String countryName);

    List<CountryState> getAllForCountryOnDate(String countryName, Long processedDateId);

    List<CountryState> getAllForWorld();

    List<String> getListOfCountries();

    void fetchNewStatistic();

    void evictCacheForDailyUpdate();

    void deleteAllForProcessedDateId(Long processedDateId);

    CountryState getAllForWorldOnDate(Long processedDateId);

    void evictAllCache();
}
