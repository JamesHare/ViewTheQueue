package com.jamesmhare.viewthequeue.model.themepark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamesmhare.viewthequeue.model.OperatingStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;

/**
 * Provides a DTO for the Theme Park object.
 *
 * @author James Hare
 */
@Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ThemeParkDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long themeParkId;

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonProperty("description")
    private String description;

    @JsonProperty("operatingStatus")
    private OperatingStatus operatingStatus;

    @JsonProperty("openingTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime openingTime;

    @JsonProperty("closingTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime closingTime;

}
