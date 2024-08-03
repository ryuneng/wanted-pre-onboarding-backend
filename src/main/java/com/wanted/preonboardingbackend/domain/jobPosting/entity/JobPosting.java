package com.wanted.preonboardingbackend.domain.jobPosting.entity;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_posting")
@Getter
@Setter
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

    public void update(String position, int reward, String content, String skill) {
        this.position = position;
        this.reward = reward;
        this.content = content;
        this.skill = skill;
    }

}
