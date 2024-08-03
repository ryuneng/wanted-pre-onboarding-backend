package com.wanted.preonboardingbackend.domain.user.repository;

import com.wanted.preonboardingbackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
