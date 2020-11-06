package de.shimunmatic.informationhub.service.implementation;

import de.shimunmatic.informationhub.model.NetDataView;
import de.shimunmatic.informationhub.model.ProcessedDate;
import de.shimunmatic.informationhub.repository.NetDataViewRepository;
import de.shimunmatic.informationhub.service.definition.NetDataService;
import de.shimunmatic.informationhub.service.definition.ProcessedDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NetDataServiceImpl implements NetDataService {
    private final NetDataViewRepository netDataViewRepository;
    private final ProcessedDateService processedDateService;

    @Autowired
    public NetDataServiceImpl(NetDataViewRepository netDataViewRepository, ProcessedDateService processedDateService) {
        this.netDataViewRepository = netDataViewRepository;
        this.processedDateService = processedDateService;
    }

    @Override
    public List<NetDataView> getLatestForAll() {
        ProcessedDate pd = processedDateService.getLastProcessedDate();
        log.info("Getting Latest for {}", pd);
        return netDataViewRepository.findLatestForDate(pd.getProcessedDateFormatted());
    }

    @Override
    public List<NetDataView> getAllForCountry(String country) {
        log.info("Getting all for {}", country);
        return netDataViewRepository.findAllByCountryName(country);
    }

    @Override
    public NetDataView getLatestForCountry(String country) {
        log.info("Getting latest for {}", country);
        return netDataViewRepository.findOneByCountryName(country);
    }
}
