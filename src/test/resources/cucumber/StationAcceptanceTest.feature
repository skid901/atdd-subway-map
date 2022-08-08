Feature: 지하철 역 API
  지하철 역 CRUD API 를 검증한다

  Scenario: 지하철 역 목록을 조회한다
    Given 지하철 역 강남역 생성을 요청한다
    Given 지하철 역 역삼역 생성을 요청한다
    Then 지하철 역 목록을 요청하면, 강남역, 역삼역 을 응답한다
