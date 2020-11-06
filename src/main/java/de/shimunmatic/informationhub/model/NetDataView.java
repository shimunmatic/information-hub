package de.shimunmatic.informationhub.model;

public interface NetDataView {
    String getDate();

    String getDateFormatted();

    String getCountryName();

    Integer getConfirmedCases();

    Integer getConfirmedDeaths();
}
