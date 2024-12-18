package com.example.demo.repository;

import com.example.demo.entity.Reservation;

import java.util.List;

public interface ReservationRepositoryCustom {

    public List<Reservation> SearchUserAndItem(Long userId, Long itemId);
}
