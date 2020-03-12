package de.shimunmatic.informationhub.service.implementation;

import de.shimunmatic.informationhub.model.CountryState;
import de.shimunmatic.informationhub.repository.CountryStateRepository;
import de.shimunmatic.informationhub.service.definition.CountryStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryStateServiceImpl extends AbstractService<CountryState, Long> implements CountryStateService {
    private final CountryStateRepository repository;

    @Autowired
    public CountryStateServiceImpl(CountryStateRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<CountryState> getAllForProcessedDate(Long processedDateId) {
        return repository.findByProcessedDateIdEquals(processedDateId);
    }

    @Override
    public List<CountryState> getAllForCountry(String countryName) {
        return repository.findByCountryNameEquals(countryName);
    }

    @Override
    public List<CountryState> getAllForCountryOnDate(String countryName, Long processedDateId) {
        return repository.findByCountryNameEqualsAndProcessedDateIdEquals(countryName, processedDateId);
    }
}
