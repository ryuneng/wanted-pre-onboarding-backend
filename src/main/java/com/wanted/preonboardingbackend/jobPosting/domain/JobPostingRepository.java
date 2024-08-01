package com.wanted.preonboardingbackend.jobPosting.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, String> {
}
