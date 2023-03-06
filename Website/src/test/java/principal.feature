Feature: login en servidor

  
  Scenario: login correcto como b
    Given driver baseUrl + '/login'
    And input('#username', 'b')
    And input('#password', 'aa')
    When submit().click(".form-signin button")
    Then waitForUrl(baseUrl + '/user/2')
    Given driver baseUrl + '/search'
    When click("#ss")
    Then waitForUrl(baseUrl + '/event')
    And click("#check-join")
    When click("#ch")
    Then waitForUrl(baseUrl + '/chat')
    And input('#textAreaExample2', 'Hello , i would like to join you to the trip please')
    And click("#env")