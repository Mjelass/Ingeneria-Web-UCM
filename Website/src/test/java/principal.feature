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
    
