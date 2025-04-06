package com.bluemoonproject.service;


import com.bluemoonproject.dto.request.RoomRequest;
import com.bluemoonproject.dto.response.RoomResponse;
import com.bluemoonproject.entity.Room;
import com.bluemoonproject.entity.User;
import com.bluemoonproject.exception.ErrorCode;
import com.bluemoonproject.mapper.RoomMapper;
import com.bluemoonproject.repository.RoomRepository;
import com.bluemoonproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.bluemoonproject.exception.AppException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomResponse postRoom(RoomRequest request) {

        Room room = roomMapper.toRoom(request);
        if(roomRepository.existsByRoomNumber(room.getRoomNumber())) throw new AppException(ErrorCode.ROOM_EXISTS);

        roomRepository.save(room);


        return roomMapper.toRoomResponse(room);

    }

//
    @Transactional
    public Room addUserToRoom(String roomNumber, String userName) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        room.getUserIds().add(user.getId());
        room.setPeopleCount((long) room.getUserIds().size());

        return roomRepository.save(room);
    }
//

    @Transactional
    public List<String> getAllUsernamesInRoom(String roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        return room.getUserIds().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(User::getUsername)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteRoom(Long roomId) { roomRepository.deleteById(roomId); }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional
    public boolean removeUserFromRoom(Long roomId, Long userId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();

            if (room.getUserIds().contains(userId)) {
                room.getUserIds().remove(userId);
                roomRepository.save(room);
                return true; // Successfully removed user
            }
        }
        return false; // Room not found or user not in room
    }


    public RoomResponse getRoomResponseById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));

        return roomMapper.toRoomResponse(room);
    }
      public RoomResponse updateRoom(Long id, RoomRequest roomRequest) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));

        roomMapper.updateRoomFromRequest(roomRequest, room); // Update fields
        room = roomRepository.save(room); // Save updated entity

        return roomMapper.toRoomResponse(room); // Return updated response
    }

}
