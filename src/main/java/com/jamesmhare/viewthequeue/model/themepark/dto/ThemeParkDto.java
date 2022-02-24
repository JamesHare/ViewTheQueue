package com.jamesmhare.viewthequeue.model.themepark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamesmhare.viewthequeue.model.OperatingStatus;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long themeParkId;

    @ApiModelProperty(required = true, value = "Name of the theme park.")
    @NotEmpty
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(required = true, value = "Description of the theme park.")
    @NotEmpty
    @JsonProperty("description")
    private String description;

    @ApiModelProperty(required = true, value = "The operating status of the theme park.")
    @JsonProperty("operatingStatus")
    private OperatingStatus operatingStatus;

    @ApiModelProperty(required = true, value = "The opening time of the theme park.")
    @JsonProperty("openingTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime openingTime;

    @ApiModelProperty(required = true, value = "The closing time of the theme park.")
    @JsonProperty("closingTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime closingTime;

}
