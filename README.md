# Smart Memo

## 위치 기반 장소 메모 어플리케이션
----------------------------------------

### 1. 프로젝트 정의

GPS 기반 메모 어플리케이션

### 2. 프로젝트 배경

사람들은 해야 할 일, 가야할 곳 등 자신에게 필요한 정보를 잊지 않기 위해 메모한다. 하지만 메모 작성을 해놓고도 확인하지 않아 잊기 마련이다. 이러한 문제를 해결하기 위해 일반 메모 어플리케이션들은 시간 알림 서비스를 제공하는 경우가 있다. 하지만, 시간 알림은 시간이라는 제약 안에 놓여있기 때문에, 어떠한 장소에서 해야 할 일을 기억하게 도와줄 수는 없다.

이러한 경우에 편의성을 주고자 장소 알림 기능을 고안해보았다. 또한 메모의 양이 많아 질 경우, 무엇을 위한 메모였는지 기억을 못하는 경우가 생기는데, 이를 위해 작성위치 혹은 사용자가 원하는 위치가 함께 기록되면 좋지 않을까 라는 생각을 하게 되었다. 대개 사람들은 그 상황을 생각하며 기억을 되짚기 때문이다. 이러한 메모 관리를 위해 위치 별 메모 확인기능도 제공하고자 한다.

### 3. 프로젝트 기능

#### 일반 메모
   - 그룹 해시 값이 포함된 링크를 공유하고 싶은 구성원들에게 전송하여 하나의 그룹을 만들고, 구성원들 내에서 메모를 공유하도록 구현
   - 위치 별 메모 그룹 핑 후 메인 화면에 마커로 표시
#### TODO List
   - 일반 메모와 같은 방식으로 TODO LIST를 작성하여 구성원들 사이에서 공유하도록 구현
   - Map 또는 TODO LIST 탭에서 가야할 장소 등을 설정한 뒤, 사용자 위치 반경 내에 해당 장소가 들어오면 알림 제공
   - 장소 알림을 설정한 경우에 메인 화면에 마커로 표시
   - 사용자 지정 시간 헤드 업 알림 서비스
  
### 4. 담당 기능
- 메모, TODO LIST 기능 구현
- 전체적인 UI 설계 및 개발

### 5. 프로젝트 작동 모습
#### 로그인
![image](https://user-images.githubusercontent.com/55984573/113248939-dd766280-92f8-11eb-98e2-517690122593.png)
![image](https://user-images.githubusercontent.com/55984573/113248948-df402600-92f8-11eb-99b0-54dc0da8ffe1.png)

#### 메모
![image](https://user-images.githubusercontent.com/55984573/113248955-e1a28000-92f8-11eb-8dc7-4389d3073693.png)
![image](https://user-images.githubusercontent.com/55984573/113248961-e36c4380-92f8-11eb-9f45-9498fc11f00b.png)
![image](https://user-images.githubusercontent.com/55984573/113248969-e5360700-92f8-11eb-98fa-025f6443dcc9.png)

#### TODO
![image](https://user-images.githubusercontent.com/55984573/113248975-e830f780-92f8-11eb-8c20-72b21a5f6c8c.png)
![image](https://user-images.githubusercontent.com/55984573/113248980-e9fabb00-92f8-11eb-9c20-f0a5a43c0bbb.png)
![image](https://user-images.githubusercontent.com/55984573/113248987-ebc47e80-92f8-11eb-85c2-d7136ac357b5.png)

#### MAP
![image](https://user-images.githubusercontent.com/55984573/113249119-262e1b80-92f9-11eb-9e8c-27a848370e64.png)
![image](https://user-images.githubusercontent.com/55984573/113249124-27f7df00-92f9-11eb-8882-54dc29dad874.png)
![image](https://user-images.githubusercontent.com/55984573/113249127-29c1a280-92f9-11eb-8e02-bf9ff7ca8a96.png)

#### 장소알람설정
![image](https://user-images.githubusercontent.com/55984573/113249134-2cbc9300-92f9-11eb-8a02-022f4352ffd0.png)
![image](https://user-images.githubusercontent.com/55984573/113249136-2e865680-92f9-11eb-9665-d296bf70fb96.png)
![image](https://user-images.githubusercontent.com/55984573/113249141-30501a00-92f9-11eb-9f70-42629aa1fa65.png)

#### 알람
![image](https://user-images.githubusercontent.com/55984573/113249147-32b27400-92f9-11eb-84e5-65e44e2b9317.png)
![image](https://user-images.githubusercontent.com/55984573/113249156-347c3780-92f9-11eb-9ebf-bfc0fac171e6.png)
![image](https://user-images.githubusercontent.com/55984573/113249159-3645fb00-92f9-11eb-858d-7d7960523b85.png)

#### 회원 정보
![image](https://user-images.githubusercontent.com/55984573/113249163-37772800-92f9-11eb-8454-29a5e1a44b1a.png)
![image](https://user-images.githubusercontent.com/55984573/113249168-3940eb80-92f9-11eb-8434-05d001d71f05.png)

#### 멤버추가
![image](https://user-images.githubusercontent.com/55984573/113249176-3ba34580-92f9-11eb-8f6e-af3da4e9239c.png)
![image](https://user-images.githubusercontent.com/55984573/113249181-3d6d0900-92f9-11eb-8fa3-1fa188b001ec.png)
![image](https://user-images.githubusercontent.com/55984573/113249186-3e9e3600-92f9-11eb-93bf-0263822d5462.png)



### 6. 프로젝트 기술 스택

#### 언어
- Kotlin

#### 사용 기술
- Kakao Maps API
   
   앱 내에서 사용할 지도로 Kakao 지도를 이용
   
- Firebase Authentication
   
   유효한 사용자인지 판별할 도구
   
- Realtime Database
   
   사용자 데이터 변화를 실시간으로 업데이트하기 위해 사용
   
- Dynamic Link & Kakao Link
   
   앱 내 기능 중에 그룹 인원 초대기능을 구현하기 위해 사용






