package de.shimunmatic.informationhub.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "country_state", schema = "sc_information_hub")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder
public class CountryState extends BaseEntity {
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "state_name")
    private String stateName;
    @Column(name = "last_updated")
    private Instant lastUpdated;
    @Column(name = "confirmed_cases")
    private Integer confirmedCases;
    @Column(name = "death_cases")
    private Integer deathCases;
    @Column(name = "recovered_cases")
    private Integer recoveredCases;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @ManyToOne
    @JoinColumn(name = "processed_date_id", nullable = false)
    private ProcessedDate processedDate;
}
