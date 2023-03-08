Feature: login en servidor

  
  Scenario: login correcto como b
    Given driver baseUrl + '/login'
    And input('#username', 'igoford0')
    And input('#password', 'aa')
    When submit().click(".form-signin button")
    Then waitForUrl(baseUrl + '/user/3')
    Given driver baseUrl + '/search'
    When click(".widget-26-job-info > .tit")
    Then waitForUrl(baseUrl + '/event/1')
    And click("#check-join")
    When click("#acts-aft-join > a:first-child")
    Then waitForUrl(baseUrl + '/chat')
    And input('#textAreaExample2', 'Hello , i would like to join you to the trip please')
    And click("#env")