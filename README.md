# 코인 모의 투자
#### 실제 거래 환경과 유사하게 시세 기반으로 모의 투자할 수 있는 서비스입니다.  
실시간으로 매수/매도 주문을 등록하고 거래 흐름을 경험해 보세요. 
#### [사이트 바로가기](http://cmi.p-e.kr)

## 문제 해결과 개선

<details>
   <summary>내용 확인</summary>
<br>
	
## **주문/체결 데이터 관리 구조 개선**

### 문제점
- 초기에는 체결/미체결 데이터를 단일 테이블에서 관리
- 미체결 주문과 체결된 거래 내역이 혼재되어 데이터 관리의 복잡도 증가

### 해결 과정
- 데이터의 생명주기를 기준으로 테이블 분리
  - `order_book (호가창)`: 미체결 주문 관리
  - `trade_history (거래내역)`: 체결된 거래 내역 관리

<image src="https://github.com/user-attachments/assets/9fad3ef1-1457-4d7e-b519-ad5b3ec192fe">

두 테이블은 거의 동일한 컬럼들을 가지고 있지만 구조가 비슷해도 목적이 다르기 때문에 분리했다.  
호가창 테이블은 미체결 주문, 거래내역 테이블은 부분/완전 체결된 주문을 관리하도록 설계했다.  

- 두 테이블은 주문 상태에 따라 데이터 처리 방식이 다르다.  
> 주문할 때:  
> `order_book`에 주문 추가.
> 
> 완전 체결될 때:  
> `order_book`에서 해당 주문 삭제.  
> `trade_history`에서 해당 주문을 status가 'COMPLETE'인 새로운 레코드 저장.
> 
> 부분 체결할 때:  
> `order_book`에서 해당 주문의 수량을 업데이트.  
> `trade_history`에 해당 주문을 status가 'PARTIAL'인 새로운 레코드를 저장.
> 
> 취소할 때:  
> `order_book`에서 해당 주문 삭제.

각 테이블의 역할과 목적에 맞게 관리하도록 구현했다.

### 결과
- 데이터 관리 복잡도 감소
- 조회 성능 향상
- 명확한 데이터 구조로 유지보수성 향상

---
<br>

## **DB 락을 활용한 체결 로직 설계**

### 체결 로직
체결 로직은 다음과 같은 4가지 규칙에 의해서 처리된다.
- 가장 낮은 매도가와 가장 높은 매수가가 체결이 된다.
- 매수가 >= 매도가가 성립해야 체결이 된다.
- 같은 가격에 주문이 많은 경우, 먼저 들어온 주문부터 체결된다.
- 두 주문의 체결가는 먼저 들어온 호가(주문)의 가격으로 결정된다.
<br>

### 락 없이 체결 진행 시 발생할 문제
- 주문 체결 시 여러 사용자의 요청이 동시에 들어오며, 동일한 가격대에서 다수의 주문이 체결될 수 있다.  
- 이 과정에서 미체결 주문(공유 자원)에 대한 경쟁 조건이 발생하여, 실행 순서나 타이밍에 따라 체결 결과가 달라지는 문제가 생길 수 있다.  
- 이를 해결하기 위해 각 체결에 대해 배타적 잠금(X-Lock)을 적용하여 순차적인 접근을 보장하도록 설계했다.  
<br>

#### 새로 접수된 주문에 대해 기존 체결 처리 과정은 다음과 같다.
  1. 주문이 들어오면, 호가창 테이블에 저장한다.
  2. DB에서 해당 코인의 미체결 주문 전체(새로 접수된 주문까지 포함)를 락을 걸어 조회한다.
  3. 매도 주문들과 매수 주문들을 체결 로직 기준으로 정렬하고, 조건에 맞는 주문들은 처리가 된다.
<br>

### 문제점
- 접수될 때마다 미체결 주문 전체에 락을 걸기 때문에, 동시 주문 체결 속도 저하.
- 다수의 트랜잭션이 동시에 실행될 때(동시 주문), `SELECT ~ FOR UPDATE` 쿼리에 의해 데드락 발생.
  - 두 트랜잭션이 각각 새로운 주문을 삽입한 뒤,  
    동시에 서로의 주문 데이터와 미체결 주문에 락 요청을 하며 대기 상태 발생.  

<br>

&nbsp;&nbsp;&nbsp;다음 그림처럼 order_book에 새로운 주문이 동시에 접수될 때, 데드락이 발생한다.

<image src="https://github.com/user-attachments/assets/c557953f-80fe-4bd0-bac6-149de886ab1b">

### 해결 과정
  바뀐 체결 처리 과정은 다음과 같다.
  1. 주문이 접수되면, 주문 타입(매수/매도)에 따라 특정 범위만을 락을 걸어 조회한다.
  2. 새로 접수된 주문을 호가창에 저장하고, 접수된 주문과 조회한 주문들이 조건에 맞게 처리한다.
<br>

**개선점**
- 잠금 범위 최소화
- 쿼리 실행 순서 변경
  - `INSERT -> SELECT ~ FOR UPDATE` 순서를 `SELECT ~ FOR UPDATE -> INSERT` 로 변경


<br>

&nbsp;&nbsp;&nbsp;다음 그림처럼 order_book에 새로운 주문이 접수될 때,  
&nbsp;&nbsp;&nbsp;`INSERT` 이전에 `SELECT ~ FOR UPDATE`를 실행하여 필요한 레코드에 대해 락을 먼저 획득하도록 설계.

<image src="https://github.com/user-attachments/assets/71c59121-4a04-40c1-9c6c-c9dce29837fd">

### 결과
- 락 경쟁이 줄어들어 처리 속도 향상
- 데드락 문제 해결
- 안정적인 동시성 제어 달성

---
<br>

## **주문 처리 과정 개선**

### 문제점
- 단일 트랜잭션으로 모든 주문 처리의 모든 과정을 진행하여 성능 저하 발생
  - 처리 과정:  
     **주문 접수** → **체결**(주문 업데이트 및 삭제, 거래내역 저장) → **자산 업데이트** → **알림 저장** → **호가창 및 알림 SSE 전송**
<br>
<image src="https://github.com/user-attachments/assets/c8d64efa-37a5-4979-933d-06fc4b537f92">

- 단일 트랜잭션으로 모든 처리를 진행하여 성능 저하 발생
- 주문 처리 과정이 길어 응답 시간 증가
- 다수 사용자 동시 주문 시 성능 저하

### 해결 과정
- 스프링 이벤트를 활용하여 트랜잭션을 기능 단위로 분리.
  1. **주문 접수 → 체결 → 호가창 SSE 전송**: 주요 단계를 우선 처리
  2. **자산 상태 업데이트**: 체결 이후 보유 자산 변경
  3. **알림 처리**: 체결 결과를 저장 후 전송
  <br>
  <image src="https://github.com/user-attachments/assets/415681a6-c052-490e-ad8a-73f561df7de4">
### 결과
- 주문 처리 속도 약 40% 향상
- 기존 방식보다 유연하고 확장 가능한 구조 제공

</details>
<br>

## 화면 구성 및 기능

<details>
   <summary>내용 확인</summary>
<br>
	
|시세 정보|
|:-:|
|<image src="https://github.com/user-attachments/assets/79b3c135-d4bf-4acf-a8bb-e704fc52cb52">|
|<b>실시간으로 코인 시세 정보를 제공합니다.</b>|
<br>

|호가창과 주문|  
|:-:|
|<image src="https://github.com/user-attachments/assets/f8699443-20b6-4d5f-8303-8c4d6893074b">|
|<b>매수/매도 주문을 가상 호가창에 등록하여 실제 거래처럼 이용할 수 있습니다.</b>|
<br>

|투자 정보|
|:-:|
|<image src="https://github.com/user-attachments/assets/42061db3-f473-4237-8bb0-da65d2f2860b">|
|<b>보유자산, 거래내역, 미체결 주문을 확인할 수 있습니다.</b>|
<br>

|알림|
|:-:|
|<image src="https://github.com/user-attachments/assets/df3b6f5d-e72f-44f3-928c-aa974b8a87b1">|
|<b>주문 체결 알림을 받을 수 있습니다.</b>|

</details>
<br>

## 시스템 구조

<details>
   <summary>내용 확인</summary>
<br>
	
<image src="https://github.com/user-attachments/assets/b8ab3c4a-19d4-46c3-b612-9addfb7e09e4">

</details>
<br>

## ERD

<details>
   <summary>내용 확인</summary>
<br>
	
<image src="https://github.com/user-attachments/assets/5ad71697-2905-4669-a497-a33a399dd03c">

</details>
<br>

## 기술 스택
- 프론트엔드
	* React
- 백엔드
	* Spring Boot 
	* Spring Data JPA	
	* MySQL

---