package com.bluemoonproject.dto.request;

import com.bluemoonproject.enums.FeeStatus;
import com.bluemoonproject.enums.FeeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeeUploadRequest {
    private String description;
    private LocalDate dueDate;

    private FeeStatus status;
    private FeeType feeType; // <== Thêm loại phí
}
