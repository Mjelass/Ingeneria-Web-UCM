Feature: login en servidor

  
  Scenario: login correcto como b
    Given driver baseUrl + '/login'
    And input('#username', 'igoford0')
    And input('#password', 'aa')
    When submit().click(".form-signin button")
    Then waitForUrl(baseUrl + '/user/3')
    Given driver baseUrl + '/search'
    When click(".results > div:first-child .title > a")
    Then waitForUrl(baseUrl + '/event/1')
    And click("#check-join")
    When click("#acts-aft-join > a:first-child")
    Then waitForUrl(baseUrl + '/chat')
    And click("#chat-b-1")
    And input('#input-msg-1', 'Hello , i would like to join you to the trip please')
    And click("#input-msg-1 + button")