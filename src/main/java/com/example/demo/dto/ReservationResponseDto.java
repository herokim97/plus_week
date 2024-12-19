package com.example.demo.dto;

import com.example.demo.entity.ReservationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationResponseDto {
    private Long id;
    private String nickname;
    private String itemName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private ReservationStatus status;

    public ReservationResponseDto(Long id, String nickname, String itemName,ReservationStatus status, LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.nickname = nickname;
        this.itemName = itemName;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
