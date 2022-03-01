package com.jamesmhare.viewthequeue.model.themepark;

import com.jamesmhare.viewthequeue.model.AbstractTimestampedEntity;
import com.jamesmhare.viewthequeue.model.OperatingStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * Represents a Theme Park.
 *
 * @author James Hare
 */
@Generated
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "theme_parks")
public class ThemePark extends AbstractTimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long themeParkId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private OperatingStatus operatingStatus;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime openingTime;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime closingTime;

}
