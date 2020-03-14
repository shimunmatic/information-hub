package de.shimunmatic.informationhub.job;

import com.sun.istack.NotNull;
import de.shimunmatic.informationhub.model.CountryState;
import de.shimunmatic.informationhub.model.ProcessedDate;
import de.shimunmatic.informationhub.service.definition.CountryStateService;
import de.shimunmatic.informationhub.service.definition.ProcessedDateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class CountryStateFetchingJob {
    private final String apiUrl;
    private final RestTemplate template;
    private final ProcessedDateService processedDateService;
    private final CountryStateService countryStateService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
            .withLocale(Locale.getDefault()).withZone(ZoneId.of("UTC"));
    private static final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withLocale(Locale.getDefault()).withZone(ZoneId.of("UTC"));

    @Autowired
    public CountryStateFetchingJob(@Value("${information-hub.corona.api.url}") String apiUrl, ProcessedDateService processedDateService, CountryStateService countryStateService) {
        this.apiUrl = apiUrl;
        this.template = new RestTemplate();
        this.processedDateService = processedDateService;
        this.countryStateService = countryStateService;
    }

    @Scheduled(cron = "0 5 * * * *")
    public void runJob() {
        ProcessedDate lastProcessedDate = processedDateService.getLastProcessedDate();
        String[] datesToCheck = getDatesFromLast(lastProcessedDate);

        for (String date : datesToCheck) {
            runForDate(date);
        }
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

    public void runForDate(String date) {
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

        countryStateService.save(results);
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
            list.stream().skip(1).forEach(csvRecord -> states.add(getFromRecord(csvRecord)));

        } catch (Exception e) {
            log.error("Error parsing csv: \n{}", csv, e);
        }
        return states;
    }

    private CountryState getFromRecord(CSVRecord record) {
        CountryState.CountryStateBuilder builder = CountryState.builder();
        log.info("Parsing record: {}", record);
        builder
                .stateName(record.get(0))
                .countryName(record.get(1))
                .lastUpdated(Instant.from(utcFormatter.parse(record.get(2))))
                .confirmedCases(Integer.parseInt(record.get(3)))
                .deathCases(Integer.parseInt(record.get(4)))
                .recoveredCases(Integer.parseInt(record.get(5)));
        if (record.size() == 8) {
            builder
                    .latitude(Double.parseDouble(record.get(6)))
                    .longitude(Double.parseDouble(record.get(7)));
        }
        return builder.build();
    }
}
