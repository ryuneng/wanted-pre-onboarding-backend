package com.wanted.preonboardingbackend.domain.jobApplication.repository;

import com.wanted.preonboardingbackend.domain.jobApplication.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobPostingIdAndUserId(Long jobPostingId, Long userId);

    @Query("SELECT a FROM JobApplication a WHERE a.user.id = :userId ORDER BY a.id DESC")
    Page<JobApplication> findAllByUserId(Pageable pageable, Long userId);
}
