package com.bluemoonproject.mapper;

import com.bluemoonproject.dto.request.FeeUpdateRequest;
import com.bluemoonproject.entity.Fee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T09:21:13+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Oracle Corporation)"
)
@Component
public class FeeMapperImpl implements FeeMapper {

    @Override
    public void updateFee(Fee fee, FeeUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        fee.setRoomNumber( request.getRoomNumber() );
        fee.setDescription( request.getDescription() );
        fee.setAmount( request.getAmount() );
        fee.setDueDate( request.getDueDate() );
    }
}
