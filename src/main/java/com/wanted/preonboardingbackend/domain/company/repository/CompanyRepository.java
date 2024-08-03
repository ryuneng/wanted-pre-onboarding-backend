package com.wanted.preonboardingbackend.domain.company.repository;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
