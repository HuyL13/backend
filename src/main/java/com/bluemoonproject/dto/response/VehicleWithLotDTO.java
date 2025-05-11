package com.bluemoonproject.dto.response;

import com.bluemoonproject.entity.Vehicle;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleWithLotDTO {
    private String licensePlate;
    private Vehicle.Type type;
    private String lotNumber; // null if not assigned

    public VehicleWithLotDTO(String licensePlate, Vehicle.Type type, String lotNumber) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.lotNumber = lotNumber;
    }

    // Getters & setters
}
