package com.anurag.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserId(Integer userId);

    List<Booking> findByUserIdAndShowId(Integer userId, Integer showId);
}
