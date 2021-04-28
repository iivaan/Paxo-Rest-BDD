Feature: Strapi API functionality - 1
  This explain how Strapi api functionality will work
  #1 . Only valid user with valid password can login
  #2 . No password is a valid entry

  @Debug
  Scenario: 1-Get all restaurants
    Given contentType is "application/json"
    And acceptType is "application/json"
    When we do get with the endpoint "/restaurants"
    Then verify statusCode is "200"
    And body with path "[0]" hasKey "id"
    And body with path "[0]" hasKey "name"
    And body with path "[0]" hasKey "description"
    And body with path "[0]" hasKey "created_at"
    And body with path "[0]" hasKey "updated_at"
    And body with path "[0]" hasKey "categories"
    And body with path "[0].categories.size()" is greaterThan "0"
    And body with path "[0].categories[0]" hasKey "id"
    And body with path "[0].categories[0]" hasKey "name"
    And body with path "[0].categories[0]" hasKey "created_at"
    And body with path "[0].categories[0]" hasKey "updated_at"

  @smoke
  Scenario: 2-Get all restaurants
    Given contentType is "application/json"
    And acceptType is "application/json"
    When we do get with the endpoint "/restaurants"
    Then verify statusCode is "200"
    And body with path hasKeys
      |[0]                | id            |
      |[0]                | name          |
      |[0]                | description   |
      |[0]                | created_at    |
      |[0]                | updated_at    |
      |[0]                | categories    |
      |[0].categories[0]  | id            |
      |[0].categories[0]  | name          |
      |[0].categories[0]  | created_at    |
      |[0].categories[0]  | updated_at    |

  Scenario: 3-Get all restaurants
    Given contentType is "application/json"
    And acceptType is "application/json"
    When we do get with the endpoint "/restaurants"
    Then verify statusCode is "200"
    And body with path "[0].id" has int value equal to "2"
    And body with path "[0].name" has value equal to "Swing the Teapot"
    And body with path "[0].description" has value equal to "Vegetarian Friendly, Vegan Options, Gluten Free Options"
    And body with path "[0].categories[0].id" has int value equal to "1"
    And body with path "[0].categories[0].name" has value equal to "Fast food"

  Scenario: 4-Get all restaurants
    Given contentType is "application/json"
    And acceptType is "application/json"
    When we do get with the endpoint "/restaurants"
    Then verify statusCode is "200"
    And body with path has int values equal to
      |[0].id                   | 2       |
      |[0].categories[0].id     | 1       |
    And body with path has values equal to
      |[0].name                 | Swing the Teapot                                        |
      |[0].description          | Vegetarian Friendly, Vegan Options, Gluten Free Options |
      |[0].categories[0].name   | Fast food                                               |

  Scenario: 5-Get all restaurants
    Given contentType is "application/json"
    And acceptType is "application/json"
    When we do get with the endpoint "/restaurants"
    Then verify statusCode is "200"
    And body with path "[0].id" has value "2"
    And body with path "[0].name" has value "Swing the Teapot"
    And body with path "[0].id" has value "#Number"
    And body with path "[0].name" has value "#String"
    And body with path "[0].description" has value "#NotNull"
    And body with path "[0].description" has value "#RegEx#Vegetarian.*"
    And body with path "[0].descriptionx" has value "#Null"
    And body with path "[0].created_at" has value "#DateTime"
    And body with path "[0].updated_at" has value "#DateTime#yyyy-MM-dd'T'HH:mm:ss.SSS"
    And body with path "[0].categories" has value "#Array"
    And body with path "[0].categories[0]" has value "#Object"

  Scenario: 6-Get all restaurants
    Given contentType is "application/json"
    And acceptType is "application/json"
    When we do get with the endpoint "/restaurants"
    Then verify statusCode is "200"
    And body with path "[0].categories[0]" has value
    """
      {
          "id": 1,
          "name": "Fast food",
          "created_at": "2019-05-12T22:22:40.000Z",
          "updated_at": "2019-05-12T22:22:40.000Z"
      }
    """
    And body with path "[0].categories" has value
    """
    [
          {
              "id": 1,
              "name": "Fast food",
              "created_at": "2019-05-12T22:22:40.000Z",
              "updated_at": "2019-05-12T22:22:40.000Z"
          },
          {
              "id": 3,
              "name": "Family style",
              "created_at": "2019-05-12T22:23:16.000Z",
              "updated_at": "2019-05-12T22:23:16.000Z"
          },
          {
              "id": 96,
              "name": "NewName",
              "created_at": "2021-01-30T13:21:13.000Z",
              "updated_at": "2021-01-30T13:49:26.000Z"
          }
    ]
    """

  Scenario: 7-Get all restaurants
    Given contentType is "application/json"
    And acceptType is "application/json"
    When we do get with the endpoint "/restaurants"
    Then verify statusCode is "200"
    And body with path "[0].categories[0]" has value
    """
      {
          "id": "#Number",
          "name": "#String",
          "created_at": "#DateTime",
          "updated_at": "#DateTime#yyyy-MM-dd'T'HH:mm:ss.SSS"
      }
    """
    And body with path "[0].categories" has value
    """
    [
          {
              "id": "#Number",
              "name": "Fast food",
              "created_at": "#DateTime",
              "updated_at": "#DateTime"
          },
          {
              "id": "#Number",
              "name": "Family style",
              "created_at": "#DateTime",
              "updated_at": "#DateTime"
          },
          {
              "id": "#Number",
              "name": "#RegEx#.*",
              "created_at": "#DateTime",
              "updated_at": "#DateTime"
          }
    ]
    """