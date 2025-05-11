package com.bluemoonproject.mapper;

import com.bluemoonproject.dto.request.RoomRequest;
import com.bluemoonproject.dto.response.RoomResponse;
import com.bluemoonproject.entity.Room;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T09:21:13+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Oracle Corporation)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public Room toRoom(RoomRequest request) {
        if ( request == null ) {
            return null;
        }

        Room.RoomBuilder room = Room.builder();

        room.roomNumber( request.getRoomNumber() );
        room.floor( request.getFloor() );
        room.peopleCount( request.getPeopleCount() );
        room.residentCount( request.getResidentCount() );
        room.area( request.getArea() );
        room.roomType( request.getRoomType() );
        room.status( request.getStatus() );

        return room.build();
    }

    @Override
    public RoomResponse toRoomResponse(Room room) {
        if ( room == null ) {
            return null;
        }

        RoomResponse.RoomResponseBuilder roomResponse = RoomResponse.builder();

        if ( room.getId() != null ) {
            roomResponse.id( room.getId() );
        }
        roomResponse.floor( room.getFloor() );
        roomResponse.roomNumber( room.getRoomNumber() );
        roomResponse.peopleCount( room.getPeopleCount() );
        roomResponse.area( room.getArea() );
        roomResponse.roomType( room.getRoomType() );
        roomResponse.status( room.getStatus() );

        return roomResponse.build();
    }

    @Override
    public void updateRoomFromRequest(RoomRequest request, Room room) {
        if ( request == null ) {
            return;
        }

        room.setRoomNumber( request.getRoomNumber() );
        room.setFloor( request.getFloor() );
        room.setPeopleCount( request.getPeopleCount() );
        room.setResidentCount( request.getResidentCount() );
        room.setArea( request.getArea() );
        room.setRoomType( request.getRoomType() );
        room.setStatus( request.getStatus() );
    }
}
