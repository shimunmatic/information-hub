package de.shimunmatic.informationhub.service.definition;

import de.shimunmatic.informationhub.model.ProcessedDate;

public interface ProcessedDateService extends CRUDService<ProcessedDate, Long> {

    ProcessedDate getForFormattedProcessedDate(String processedDateFormatted);

}
