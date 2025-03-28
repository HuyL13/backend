package com.bluemoonproject.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActivateRequest {
    String password;
    String firstName;
    String lastName;
//
    LocalDate dob;

    Set<String> roles = new HashSet<>(List.of("USER"));


}
