package com.bluemoonproject.service;

import com.bluemoonproject.dto.request.FeeUpdateRequest;
import com.bluemoonproject.dto.request.FeeUploadRequest;
import com.bluemoonproject.entity.Fee;
import com.bluemoonproject.entity.Room;
import com.bluemoonproject.enums.FeeStatus;
import com.bluemoonproject.enums.FeeType;
import com.bluemoonproject.repository.FeeRepository;
import com.bluemoonproject.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public Map<String, Double> readExcelAndCalculateFeeMap(MultipartFile file, FeeType feeType) throws IOException {
        Map<String, Double> feeMap = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua header

                Cell roomCell = row.getCell(0);
                Cell rawAmountCell = row.getCell(1);

                if (roomCell == null || rawAmountCell == null) continue;



                    String roomNumber = roomCell.getStringCellValue();
                    double rawAmount = rawAmountCell.getNumericCellValue();

                    // Log thêm thông tin để kiểm tra dữ liệu
                    System.out.println("Room: " + roomNumber + ", Amount: " + rawAmount);
                    if (rawAmount < 0) {
                        throw new IllegalArgumentException("Số liệu âm không hợp lệ tại phòng: " + roomNumber);
                    }

                    // Kiểm tra roomNumber có tồn tại trong DB hay không
                    if (!isRoomNumberValid(roomNumber)) {
                        throw new IllegalArgumentException("Phòng " + roomNumber + " không tồn tại trong hệ thống.");
                    }

                    Double calculatedAmount = calculateAmount(feeType, rawAmount);
                    feeMap.put(roomNumber, calculatedAmount);

            }
        }

        return feeMap;
    }

    public void createFeesFromExcel(FeeUploadRequest dto, MultipartFile file) throws IOException {
        Map<String, Double> feeMap = readExcelAndCalculateFeeMap(file, dto.getFeeType());
        log.info("Fee map size: {}", feeMap.size());

        List<Fee> fees = feeMap.entrySet().stream().map(entry -> {
            Fee fee = new Fee();
            fee.setRoomNumber(entry.getKey());
            fee.setAmount(entry.getValue());
            fee.setDescription(dto.getDescription());
            fee.setDueDate(dto.getDueDate());
            fee.setCreatedAt(LocalDateTime.now());
            fee.setStatus(dto.getStatus());
            return fee;
        }).toList();

        feeRepository.saveAll(fees);
    }

    public Double calculateAmount(FeeType feeType, double rawAmount) {
        return switch (feeType) {
            case ELECTRICITY -> rawAmount * 3000; // Ví dụ: 3000đ / kWh
            case WATER -> rawAmount * 15000;      // Ví dụ: 15000đ / m³
            case ELSE-> rawAmount;            // Phí khác giữ nguyên giá trị ghi, mặc định đơn vị VND
        };
    }

    private boolean isRoomNumberValid(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }

}

