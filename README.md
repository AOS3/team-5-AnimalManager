# team-5-AnimalManager 🐾 동물 관리 시스템

## 📌 프로젝트 개요
이 프로젝트는 동물 데이터를 효율적으로 관리하기 위한 Android 애플리케이션입니다.  
동물 정보를 등록, 조회, 수정, 삭제할 수 있는 기능을 가집니다.
본 실습은 Fragment 간의 전환, Jetpack RoomDB를 활용하고 학습하는데 목적을 두었습니다.

---

## 📅 프로젝트 기간
2024.11.14 - 2024.11.18 (5일)

---

## 👥 팀 구성
- **박상원 (L)**
- **나희진**
- **정지석**
- **정지은**

---

## 🌟 주요 기능
### 1. 동물 정보 관리 💾
- 새로운 동물 등록
- 동물 정보 조회
- 동물 정보 수정
- 동물 정보 삭제

### 2. 데이터 검증 ✅
- 필수 입력 필드 검증
- 사용자 입력 데이터 검증
- 오류 메시지 표시

### 3. 데이터 저장 관리 💽
- Room 데이터베이스를 통한 로컬 저장
- 비동기 데이터 처리
- 안정적인 데이터 관리

---

## 📱 주요 화면
### 메인 화면
- 등록된 동물 목록 표시  
- FAB(Floating Action Button)을 통한 동물 추가 기능  
- 빈 화면 처리  

### 입력 화면
- 동물 정보 입력 폼  
- 필수 입력값 검증  
- 다양한 입력 옵션 (종류, 성별, 선호 간식)  

### 상세 화면
- 개별 동물 상세 정보 표시  
- 수정 및 삭제 기능  
- 이전 화면 이동  

### 수정 화면
- 기존 정보 수정 기능  
- 데이터 유효성 검증  
- 변경사항 저장 전 확인 다이얼로그  

---

## 📁 패키지 구조
~~~
├── dao/
│   ├── AnimalDAO
│   └── AnimalDatabase
├── fragment/
│   ├── InputFragment
│   ├── MainFragment
│   ├── ModifyFragment
│   └── ShowFragment
├── repository/
│   └── AnimalRepository
├── util/
│   ├── AnimalFood
│   ├── AnimalGender
│   ├── AnimalType
│   └── FragmentName
├── viewmodel/
│   └── AnimalViewModel
├── vo/
│   └── AnimalVO
└── MainActivity
~~~

### 1. 🏗 메인 구성요소
- `MainActivity`: 앱의 메인 화면과 프래그먼트 관리  
  - 프래그먼트 전환 및 백스택 관리  

### 2. 📑 데이터 접근 및 데이터베이스
- `AnimalDAO`: Room 데이터베이스 접근을 위한 인터페이스  
  - CRUD 연산 메서드 정의  
- `AnimalDatabase`: Room 데이터베이스 설정
  
### 3. 🖼 화면 구성
- `MainFragment`: 동물 목록 표시  
- `InputFragment`: 새로운 동물 정보 입력  
- `ShowFragment`: 개별 동물 정보 상세 표시  
- `ModifyFragment`: 동물 정보 수정  

### 4. 📦 데이터 처리
- `AnimalRepository`: 데이터 처리 로직 캡슐화  
  - View와 Room Database 사이의 중개자 역할  
  - 코루틴을 사용한 비동기 데이터 처리  

### 5. 🛠 유틸리티
- `FragmentName`: 프래그먼트 식별자 열거형  
- `AnimalType`: 동물 종류 (강아지, 고양이, 앵무새)  
- `AnimalGender`: 동물 성별 (수컷, 암컷)  
- `AnimalFood`: 선호하는 간식 종류  

### 6. 📊 데이터 모델
- `AnimalViewModel`: UI 표시를 위한 데이터 모델  
- `AnimalVO`: Room 데이터베이스 엔티티  

---

## 🛠 사용 기술
- **Kotlin**
- **Room Database**  
- **Coroutines**
- **Material Design 3** 
- **View Binding**
