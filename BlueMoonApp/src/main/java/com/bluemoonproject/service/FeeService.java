package com.bluemoonproject.service;

import com.bluemoonproject.dto.request.FeeUpdateRequest;
import com.bluemoonproject.entity.Fee;
import com.bluemoonproject.entity.Room;
import com.bluemoonproject.enums.FeeStatus;
import com.bluemoonproject.repository.FeeRepository;
import com.bluemoonproject.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class FeeService {
    private final FeeRepository feeRepository;
    private final RoomRepository roomRepository;
//
    @Autowired
    public FeeService(FeeRepository feeRepository, RoomRepository roomRepository) {
        this.feeRepository = feeRepository;
        this.roomRepository = roomRepository;
    }
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Fee addFee(String roomNumber, String description, Double amount, LocalDate dueDate) {
        // Check if the room exists
        log.info("Adding fee for room {}", roomNumber);
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found with number: " + roomNumber));
        log.info("Adding fee for room: " + roomNumber + " with description: " + description);
        // Create and save the fee
        Fee fee = new Fee();
        fee.setRoomNumber(roomNumber);
        fee.setDescription(description);
        fee.setAmount(amount);
        fee.setDueDate(dueDate);
        fee.setCreatedAt(LocalDateTime.now());
        fee.setStatus(FeeStatus.UNPAID);
        Fee savedFee = feeRepository.save(fee);

        // Add the fee ID to the room’s feeIds set
        room.getFeeIds().add(savedFee.getId());
        roomRepository.save(room);

        return savedFee;
    }

    public Fee updateFeeStatus(Long feeId, FeeStatus status) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee not found with id: " + feeId));

        fee.setStatus(status);
        return feeRepository.save(fee);
    }

    public void deleteFee(Long feeId) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee not found with id: " + feeId));

        //Remove the fee ID from the associated room
        Room room = roomRepository.findByRoomNumber(fee.getRoomNumber())
               .orElseThrow(() -> new RuntimeException("Room not found with number: " + fee.getRoomNumber()));

        room.getFeeIds().remove(feeId);
        roomRepository.save(room);

        // Delete the fee
        feeRepository.deleteById(feeId);
    }

    @Transactional
    public Fee updateFee(Long id, FeeUpdateRequest request) {
        Fee fee = feeRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Fee with ID " + id + " not found"));

        fee.setRoomNumber(request.getRoomNumber());
        fee.setDescription(request.getDescription());
        fee.setAmount(request.getAmount());
        fee.setDueDate(request.getDueDate());
        fee.setCreatedAt(LocalDateTime.now());

        return feeRepository.save(fee);
    }


    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }
}

