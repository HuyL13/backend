package com.bluemoonproject.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeeUpdateRequest {
    private String roomNumber;
    private String description;

    private Double amount;

    private LocalDate dueDate;


}