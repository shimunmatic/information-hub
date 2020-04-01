package de.shimunmatic.informationhub.service.implementation;

import com.sun.istack.NotNull;
import de.shimunmatic.informationhub.model.CountryState;
import de.shimunmatic.informationhub.model.ProcessedDate;
import de.shimunmatic.informationhub.parser.countrystate.CountryStateCSVParserFactory;
import de.shimunmatic.informationhub.parser.countrystate.definition.CountryStateCSVParser;
import de.shimunmatic.informationhub.repository.CountryStateRepository;
import de.shimunmatic.informationhub.service.definition.CountryStateService;
import de.shimunmatic.informationhub.service.definition.ProcessedDateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CountryStateServiceImpl extends AbstractService<CountryState, Long> implements CountryStateService {
    private final String apiUrl;
    private final RestTemplate template;
    private final CountryStateRepository repository;
    private final ProcessedDateService processedDateService;
    private final CacheManager cacheManager;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
            .withLocale(Locale.getDefault()).withZone(ZoneId.of("UTC"));
    private static final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withLocale(Locale.getDefault()).withZone(ZoneId.of("UTC"));
    private CountryStateCSVParserFactory countryStateCSVParserFactory;

    @Autowired
    public CountryStateServiceImpl(CountryStateRepository repository, @Value("${information-hub.corona.api.url}") String apiUrl, RestTemplate template, ProcessedDateService processedDateService, CountryStateCSVParserFactory countryStateCSVParserFactory, CacheManager cacheManager) {
        super(repository);
        this.apiUrl = apiUrl;
        this.template = template;
        this.repository = repository;
        this.processedDateService = processedDateService;
        this.countryStateCSVParserFactory = countryStateCSVParserFactory;
        this.cacheManager = cacheManager;
    }

    @Cacheable(cacheNames = "getAllForProcessedDate", unless = "#result == null || #result.isEmpty()")
    @Override
    public List<CountryState> getAllForProcessedDate(Long processedDateId) {
        log.info("Not using cache...getAllForProcessedDate");
        return repository.findByProcessedDateIdEquals(processedDateId);
    }

    @Cacheable(cacheNames = "getAllForCountry")
    @Override
    public List<CountryState> getAllForCountry(String countryName) {
        log.info("Not using cache...getAllForCountry");
        return repository.findByCountryNameEquals(countryName);
    }

    @Cacheable(cacheNames = "getAllForCountryOnDate", unless = "#result == null || #result.isEmpty()")
    @Override
    public List<CountryState> getAllForCountryOnDate(String countryName, Long processedDateId) {
        log.info("Not using cache...getAllForCountryOnDate");
        return repository.findByCountryNameEqualsAndProcessedDateIdEquals(countryName, processedDateId);
    }

    @Cacheable(cacheNames = "getAllForWorld")
    @Override
    public List<CountryState> getAllForWorld() {
        log.info("Not using cache...getAllForWorld");
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
        log.info("Not using cache...getListOfCountries");
        return repository.findDistinctCountryNames();
    }

    @Override
    public void fetchNewStatistic() {
        ProcessedDate lastProcessedDate = processedDateService.getLastProcessedDate();
        String[] datesToCheck = getDatesFromLast(lastProcessedDate);

        for (String date : datesToCheck) {
            runForDate(date);
        }
    }

    @CacheEvict(cacheNames = {"getListOfCountries", "getAllForWorld", "getAllForCountry"}, allEntries = true)
    @Override
    public void evictCacheForDailyUpdate() {
        log.info("Evicting cache for: getListOfCountries,getAllForWorld,getAllForCountry ");
        cacheManager.getCache("getListOfCountries").clear();
        cacheManager.getCache("getAllForWorld").clear();
        cacheManager.getCache("getAllForCountry").clear();

    }

    @Override
    public void deleteAllForProcessedDateId(Long processedDateId) {
        log.info("Deleting all for processedDateId {}", processedDateId);
        repository.deleteByProcessedDateIdEquals(processedDateId);
    }

    @Cacheable(cacheNames = "getAllForWorldOnDate", unless = "#result == null")
    @Override
    public CountryState getAllForWorldOnDate(Long processedDateId) {
        log.info("Not using cache...getAllForWorldOnDate");
        List<CountryState> states = repository.findByProcessedDateIdEquals(processedDateId);
        Optional<ProcessedDate> oDate = processedDateService.getById(processedDateId);
        if (oDate.isEmpty()) return null;
        return getStateFromStatesAndDate("World", oDate.get(), states);

    }

    @CacheEvict()
    @Override
    public void evictAllCache() {
        log.info("Evicting all cache");
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
    }

    private CountryState getStateFromStatesAndDate(String name, ProcessedDate processedDate, List<CountryState> states) {
        int confirmed = 0, deaths = 0, recovered = 0;
        for (CountryState countryState : states) {
            confirmed += countryState.getConfirmedCases();
            deaths += countryState.getDeathCases();
            recovered += countryState.getRecoveredCases();
        }
        return CountryState.builder()
                .countryName(name)
                .processedDate(processedDate)
                .deathCases(deaths)
                .confirmedCases(confirmed)
                .recoveredCases(recovered)
                .lastUpdated(processedDate.getProcessedDate())
                .build();

    }

    @NotNull
    private String[] getDatesFromLast(ProcessedDate lastProcessedDate) {
        List<String> dates = new ArrayList<>();
        Instant tempDate = Instant.from(lastProcessedDate.getProcessedDate());
        tempDate = tempDate.plus(1, ChronoUnit.DAYS);

        while (tempDate.isBefore(Instant.now())) {
            dates.add(formatter.format(tempDate));
            tempDate = tempDate.plus(1, ChronoUnit.DAYS);
        }

        return dates.toArray(new String[0]);
    }

    private void runForDate(String date) {
        ProcessedDate processedDate = processedDateService.getForFormattedProcessedDate(date);
        if (processedDate != null) {
            log.info("Up to date!");
            return;
        }

        List<CountryState> results = fetchForDate(date);
        if (results == null || results.isEmpty()) {
            log.info("There were no results");
            return;
        }
        ProcessedDate newProcessedDate = processedDateService.save(new ProcessedDate(Instant.from(LocalDate.from(formatter.parse(date)).atStartOfDay().atZone(ZoneId.of("UTC"))), date));
        results.forEach(cs -> cs.setProcessedDate(newProcessedDate));

        save(results);
        evictCacheForDailyUpdate();
    }


    private List<CountryState> fetchForDate(@NotNull String date) {
        log.info("Fetching for Date {}", date);
        String url = String.format(apiUrl, date);
        try {
            log.info("Using url: {}", url);
            ResponseEntity<String> response = template.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseCountryStates(response.getBody());
            }
            log.warn("Error fetching for date: {}, status code: {}", date, response.getStatusCode());
        } catch (Exception e) {
            log.error("Error while fetching data.", e);
        }
        return null;
    }

    private List<CountryState> parseCountryStates(@NotNull String csv) {
        List<CountryState> states = new ArrayList<>();
        try {
            Reader in = new StringReader(csv);
            CSVParser parser = new CSVParser(in, CSVFormat.EXCEL);
            List<CSVRecord> list = parser.getRecords();
            boolean first = true;
            for (CSVRecord csvRecord : list) {
                if (first) {
                    first = false;
                    continue;
                }
                states.add(getFromRecord(csvRecord));
            }

        } catch (Exception e) {
            log.error("Error parsing csv: \n{}", csv, e);
        }
        return states;
    }

    private CountryState getFromRecord(CSVRecord record) throws Exception {
        CountryState state = null;

        for (CountryStateCSVParser parser : countryStateCSVParserFactory.getParsers()) {
            try {
                state = parser.parse(record);
                break;
            } catch (Exception e) {
                log.error("Error while parsing, trying different parser! record:{}, parser Version: {}", record, parser.getVersion(), e);
            }
        }
        if (state == null) {
            throw new Exception("All parsers returned error");
        }

        return state;
    }
}
