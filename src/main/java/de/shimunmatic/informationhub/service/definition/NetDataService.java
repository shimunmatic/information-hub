package de.shimunmatic.informationhub.service.definition;

import de.shimunmatic.informationhub.model.NetDataView;

import java.util.List;

public interface NetDataService {

    List<NetDataView> getLatestForAll();

    List<NetDataView> getAllForCountry(String country);

    NetDataView getLatestForCountry(String country);

}
