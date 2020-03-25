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
    private final CountryStateService countryStateService;

    @Autowired
    public CountryStateFetchingJob(CountryStateService countryStateService) {
        this.countryStateService = countryStateService;
    }

    @Scheduled(cron = "0 5 * * * *")
    public void runJob() {
        countryStateService.fetchNewStatistic();
    }

}
