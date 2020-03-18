package de.shimunmatic.informationhub.service.implementation;

import de.shimunmatic.informationhub.model.CountryState;
import de.shimunmatic.informationhub.model.ProcessedDate;
import de.shimunmatic.informationhub.repository.CountryStateRepository;
import de.shimunmatic.informationhub.service.definition.CountryStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CountryStateServiceImpl extends AbstractService<CountryState, Long> implements CountryStateService {
    private final CountryStateRepository repository;

    @Autowired
    public CountryStateServiceImpl(CountryStateRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Cacheable(cacheNames = "getAllForProcessedDate", unless = "#result == null || result.isEmpty()")
    @Override
    public List<CountryState> getAllForProcessedDate(Long processedDateId) {
        return repository.findByProcessedDateIdEquals(processedDateId);
    }

    @Cacheable(cacheNames = "getAllForCountry")
    @Override
    public List<CountryState> getAllForCountry(String countryName) {
        return repository.findByCountryNameEquals(countryName);
    }

    @Cacheable(cacheNames = "getAllForCountryOnDate", unless = "result == null || result.isEmpty()")
    @Override
    public List<CountryState> getAllForCountryOnDate(String countryName, Long processedDateId) {
        return repository.findByCountryNameEqualsAndProcessedDateIdEquals(countryName, processedDateId);
    }

    @Cacheable(cacheNames = "getAllForWorld")
    @Override
    public List<CountryState> getAllForWorld() {
        List<CountryState> states = repository.findAll();
        Map<ProcessedDate, List<CountryState>> mapped = states.stream().collect(Collectors.groupingBy(CountryState::getProcessedDate));
        List<CountryState> worldState = new ArrayList<>();
        mapped.forEach(((processedDate, countryStates) -> {

            int confirmed = 0, deaths = 0, recovered = 0;
            for (CountryState countryState : countryStates) {
                confirmed += countryState.getConfirmedCases();
                deaths += countryState.getDeathCases();
                recovered += countryState.getRecoveredCases();
            }
            worldState.add(CountryState.builder()
                    .countryName("World")
                    .processedDate(processedDate)
                    .deathCases(deaths)
                    .confirmedCases(confirmed)
                    .recoveredCases(recovered)
                    .lastUpdated(processedDate.getProcessedDate())
                    .build());
        }));
        return worldState;
    }

    @Cacheable(cacheNames = "getListOfCountries")
    @Override
    public List<String> getListOfCountries() {
        return repository.findDistinctCountryNames();
    }

    @CacheEvict(cacheNames = {"getListOfCountries", "getAllForWorld", "getAllForCountry"})
    @Override
    public void evictCacheForDailyUpdate() {
        log.info("Evicting cache for: getListOfCountries,getAllForWorld,getAllForCountry ");
    }
}
