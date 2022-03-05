package com.jamesmhare.viewthequeue.model.area.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamesmhare.viewthequeue.model.OperatingStatus;
import com.jamesmhare.viewthequeue.model.themepark.ThemePark;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;

/**
 * Provides a DTO for the Area object.
 *
 * @author James Hare
 */
@Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AreaDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long areaId;

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonProperty("description")
    private String description;

    @NotEmpty
    @JsonProperty("themePark")
    private ThemePark themePark;

}
