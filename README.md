`Hey, my name is Ricardo Elias and here is my Backend Software Engineer Challenge
`
***
## Build Instructions

    From the root folder run: 
    mvn clean install

**Start Kafka**

    docker-compose up -d

**Start Core Module**

    In a separate terminal, run:

    1) cd calculator-core

    2) mvn spring-boot:run

**Start REST Module**

    In another terminal, run:
    
    1) cd calculator-rest
    
    2) mvn spring-boot:run

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
