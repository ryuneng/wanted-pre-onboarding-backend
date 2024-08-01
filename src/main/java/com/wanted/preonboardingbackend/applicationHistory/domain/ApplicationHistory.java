package com.wanted.preonboardingbackend.applicationHistory.domain;

import com.wanted.preonboardingbackend.jobPosting.domain.JobPosting;
import com.wanted.preonboardingbackend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "application_history")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
