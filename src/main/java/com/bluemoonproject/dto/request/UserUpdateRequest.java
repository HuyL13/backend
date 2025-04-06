package com.bluemoonproject.dto.request;

import com.bluemoonproject.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;
    String email;
    @DobConstraint(min=18,message = "INVALID_DOB")
    LocalDate dob;




}
