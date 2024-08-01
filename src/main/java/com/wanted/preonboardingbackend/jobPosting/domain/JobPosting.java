package com.wanted.preonboardingbackend.jobPosting.domain;

import com.wanted.preonboardingbackend.company.domain.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_posting")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private int reward;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private String skill;

}
