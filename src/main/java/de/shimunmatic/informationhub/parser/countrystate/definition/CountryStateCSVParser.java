package de.shimunmatic.informationhub.parser.countrystate.definition;

import de.shimunmatic.informationhub.model.CountryState;
import org.apache.commons.csv.CSVRecord;

public interface CountryStateCSVParser {

    CountryState parse(CSVRecord record);

    int getVersion();
}
