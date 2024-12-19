## 1. 프로젝트 제목
 - 주특기 플러스 주차, 장비 대여 프로그램 실습

## 2. 프로젝트 설명
 - 사용자가 장비를 대여하고, 관련한 정보를 저장하는 프로그램

## 3. 프로젝트 설치 및 실행 방법
 - DB : mysql(환경변수 설정 필요)
 - build.gradle : Java Version(21), implements는 내부 파일 확인

## 4. 프로젝트 사용법
- 사용자 회원가입(@PostMapping "/users")
- ![image](https://github.com/user-attachments/assets/8ebd30a1-beab-4427-bde6-3842fc3ee5d6) 
 
- 사용자 로그인(@PostMapping "/users/login")
- ![image](https://github.com/user-attachments/assets/9e85a7c4-b719-4d7c-95ad-053c63fdb9e0)

- 사용자 로그아웃(@PostMapping "/users/logout")
- 200 OK

- 대여 아이템 생성(@PostMapping "/items")
- ![image](https://github.com/user-attachments/assets/48464bc1-288e-45b2-beda-9694e918b9f0)

- 아이템 대여 예약 생성(@PostMapping "/reservations")
- ![image](https://github.com/user-attachments/assets/8c0a3e6c-4f9d-4dd7-9d1c-b068a8b2a76f)

- 대여한 아이템 상태값 변경(@PatchMapping "/reservations/itemId/update-status?status='변경값')
- ![image](https://github.com/user-attachments/assets/055d6e0b-b4bb-42bd-b642-3ebc417ac1e8)

- 사용자 신고 기능(@PostMapping "/admins/report-users")
- ![image](https://github.com/user-attachments/assets/87425466-25e3-4d26-95ab-5337134f2e2e)

