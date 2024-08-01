package com.wanted.preonboardingbackend.company.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nation;

    @Column(nullable = false)
    private String region;

}
