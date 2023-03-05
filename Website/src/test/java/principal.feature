Feature: login en servidor

  @login_b
  Scenario: login correcto como b
    Given driver baseUrl + '/login'
    And input('#username', 'b')
    And input('#password', 'aa')
    When submit().click(".form-signin button")
    Then waitForUrl(baseUrl + '/user/2')