`Hey, my name is Ricardo Elias and here is my Backend Software Engineer Challenge!
I'm using Java 17
`
***
## Build Instructions

    1. mvn clean install
    2. mvn clean package
    3. docker-compose up -d --build

**BEFORE TESTING**

    Make sure with "docker-compose logs -f calculator-rest" and "docker-compose logs -f calculator-core" that everything is ready

## NOW YOU CAN TEST

| Operation | Method | URL Example                             | Response Example   |
| --------- | ------ |-----------------------------------------| ------------------ |
| Sum       | GET    | `/localhost:8080/calculator/sum?a=1&b=2` | `{ "result": 3 }`  |
| Subtract  | GET    | `/localhost:8080/calculator/subtract?a=5&b=2`                     | `{ "result": 3 }`  |
| Multiply  | GET    | `/localhost:8080/calculator/multiply?a=3&b=4`                     | `{ "result": 12 }` |
| Divide    | GET    | `/localhost:8080/calculator/divide?a=10&b=2`                      | `{ "result": 5 }`  |

**NOTES**
1) You can test with some invalid parameters:
    - trying to divide by 0
    - using a letter instead of a number
    - omitting a required parameter
