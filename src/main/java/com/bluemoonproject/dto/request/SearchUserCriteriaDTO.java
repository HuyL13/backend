package com.bluemoonproject.dto.request;

import lombok.Data;

@Data
public class SearchUserCriteriaDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
