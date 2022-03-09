package com.jamesmhare.viewthequeue.model.area;

import com.jamesmhare.viewthequeue.model.AbstractTimestampedEntity;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import lombok.*;

import javax.persistence.*;

/**
 * Represents an Area of a Theme Park.
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
@Table(name = "areas")
public class Area extends AbstractTimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long areaId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "theme_parks_to_areas",
            joinColumns = @JoinColumn(name = "area_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_park_id"))
    private ThemePark themePark;

}
