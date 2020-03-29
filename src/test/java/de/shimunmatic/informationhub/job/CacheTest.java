package de.shimunmatic.informationhub.job;

import de.shimunmatic.informationhub.service.definition.CountryStateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CacheTest {
    @Autowired
    private CountryStateService stateService;

    @Test
    public void testCache(){
        stateService.getAllForWorld();
        stateService.getAllForWorld();
    }

    @Test
    public void testCacheEvict(){
        stateService.getAllForWorld();
        stateService.evictCacheForDailyUpdate();
        stateService.getAllForWorld();
    }

}
