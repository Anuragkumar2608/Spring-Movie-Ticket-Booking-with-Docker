package com.anurag.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRepo extends JpaRepository<Show, Integer> {
    List<Show> findByTheatreId(Integer theatreId);
}
