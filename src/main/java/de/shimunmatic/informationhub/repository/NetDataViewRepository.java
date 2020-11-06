package de.shimunmatic.informationhub.repository;

import de.shimunmatic.informationhub.model.NetDataView;
import de.shimunmatic.informationhub.model.ProcessedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetDataViewRepository extends JpaRepository<ProcessedDate, Long> {

    @Query(value = "select nv.date as date, nv.date_formatted as dateFormatted, nv.country_name as countryName, nv.confirmed_since_day_before as confirmedCases, nv.deaths_since_day_before as confirmedDeaths" + "  from " + "sc_information_hub" + ".view_net_day_before_data nv where nv.country_name = ?1", nativeQuery = true)
    List<NetDataView> findAllByCountryName(String countryName);

    @Query(value = "select nv.date as date, nv.date_formatted as dateFormatted, nv.country_name as countryName, nv.confirmed_since_day_before as confirmedCases, nv.deaths_since_day_before as confirmedDeaths FROM sc_information_hub.view_net_day_before_data nv where nv.country_name = ?1 limit 1", nativeQuery = true)
    NetDataView findOneByCountryName(String countryName);

    @Query(value = "select nv.date as date, nv.date_formatted as dateFormatted, nv.country_name as countryName, nv.confirmed_since_day_before as confirmedCases, nv.deaths_since_day_before as confirmedDeaths FROM sc_information_hub.view_net_day_before_data nv where nv.date_formatted = ?1", nativeQuery = true)
    List<NetDataView> findLatestForDate(String date);
}
