package de.shimunmatic.informationhub.job;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class CountryStateFetchingJobTest {
    @Autowired
    private CountryStateFetchingJob countryStateFetchingJob;

    private String[] dates = {"02-02-2020", "02-02-2020", "02-03-2020", "02-04-2020", "02-05-2020", "02-06-2020", "02-07-2020", "02-08-2020", "02-09-2020", "02-10-2020", "02-11-2020", "02-12-2020", "02-13-2020", "02-14-2020", "02-15-2020", "02-16-2020", "02-17-2020", "02-18-2020", "02-19-2020", "02-20-2020", "02-21-2020", "02-22-2020", "02-23-2020", "02-24-2020", "02-25-2020", "02-26-2020", "02-27-2020", "02-28-2020", "02-29-2020", "03-01-2020", "03-02-2020", "03-03-2020", "03-04-2020", "03-05-2020", "03-06-2020", "03-07-2020", "03-08-2020", "03-09-2020", "03-10-2020", "03-11-2020"};

    @Test
    @DisplayName("Test fetching parsing and saving new corona statistics for one date")
    void runForDate() {
        countryStateFetchingJob.runForDate("02-02-2020");
    }

    @Test
    @DisplayName("Test fetching parsing and saving new corona statistics for dates in batch")
    void runBatchForDates() {
        Arrays.stream(dates).forEach(d -> countryStateFetchingJob.runForDate(d));
    }
}