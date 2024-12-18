package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.config.JPAConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RentalLogService rentalLogService;



    public ReservationService(ReservationRepository reservationRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository,
                              RentalLogService rentalLogService) {
        this.reservationRepository = reservationRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.rentalLogService = rentalLogService;
    }

    // TODO: 1. 트랜잭션 이해
    @Transactional
    public void createReservation(Long itemId, Long userId, LocalDateTime startAt, LocalDateTime endAt) {
        // 쉽게 데이터를 생성하려면 아래 유효성검사 주석 처리
        List<Reservation> haveReservations = reservationRepository.findConflictingReservations(itemId, startAt, endAt);
        if(!haveReservations.isEmpty()) {
            throw new ReservationConflictException("해당 물건은 이미 그 시간에 예약이 있습니다.");
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
        Reservation reservation = new Reservation(item, user, ReservationStatus.PENDING, startAt,endAt);

        Reservation savedReservation = reservationRepository.save(reservation);

        RentalLog rentalLog = new RentalLog(savedReservation, "로그 메세지", "CREATE");
        rentalLogService.save(rentalLog);
    }

    // TODO: 3. N+1 문제
    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllWithFetchJoin();

        return reservations.stream().map(reservation -> {
            User user = reservation.getUser();
            Item item = reservation.getItem();

            return new ReservationResponseDto(
                    reservation.getId(),
                    user.getNickname(),
                    item.getName(),
                    reservation.getStartAt(),
                    reservation.getEndAt()
            );
        }).toList();
    }

    // TODO: 5. QueryDSL 검색 개선
    public List<ReservationResponseDto> searchAndConvertReservations(Long userId, Long itemId) {

        List<Reservation> reservations = searchReservations(userId, itemId);

        return convertToDto(reservations);
    }

    public List<Reservation> searchReservations(Long userId, Long itemId) {

        return reservationRepository.SearchUserAndItem(userId, itemId);
    }

    private List<ReservationResponseDto> convertToDto(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getUser().getNickname(),
                        reservation.getItem().getName(),
                        reservation.getStartAt(),
                        reservation.getEndAt()
                ))
                .toList();
    }

    // TODO: 7. 리팩토링
    @Transactional
    public void updateReservationStatus(Long reservationId, String inStatus) {
        ReservationStatus status;
        try {
            status = ReservationStatus.valueOf(inStatus.toUpperCase()); // 대소문자 무시
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 상태값입니다: " + inStatus);
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 데이터가 존재하지 않습니다."));

        ReservationStatus oldStatus = reservation.getStatus();
        validStatus(oldStatus, status);

        reservation.updateStatus(status);
    }

    private void validStatus(ReservationStatus oldStatus, ReservationStatus inStatus) {
        if (oldStatus.equals(ReservationStatus.PENDING)) {
            // PENDING 상태에서 APPROVED 또는 EXPIRED로만 변경 가능
            if (!(inStatus.equals(ReservationStatus.APPROVED) || inStatus.equals(ReservationStatus.EXPIRED))) {
                throw new IllegalArgumentException("PENDING 상태는 APPROVED 또는 EXPIRED로만 변경 가능합니다.");
            }
        } else if (oldStatus.equals(ReservationStatus.EXPIRED)) {
            // EXPIRED 상태에서는 CANCELLED로 변경 불가
            if (inStatus.equals(ReservationStatus.CANCELED)) {
                throw new IllegalArgumentException("EXPIRED 상태인 예약은 CANCELLED 상태로 변경할 수 없습니다.");
            }
        } else if (oldStatus.equals(ReservationStatus.APPROVED)) {
            // APPROVED 상태는 EXPIRED로만 변경 가능 (예시)
            if (!inStatus.equals(ReservationStatus.EXPIRED)) {
                throw new IllegalArgumentException("APPROVED 상태는 EXPIRED 상태로만 변경 가능합니다.");
            }
        } else {
            // 정의되지 않은 상태 변경 요청에 대한 처리
            throw new IllegalArgumentException(
                    String.format("현재 상태 %s에서 %s로의 변경은 허용되지 않습니다.", oldStatus, inStatus)
            );
        }
    }
}
