package com.jamesmhare.viewthequeue.model.attraction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.area.Area;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * Provides a DTO for the Attraction object.
 *
 * @author James Hare
 */
@Data
@Builder
@Validated
public class AttractionDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long attractionId;

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonProperty("description")
    private String description;

    @JsonProperty("operatingStatus")
    private OperatingStatus operatingStatus;

    @JsonProperty("waitTime")
    private Long waitTime;

    @NotEmpty
    @JsonProperty("maxHeightInches")
    private Integer maxHeightInches;

    @NotEmpty
    @JsonProperty("minHeightInches")
    private Integer minHeightInches;

    @NotEmpty
    @JsonProperty("hasExpressLine")
    private Boolean hasExpressLine;

    @NotEmpty
    @JsonProperty("hasSingleRiderLine")
    private Boolean hasSingleRiderLine;

    @NotEmpty
    @JsonProperty("area")
    private Area area;

    @NotEmpty
    @JsonProperty("themePark")
    private ThemePark themePark;

}
