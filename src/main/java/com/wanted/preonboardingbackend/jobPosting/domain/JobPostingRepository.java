package com.wanted.preonboardingbackend.jobPosting.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    @Query("SELECT j FROM JobPosting j ORDER BY j.id DESC")
    Page<JobPosting> findAll(Pageable pageable);

    @Query("SELECT j FROM JobPosting j WHERE j.position LIKE %:keyword% OR j.skill LIKE %:keyword% OR j.company.name LIKE %:keyword% ORDER BY j.id DESC")
    Page<JobPosting> findByKeyword(String keyword, Pageable pageable);
}
