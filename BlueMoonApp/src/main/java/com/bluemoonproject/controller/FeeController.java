package com.bluemoonproject.controller;

import com.bluemoonproject.dto.request.ApiResponse;
import com.bluemoonproject.entity.Fee;
import com.bluemoonproject.enums.FeeStatus;
import com.bluemoonproject.service.FeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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




}
