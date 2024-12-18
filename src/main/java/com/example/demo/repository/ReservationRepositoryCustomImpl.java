package com.example.demo.repository;

import com.example.demo.entity.QItem;
import com.example.demo.entity.QReservation;
import com.example.demo.entity.QUser;
import com.example.demo.entity.Reservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {
    @PersistenceContext
    EntityManager em;
    QReservation reservation = QReservation.reservation;
    JPAQueryFactory jqf = new JPAQueryFactory(em);

    @Override
    public List<Reservation> SearchUserAndItem(Long userId, Long itemId) {

        QUser user = QUser.user;
        QItem item = QItem.item;

       return jqf.selectFrom(reservation)
                .join(reservation.user, user).fetchJoin()
                .join(reservation.item, item).fetchJoin()
                .where(
                        validUserId(userId),
                        validItemId(itemId)
                )
                .fetch();
    }

    private BooleanExpression validUserId(Long userId) {
        return userId != null ? reservation.user.id.eq(userId) : null;
    }
    private BooleanExpression validItemId(Long itemId) {
        return itemId != null ? reservation.item.id.eq(itemId) : null;
    }
}
