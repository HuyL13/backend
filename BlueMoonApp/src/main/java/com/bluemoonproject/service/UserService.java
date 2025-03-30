package com.bluemoonproject.service;

import com.bluemoonproject.constant.PredefinedRole;
import com.bluemoonproject.dto.request.UserActivateRequest;
import com.bluemoonproject.dto.request.UserCreationRequest;
import com.bluemoonproject.dto.request.UserUpdateRequest;
import com.bluemoonproject.dto.response.UserResponse;
import com.bluemoonproject.entity.*;
import com.bluemoonproject.enums.FeeStatus;
import com.bluemoonproject.exception.AppException;
import com.bluemoonproject.exception.ErrorCode;
import com.bluemoonproject.mapper.UserMapper;
import com.bluemoonproject.repository.*;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    private final FeeRepository feeRepository;
    RoomRepository roomRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final GuestRepository guestRepository;

    public UserResponse createUser(UserCreationRequest request) {
        log.info("Service: create user");

        // Check if the username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS); // Handle duplicate username
        }
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

        // Convert request to User entity
        User user = userMapper.toUser(request);


        user.setPassword(passwordEncoder.encode(request.getPassword()));


        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        // Save user to the database
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }
    
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo(){
        var context=SecurityContextHolder.getContext();
        String name=context.getAuthentication().getName();

        User user=userRepository.findByUsername(name).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        return userMapper.toUserResponse(user);
    }
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }


    public List<UserResponse> getUsers(){
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    public UserResponse getUser(Long id){
        log.info("In method get user by Id");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }


    public User activateAccount(String email) {
        Guest guest = guestRepository.findByEmail(email);

        if (guest == null) {
            throw new RuntimeException("Guest account not found.");
        }

        User user = new User();
        user.setUsername(guest.getUsername());
        user.setPassword(passwordEncoder.encode(guest.getPassword()));
        user.setFirstName(guest.getFirstName());
        user.setLastName(guest.getLastName());
        user.setDob(guest.getDob());
        user.setEmail(guest.getEmail());
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        user.setRoles(roles);

        user = userRepository.save(user);
        guestRepository.delete(guest); // Remove guest after activation

        return user;
    }

    public List<Fee> getUnpaidFeesForUser() {
        // Find rooms where the user is assigned
        var context=SecurityContextHolder.getContext();
        String name=context.getAuthentication().getName();

        User user=userRepository.findByUsername(name).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        List<Room> rooms = roomRepository.findByUserIdsContaining(user.getId());

        // Collect unpaid fees from these rooms
        List<Fee> unpaidFees = new ArrayList<>();
        for (Room room : rooms) {
            for (Long feeId : room.getFeeIds()) {
                feeRepository.findById(feeId)
                        .filter(fee -> fee.getStatus() == FeeStatus.UNPAID)
                        .ifPresent(unpaidFees::add);
            }
        }
        return unpaidFees;
    }

    public String resetPassword(String email, String otp, String newPassword) {
        if (!otpService.verifyOtp(email, otp)) {
            return "Invalid OTP!";
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return "Password reset successful!";
        } else {
            return "User not found!";
        }
    }

    @Transactional
    public void makeUserAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        user.getRoles().add(adminRole);
        userRepository.save(user);
    }

    public List<Room> getRoomsForUser() {
        var context=SecurityContextHolder.getContext();
        String name=context.getAuthentication().getName();

        User user=userRepository.findByUsername(name).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        return roomRepository.findByUserIdsContaining(user.getId());
    }
}
//
