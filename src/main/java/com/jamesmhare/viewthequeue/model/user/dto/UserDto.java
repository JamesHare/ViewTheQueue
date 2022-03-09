package com.jamesmhare.viewthequeue.model.user.dto;

import lombok.*;

/**
 * Provides a DTO for the User class.
 *
 * @author James Hare
 */
@Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;
    private String lastName;
    private String emailAddress;
    @ToString.Exclude
    private String password;
    @ToString.Exclude
    private String repeatedPassword;

}
