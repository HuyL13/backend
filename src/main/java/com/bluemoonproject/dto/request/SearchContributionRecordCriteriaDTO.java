package com.bluemoonproject.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SearchContributionRecordCriteriaDTO {

    private Long id;                     // ID của bản ghi đóng góp

    private Long userId;                // ID người đóng góp

    private Long contributionId;        // ID của khoản đóng góp (khóa ngoại)

    private Double amount;              // Số tiền đóng góp

    private LocalDateTime contributedAt; // Ngày đóng góp

    private Boolean approved;           // Trạng thái duyệt
}
