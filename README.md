# 📖 프로젝트 소개
##  `백엔드 직무 과제`

## Spring Security, JWT 인증/인가 기능을 구현하고, AWS EC2에 배포까지 수행하는 과제입니다.
## 모든 데이터는 메모리 상에서 처리됩니다.


## 📅 프로젝트 기간
- **진행 기간**: 2025년 05월 12일 ~ 2025년 05월 15일

## ⚙️ 기술 스택
| 기술                        | 
|---------------------------|
| **Java 17**               |
| **Spring Boot 3.4.4**     |
| **Spring Security**       |
| **JWT**                   |
| **Swagger** **, Postman** |
| **AWS EC2**               |

## 🔗 **URL**
### http://52.79.191.28:8080/swagger-ui/index.html

### 📌 참고사항
![참고사항](https://github.com/user-attachments/assets/0ae5ae9a-d68a-406c-8679-351fc39cf99e)
### 로그인시 발급되는 토큰을 복사하여 Authorize를 클릭하여 위와 같은 창에 복사한 토큰을 붙혀 넣어서 테스트 할수있습니다



## 🛠️ 주요 기능

### ✅ 사용자 인증 및 인가
- 회원가입
- 로그인 → JWT 토큰 발급
- 토큰을 통한 인증 및 권한 확인
- 사용자/관리자 역할 기반 API 접근 제어

### 🚨 예외 응답 처리
- 모든 예외는 통일된 JSON 형태로 반환
```json
{
  "error": {
    "code": "USER_ALREADY_EXISTS",
    "message": "이미 가입된 사용자입니다."
  }
}
```



