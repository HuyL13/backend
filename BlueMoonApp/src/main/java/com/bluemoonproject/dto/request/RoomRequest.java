package com.bluemoonproject.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRequest {

    private String roomNumber;

    private Integer floor;
//
    private Long peopleCount;

}

