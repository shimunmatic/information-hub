package de.shimunmatic.informationhub.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "processed_date", schema = "sc_information_hub")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder
public class ProcessedDate extends BaseEntity {
    @Column(name = "processed_date")
    private Instant date;
    @Column(name = "processed_date_formatted")
    private String processedDateFormatted;
}
