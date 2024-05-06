package com.anurag.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<UserT, Integer> {
    List<UserT> findByEmail(String email);
}
