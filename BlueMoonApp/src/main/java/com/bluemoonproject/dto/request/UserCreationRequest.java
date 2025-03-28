package com.bluemoonproject.dto.request;

import com.bluemoonproject.validator.DobConstraint;
import com.bluemoonproject.validator.PasswordConstraint;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NonNull
    @Size(min=3,message ="USERNAME_INVALID")
    String username;
//
    @NonNull
    @PasswordConstraint
    String password;

    @NonNull
    String email;

    @NonNull
    String firstName;

    @NonNull
    String lastName;

    @NonNull
    @DobConstraint(min=18,message = "INVALID_DOB")
    LocalDate dob;


}
