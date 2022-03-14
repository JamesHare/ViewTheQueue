package com.jamesmhare.viewthequeue.model.attraction;

import com.jamesmhare.viewthequeue.model.AbstractTimestampedEntity;
import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import lombok.*;

import javax.persistence.*;

/**
 * Represents an Attraction of a Theme Park and Area.
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
@Table(name = "attractions")
public class Attraction extends AbstractTimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long attractionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private OperatingStatus operatingStatus;

    @Column(nullable = false)
    private Long waitTime;

    @Column(nullable = false)
    private Integer minHeightInches;

    @Column(nullable = false)
    private Integer maxHeightInches;

    @Column(name = "express_line", nullable = false)
    private Boolean hasExpressLine;

    @Column(name = "single_rider_line", nullable = false)
    private Boolean hasSingleRiderLine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "areas_to_attractions",
            joinColumns = @JoinColumn(name = "attraction_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private Area area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "theme_parks_to_attractions",
            joinColumns = @JoinColumn(name = "attraction_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_park_id"))
    private ThemePark themePark;

}
