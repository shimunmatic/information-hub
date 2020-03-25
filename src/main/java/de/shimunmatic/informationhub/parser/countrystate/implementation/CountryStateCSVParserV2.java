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
public class CountryStateCSVParserV2 implements CountryStateCSVParser {
    private static final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withLocale(Locale.getDefault()).withZone(ZoneId.of("UTC"));
    private static final DateTimeFormatter utcFormatterFallback1 = DateTimeFormatter.ofPattern("M/dd/YY HH:mm")
            .withLocale(Locale.getDefault()).withZone(ZoneId.of("UTC"));
    private static final DateTimeFormatter utcFormatterFallback2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withLocale(Locale.getDefault()).withZone(ZoneId.of("UTC"));
    private static final int VERSION = 2;

    @Override
    public CountryState parse(CSVRecord record) {
        CountryState.CountryStateBuilder builder = CountryState.builder();
        log.info("Parsing record: {}", record);
        builder
                .stateName(record.get(2))
                .countryName(record.get(3))
                .latitude(Double.parseDouble(record.get(5)))
                .longitude(Double.parseDouble(record.get(6)))
                .confirmedCases(Integer.parseInt(record.get(7)))
                .deathCases(Integer.parseInt(record.get(8)))
                .recoveredCases(Integer.parseInt(record.get(9)));
        try {
            builder.lastUpdated(Instant.from(utcFormatter.parse(record.get(4))));
        } catch (Exception e) {
            try {
                builder.lastUpdated(Instant.from(utcFormatterFallback1.parse(record.get(4))));
            } catch (Exception e1) {
                builder.lastUpdated(Instant.from(utcFormatterFallback2.parse(record.get(4))));
            }
        }

        return builder.build();
    }

    @Override
    public int getVersion() {
        return VERSION;
    }
}
