package com.wanted.preonboardingbackend.domain.jobApplication.repository;

import com.wanted.preonboardingbackend.domain.jobApplication.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobPostingIdAndUserId(Long jobPostingId, Long userId);
}
