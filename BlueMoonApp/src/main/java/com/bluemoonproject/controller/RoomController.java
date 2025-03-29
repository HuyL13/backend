package com.bluemoonproject.controller;

import com.bluemoonproject.dto.request.ApiResponse;
import com.bluemoonproject.dto.request.RoomAddRequest;
import com.bluemoonproject.dto.request.RoomRequest;
import com.bluemoonproject.dto.response.RoomResponse;
import com.bluemoonproject.entity.Room;
import com.bluemoonproject.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/room")
public class RoomController {
    private final RoomService roomService;
///
    @PostMapping("/new")
    public ApiResponse<RoomResponse> postRoom(@RequestBody RoomRequest request) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.postRoom(request))
                .build();

    }
//
    @PostMapping("/addUser")
    public ApiResponse addUserToRoom(@RequestBody RoomAddRequest roomAddRequest) {
        Room updatedRoom = roomService.addUserToRoom(roomAddRequest.getRoomNumber(), roomAddRequest.getUsername());

        return ApiResponse.builder()
                .message("User added to room successfully")
                .build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<String>> getAllUsernamesInRoom(@RequestParam String roomNumber) {
        List<String> usernames = roomService.getAllUsernamesInRoom(roomNumber);
        return ResponseEntity.ok(usernames);
    }

    @DeleteMapping("/{roomId}")
    public ApiResponse delRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ApiResponse.builder().message("Room deleted successfully").build();


    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @DeleteMapping("/{roomId}/users/{userId}")
    public ResponseEntity<String> removeUserFromRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        boolean removed = roomService.removeUserFromRoom(roomId, userId);
        if (removed) {
            return ResponseEntity.ok("User removed from room successfully.");
        } else {
            return ResponseEntity.badRequest().body("User not found in the room or room does not exist.");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomResponseById(id));
    }

     @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomRequest));
    }
}
