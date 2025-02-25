Feature: Bank Account

  Scenario: Client deposits and withdraws money
    Given a client makes a deposit of 1000 on 10-01-2012
    And a client makes a deposit of 2000 on 13-01-2012
    And a client withdraws 500 on 14-01-2012
    When they print their bank statement
    Then they would see:
      | Date       | Amount | Balance |
      | 14/01/2012 | -500   | 2500    |
      | 13/01/2012 | 2000   | 3000    |
      | 10/01/2012 | 1000   | 1000    |
