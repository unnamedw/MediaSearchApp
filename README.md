## 서비스 설명


Daum 검색 API를 이용해 이미지 및 비디오를 검색할 수 있는 서비스입니다.


## 핵심기능


1. 검색
  - 비디오/이미지 두 목록을 합쳐 단일 리스트로 표시
  - 검색 히스토리
  - 페이징 처리 및 날짜순으로 정렬 (정렬은 한 paging 단위)

2. 즐겨찾기
  - 즐겨찾기 화면 내에서는 좋아요 활성/비활성화 시 목록 그대로 유지
  - 기본순, 날짜순 정렬 기능


## 계층구조


![architecture drawio (3)](https://user-images.githubusercontent.com/56429036/233818572-d3fcc7b2-b389-4d0c-b3da-385385516e4e.png)


기본적인 구조는 [권장 아키텍처](https://developer.android.com/topic/architecture?hl=ko) 를 따르고 있습니다.  


Ui Layer에서는 비즈니스 로직을 캡슐화한 경우엔 usecase를 참조하고 있으며, 이외의 경우 repository를 참조하는 형태입니다.  
Domain Layer에서는 다른 어떤 계층에 대해서도 직접적인 의존성을 가지지 않습니다. 또한 Android의 의존성을 가지는 것을 최대한 지양하였습니다.  
Data Layer에서는 Retrofit을 사용한 RemoteDataSource와 SharedPreferences를 사용한 LocalDataSource를 가집니다.  


Local Source의 경우 간단한 구현을 위해 SharedPreferences를 사용하였습니다.

