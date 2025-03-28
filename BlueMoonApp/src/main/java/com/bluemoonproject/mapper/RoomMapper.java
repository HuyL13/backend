package com.bluemoonproject.mapper;


import com.bluemoonproject.dto.request.RoleRequest;
import com.bluemoonproject.dto.request.RoomRequest;
import com.bluemoonproject.dto.response.RoleResponse;
import com.bluemoonproject.dto.response.RoomResponse;
import com.bluemoonproject.entity.Role;
import com.bluemoonproject.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room toRoom(RoomRequest request);
//
    RoomResponse toRoomResponse(Room room);
}
