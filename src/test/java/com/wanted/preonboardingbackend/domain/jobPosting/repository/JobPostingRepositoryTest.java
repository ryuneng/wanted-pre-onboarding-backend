package com.wanted.preonboardingbackend.domain.jobPosting.repository;

import com.wanted.preonboardingbackend.domain.company.entity.Company;
import com.wanted.preonboardingbackend.domain.company.repository.CompanyRepository;
import com.wanted.preonboardingbackend.domain.jobPosting.entity.JobPosting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class JobPostingRepositoryTest {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @DisplayName("키워드로 채용공고를 조회한다.")
    @Test
    void findByKeyword() {
        // given
        Company company1 = companyRepository.save(createCompany("원티드랩", "한국", "서울"));
        Company company2 = companyRepository.save(createCompany("구글", "미국", "뉴욕"));

        jobPostingRepository.save(createJobPosting(
                company1, "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "자바, 스프링")
        );
        jobPostingRepository.save(createJobPosting(
                company1, "프론트엔드 개발자", 1000, "프론트엔드 개발자 채용합니다.", "자바스크립트")
        );
        jobPostingRepository.save(createJobPosting(
                company2, "풀스택 개발자", 3000, "풀스택 개발자 채용합니다.", "리액트, 스프링")
        );

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("id")));

        // when
        Page<JobPosting> jobPostings = jobPostingRepository.findByKeyword("스프링", pageable);

        // then
        assertThat(jobPostings).hasSize(2)
                .extracting("position", "reward", "skill")
                .containsExactlyInAnyOrder(
                        tuple("백엔드 개발자", 1500, "자바, 스프링"),
                        tuple("풀스택 개발자", 3000, "리액트, 스프링")
                );
    }

    @DisplayName("채용공고 ID로 해당 회사가 등록한 다른 채용공고 ID 리스트를 조회한다.")
    @Test
    void findOtherJobsById() {
        // given
        Company company = companyRepository.save(createCompany("원티드랩", "한국", "서울"));

        JobPosting jobPosting1 = jobPostingRepository.save(createJobPosting(
                company, "백엔드 개발자", 1500, "백엔드 개발자 채용합니다.", "자바, 스프링")
        );
        JobPosting jobPosting2 = jobPostingRepository.save(createJobPosting(
                company, "프론트엔드 개발자", 1000, "프론트엔드 개발자 채용합니다.", "자바스크립트")
        );
        JobPosting jobPosting3 = jobPostingRepository.save(createJobPosting(
                company, "풀스택 개발자", 3000, "풀스택 개발자 채용합니다.", "리액트, 스프링")
        );

        // when
        List<Long> ids = jobPostingRepository.findOtherJobsById(jobPosting1.getId(), company.getId());

        // then
        assertThat(ids).hasSize(2)
                .contains(jobPosting2.getId(), jobPosting3.getId());
    }

    private JobPosting createJobPosting(Company company, String position, int reward, String content, String skill) {
        return JobPosting.builder()
                .company(company)
                .position(position)
                .reward(reward)
                .content(content)
                .skill(skill)
                .build();
    }

    private Company createCompany(String name, String nation, String region) {
        return Company.builder()
                .name(name)
                .nation(nation)
                .region(region)
                .build();
    }
}