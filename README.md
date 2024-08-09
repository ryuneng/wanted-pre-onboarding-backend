# 🚩 원티드 프리온보딩 백엔드 인턴십 사전 과제

### 서비스 소개
- 본 서비스는 기업의 채용을 위한 웹 서비스입니다.
- 회사는 채용공고를 생성하고, 사용자는 원하는 채용에 지원할 수 있습니다.

<br>

## 1. 사용 기술
- 언어 및 프레임워크 : **Java & SpringBoot**
- ORM : **Spring Data JPA**
- DataBase : **MySQL**

<br>

## 2. 주요 기능
1. 회사는 **채용공고를 등록**합니다.
2. 회사는 **채용공고를 수정**합니다.
3. 회사는 **채용공고를 삭제**합니다.
4. 사용자는 **채용공고 목록을 확인**합니다.
5. 사용자는 키워드를 통해 **채용공고를 검색**합니다. `(선택사항)`
6. 사용자는 **채용 상세 페이지를 확인**합니다.
    - 채용내용이 추가적으로 담겨있습니다.
    - 해당 회사가 올린 다른 채용공고 목록이 포함됩니다. `(선택사항)`
7. 사용자는 **채용공고에 지원**합니다. `(선택사항)`
    - 사용자는 1회만 지원 가능합니다.
8. 사용자는 **지원내역 목록을 확인**합니다. `(추가구현)`

<br>

## 3. ERD
![원티드 프리온보딩 인턴십 ERD](https://github.com/user-attachments/assets/7f89e986-3b92-43b0-a7c6-b0ac0f724e0f)

<br>

## 4. API 명세
> API의 상세 내용은 <a href="https://github.com/ryuneng/wanted-pre-onboarding-backend/wiki/REST-API">🔍여기</a>를 클릭해주세요.

![원티드 프리온보딩 인턴십 API 명세](https://github.com/user-attachments/assets/55ef37af-57fe-4b0e-94da-ed3c4949f9eb)

<br>

## 5. 디렉토리 구조
- 직관적인 구조 파악과 관리를 위해 **도메인형 구조**를 채택하였습니다.
```
🗂️ src
├─ 🗂️ main
│  ├─ 🗂️ java
│  │  └─ 🗂️ com/wanted/preonboardingbackend
│  │     ├─ 📂 domain // 도메인형 구조
│  │     │  ├─ 📂 company
│  │     │  │  ├─ 📁 entity
│  │     │  │  └─ 📁 repository
│  │     │  ├─ 📂 jobApplication
│  │     │  │  ├─ 📁 controller
│  │     │  │  ├─ 📁 dto
│  │     │  │  ├─ 📁 entity
│  │     │  │  ├─ 📁 repository
│  │     │  │  └─ 📁 service
│  │     │  ├─ 📂 jobPosting
│  │     │  │  ├─ 📁 controller
│  │     │  │  ├─ 📁 dto
│  │     │  │  ├─ 📁 entity
│  │     │  │  ├─ 📁 repository
│  │     │  │  └─ 📁 service
│  │     │  └─ 📂 user
│  │     │     ├─ 📁 entity
│  │     │     └─ 📁 repository
│  │     ├─ 📂 global // 프로젝트 전반적으로 사용되는 클래스들로 구성된 패키지
│  │     │  ├─ 📁 config
│  │     │  ├─ 📁 controllerAdvice
│  │     │  ├─ 📁 dto
│  │     │  └─ 📁 enums
│  │     └─ 📄 WantedPreOnboardingBackendApplication
│  └─ 🗂️ resources
│     ├─ 📃 application.yml // 로컬용 프로파일과 테스트 실행 전용 프로파일 분리
│     └─ 📜 data.sql        // 회사 및 사용자 테이블의 데이터베이스 초기 데이터 설정을 위한 Script 파일
└─ 🗂️ test
   └─ 🗂️ java
      └─ 🗂️ com/wanted/preonboardingbackend
         └─ 📂 domain
            ├─ 📂 jobApplication
            │  ├─ 📁 controller
            │  ├─ 📁 repository
            │  └─ 📁 service
            └─ 📂 jobPosting
               ├─ 📁 controller
               ├─ 📁 repository
               └─ 📁 service
```

