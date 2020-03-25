package de.shimunmatic.informationhub.job;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CountryStateFetchingJobTest {
    @Autowired
    private CountryStateFetchingJob countryStateFetchingJob;

    @Test
    @DisplayName("Test scheduled run job method")
    void runJob() {
        countryStateFetchingJob.runJob();
    }
}
