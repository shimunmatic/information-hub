package de.shimunmatic.informationhub.parser.countrystate.implementation;

import de.shimunmatic.informationhub.model.CountryState;
import de.shimunmatic.informationhub.parser.countrystate.definition.CountryStateCSVParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@Slf4j
public class CountryStateCSVParserV1 implements CountryStateCSVParser {
    private static final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withLocale(Locale.getDefault()).withZone(ZoneId.of("UTC"));
    private static final int VERSION = 1;

    @Override
    public CountryState parse(CSVRecord record) {
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

    @Override
    public int getVersion() {
        return VERSION;
    }
}
