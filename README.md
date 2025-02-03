# 📅 Schedule 관리 API
내일배움캠프 Spring 5기 일정 관리 API 프로젝트입니다.
<br>Spring Boot와 Swagger를 활용하여 일정 CRUD 기능을 제공합니다.
<br><br><br>


## 📚 목차
* [📌 프로젝트 설명](#프로젝트-설명)
* [📌 주요 기능](#주요-기능)
* [📌 사용 방법](#사용-방법)
* [📑 API 문서](#api-문서)
* [📑 ERD](#erd)
* [📂 파일 구조](#파일-구조)
* [📌사용된 기술](#사용된-기술)
* [📞 Contact](#contact)
<br><br><br>


## 📌프로젝트 설명
이 프로젝트는 **일정을 관리할 수 있는 API**를 구현한 것입니다.  
사용자는 일정을 **등록, 조회, 수정(반복 수정 포함), 삭제**할 수 있으며,  
Swagger UI를 통해 API를 쉽게 확인하고 테스트할 수 있습니다.
<br><br><br>

## 📌주요 기능
### ✅ 일정 관리 (CRUD)
- **일정 추가** (`POST /schedules`)  
  - 새로운 일정을 등록합니다.
- **일정 조회** (`GET /schedules`)  
  - 모든 일정을 조회합니다.
  - 작성자와 날짜의 필터링을 통해 특정 일정을 조회할 수 있습니다.
- **일정 수정 (전체 및 부분 수정)** (`PATCH /schedules/{id}`)  
  - 비밀번호를 입력하여 특정 일정의 전체 또는 일부 정보를 수정할 수 있습니다.
- **일정 삭제** (`DELETE /schedules/{id}`)  
  - 비밀번호를 입력하여 특정 일정을 삭제합니다.

<br>

### ✅ Swagger를 통한 API 문서 확인
Swagger를 통해 API를 쉽게 테스트할 수 있습니다.
<br> 
#### Swagger UI
http://localhost:8080/swagger-ui/index.html
<br> 
#### OpenAPI 문서 (JSON)
http://localhost:8080/v3/api-docs
<br><br><br>


## 📌사용 방법
### 기본 사용 방법
1. 애플리케이션 실행 (`Spring Boot`)
2. Swagger UI (`http://localhost:8080/swagger-ui/index.html`)에서 API 테스트
3. Postman 또는 REST 클라이언트로 API 호출 가능
4. MySQL 데이터베이스에서 일정 데이터를 관리
<br><br><br>

## 📑API 문서
### API 요약 테이블
```
+---------------+-------------------+---------------+
|  HTTP Method  |        URL        |      설명      |
+---------------+-------------------+---------------+
| `POST`        | `/schedules`      | 일정 추가       |
| `GET`         | `/schedules`      | 일정 전체 조회   |   
| `PATCH`       | `/schedules/{id}` | 일정 수정       |
| `DELETE`      | `/schedules/{id}` | 일정 삭제       |
+---------------+-------------------+---------------+
```

### 1️⃣ 일정 생성
`POST /schedules`
<br>Description: 새로운 일정을 생성합니다.
##### 요청
```json
{
  "author": "사용자 이름",
  "password": "비밀번호",
  "task": "할 일 내용"
}
```
##### 응답
```json
{
  "id": 1,
  "author": "사용자 이름",
  "task": "할 일 내용",
  "createdAt": "2024-02-01 10:00",
  "updatedAt": "2024-02-01 10:00"
}
```
<br>

### 2️⃣ 일정 조회
`GET /schedules`
<br> Description: 등록된 모든 일정을 조회합니다. id, updatedAt, author로 필터링 가능
##### 요청
`GET /schedules?updatedAt=2025-02-03`

##### 응답
```json
{
  "id": 1,
  "author": "사용자 이름",
  "task": "할 일",
  "createdAt": "2025-01-27 10:00",
  "updatedAt": "2025-02-03 13:15"
}
```
<br>

### 3️⃣ 일정 수정
`PATCH /schedules/{id}`
<br> Description: 일정 ID를 기준으로 특정 일정을 수정합니다. 작성자와 할 일을 수정할 수 있다.
#### 🔷 할 일 수정
##### 요청 
`PATCH /schedules/1`
```json
{
  "task": "수정된 할 일 내용"
  "password": "비밀번호",
}
```
##### 응답
```json
{
  "id": 1,
  "author": "사용자 이름",
  "task": "수정된 할 일 내용",
  "createdAt": "2024-02-01 10:00",
  "updatedAt": "2024-02-01 12:00"
}
```
<br>

#### 🔷 작성자 수정
##### 요청
```json
{
  "author": "수정된 사용자 이름",
  "password": "비밀번호",
}
```
##### 응답
```json
{
  "id": 1,
  "author": "수정된 사용자 이름",
  "task": "수정된 할 일 내용",
  "createdAt": "2024-02-01 10:00",
  "updatedAt": "2024-02-01 13:00"
}
```
<br>

### 4️⃣ 일정 삭제 
`DELETE /schedules/{id}`
##### 요청
```json
  "password": "비밀번호",
```
##### 응답
200 OK
<br><br><br>

## 📑ERD
```
+-------------+--------------+------+-------------------+----------------+
| Field       | Type         | Key  | Default           | Extra          |
+-------------+--------------+------+-------------------+----------------+
| id          | BIGINT       |  PK  | NULL              | AUTO_INCREMENT |
| author      | VARCHAR(100) |      | NULL              |                |
| password    | VARCHAR(100) |      | NULL              |                |
| task        | VARCHAR(255) |      | NULL              |                |
| createdDate | DATETIME     |      | CURRENT_TIMESTAMP |                |
| updatedDate | DATETIME     |      | CURRENT_TIMESTAMP |                |
+-------------+--------------+------+-------------------+----------------+
```
<br><br><br>

## 📂파일 구조

```
├── src/main/java/com/example/scheduleProject
│   ├── controller
│   │     ├── ScheduleController.java  # 일정 API 컨트롤러
│   ├── dto
│   │     ├── ScheduleRequestDto.java  # 일정 요청 DTO
│   │     ├── ScheduleResponseDto.java # 일정 응답 DTO
│   ├── entity
│   │     ├── Schedule.java            # 일정 엔티티
│   ├── repository
│   │     ├── ScheduleRepository.java  # 일정 데이터베이스 인터페이스
│   │     ├── JdbcTemplateScheduleRepository.java # JDBC 구현체
│   ├── service
│   │     ├── ScheduleService.java     # 일정 서비스 로직 인터페이스
│   │     ├── ScheduleServiceImpl.java # 일정 서비스 로직 구현
│   ├── config
│   │     ├── SwaggerConfig.java       # Swagger 설정
│   ├── ScheduleApplication.java       # 프로젝트 시작 파일
```
<br><br><br>

## 📌사용된 기술
<div align=center> 
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"> 
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> 
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>

<br><br><br>

## 📞Contact
프로젝트 관련 문의는 [@Seoyeon](https://github.com/MythologyDevSeoyeon)으로 연락주세요
<br><br><br>
