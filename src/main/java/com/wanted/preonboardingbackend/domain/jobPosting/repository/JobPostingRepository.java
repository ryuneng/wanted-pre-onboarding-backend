package com.wanted.preonboardingbackend.domain.jobPosting.repository;

import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    @Query("SELECT j FROM JobPosting j ORDER BY j.id DESC")
    Page<JobPosting> findAll(Pageable pageable);

    @Query("SELECT j FROM JobPosting j WHERE j.position LIKE %:keyword% OR j.skill LIKE %:keyword% OR j.company.name LIKE %:keyword% ORDER BY j.id DESC")
    Page<JobPosting> findByKeyword(String keyword, Pageable pageable);

    @Query("SELECT j.id FROM JobPosting j WHERE j.company.id = :companyId AND j.id != :id")
    List<Long> findOtherJobsById(Long id, Long companyId);
}
