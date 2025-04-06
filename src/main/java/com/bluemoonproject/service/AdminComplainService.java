package com.bluemoonproject.service;

import com.bluemoonproject.entity.Complain;
import com.bluemoonproject.enums.Priority;
import com.bluemoonproject.enums.Status;
import com.bluemoonproject.repository.ComplainRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AdminComplainService {

    private final ComplainRepository complainRepository;


    // Admin service to resolve a complaint and set priority and status
    public Complain resolveComplain(Long complainId, String response, Priority priority, Status status) {
        Complain complain = complainRepository.findById(complainId).orElseThrow(() -> new RuntimeException("Complain not found"));
        complain.setResponse(response);
        complain.setPrior(priority);  // Change the priority
        complain.setStatus(status);   // Change the status
        return complainRepository.save(complain);
    }

    // Admin service to delete a complaint
    public void deleteComplain(Long complainId) {
        complainRepository.deleteById(complainId);
    }

    // Admin service to get all complaints
    public List<Complain> getAllComplains() {
        return complainRepository.findAll();
    }
}

