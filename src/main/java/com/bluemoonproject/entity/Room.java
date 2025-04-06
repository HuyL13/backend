package com.bluemoonproject.entity;

import com.bluemoonproject.enums.FeeStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//
    private String roomNumber;

    private Integer floor;

    private Long peopleCount;


    @ElementCollection
    @CollectionTable(name="room_users",joinColumns = @JoinColumn(name="room_id"))
    @Column(name="user_id")
    Set<Long> userIds;

    @ElementCollection
    @CollectionTable(name="room_fees",
            joinColumns = @JoinColumn(name="room_id", referencedColumnName = "id"))
    @Column(name="fee_id")
    Set<Long> feeIds=new HashSet<>();

}
