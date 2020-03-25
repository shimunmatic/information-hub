package de.shimunmatic.informationhub.parser.countrystate;

import de.shimunmatic.informationhub.parser.countrystate.definition.CountryStateCSVParser;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class CountryStateCSVParserFactory {
    @Getter
    private List<CountryStateCSVParser> parsers;

    @Autowired
    public CountryStateCSVParserFactory(List<CountryStateCSVParser> parsers) {
        parsers.sort(Comparator.comparingInt(CountryStateCSVParser::getVersion));
        Collections.reverse(parsers);
        this.parsers = parsers;
    }
}
