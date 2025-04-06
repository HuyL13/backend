package com.bluemoonproject.controller;

import com.bluemoonproject.dto.request.ApiResponse;
import com.bluemoonproject.dto.request.FeeUpdateRequest;
import com.bluemoonproject.dto.request.FeeUploadRequest;
import com.bluemoonproject.entity.Fee;
import com.bluemoonproject.enums.FeeStatus;
import com.bluemoonproject.service.FeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/fees")
public class FeeController {
    private final FeeService feeService;

    @Autowired
    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @PostMapping("/add")
    public ResponseEntity<Fee> addFee(@RequestParam String roomNumber,
                                      @RequestParam String description,
                                      @RequestParam Double amount,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {
        log.info("Add fee for room number " + roomNumber + " and description " + description);
        Fee fee = feeService.addFee(roomNumber, description, amount, dueDate);
        return ResponseEntity.ok(fee);
    }

    @PutMapping("/update-status")
    public ResponseEntity<Fee> updateFeeStatus(@RequestParam Long feeId,
                                               @RequestParam FeeStatus status) {
        Fee updatedFee = feeService.updateFeeStatus(feeId, status);
        return ResponseEntity.ok(updatedFee);
    }

    @DeleteMapping("/{feeId}")
    public ResponseEntity<String> deleteFee(@PathVariable Long feeId) {
        feeService.deleteFee(feeId);
        return ResponseEntity.ok("Fee deleted successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<Fee> updateFee(@PathVariable Long id, @RequestBody FeeUpdateRequest request) {
        Fee updatedFee = feeService.updateFee(id, request);
        return ResponseEntity.ok(updatedFee);
    }

    @GetMapping
    public ResponseEntity<List<Fee>> getAllFees() {
        return ResponseEntity.ok(feeService.getAllFees());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFees(
            @Valid @ModelAttribute FeeUploadRequest dto,
            @RequestParam("file") MultipartFile file) {

        try {
            feeService.createFeesFromExcel(dto, file);
            return ResponseEntity.ok("Fees created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

}
