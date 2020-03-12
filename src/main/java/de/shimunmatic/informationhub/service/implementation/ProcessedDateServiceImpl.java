package de.shimunmatic.informationhub.service.implementation;

import de.shimunmatic.informationhub.model.ProcessedDate;
import de.shimunmatic.informationhub.repository.ProcessedDateRepository;
import de.shimunmatic.informationhub.service.definition.ProcessedDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessedDateServiceImpl extends AbstractService<ProcessedDate, Long> implements ProcessedDateService {
    private final ProcessedDateRepository repository;

    @Autowired
    public ProcessedDateServiceImpl(ProcessedDateRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public ProcessedDate getForFormattedProcessedDate(String processedDateFormatted) {
        return repository.findByProcessedDateFormattedEquals(processedDateFormatted);
    }
}
