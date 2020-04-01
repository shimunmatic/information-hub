package de.shimunmatic.informationhub.service.implementation;

import de.shimunmatic.informationhub.model.ProcessedDate;
import de.shimunmatic.informationhub.service.definition.AdminService;
import de.shimunmatic.informationhub.service.definition.CountryStateService;
import de.shimunmatic.informationhub.service.definition.ProcessedDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final CountryStateService countryStateService;
    private final ProcessedDateService processedDateService;

    public AdminServiceImpl(CountryStateService countryStateService, ProcessedDateService processedDateService) {
        this.countryStateService = countryStateService;
        this.processedDateService = processedDateService;
    }

    @Transactional
    @Override
    public boolean deleteAllForProcessedDate(String dateFormatted) {
        ProcessedDate processedDate = processedDateService.getForFormattedProcessedDate(dateFormatted);
        if (processedDate != null) {
            countryStateService.deleteAllForProcessedDateId(processedDate.getId());
            processedDateService.deleteById(processedDate.getId());
            return true;
        }
        return false;
    }

    @Override
    public void forceFetch() {
        countryStateService.fetchNewStatistic();
    }

    @Override
    public void forceRemoveCacheForDailyUpdate() {
        countryStateService.evictCacheForDailyUpdate();
    }

    @Override
    public void forceRemoveAllCache() {
        countryStateService.evictAllCache();
    }
}
