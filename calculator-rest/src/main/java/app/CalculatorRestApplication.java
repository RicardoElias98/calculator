package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"controller", "app", "config","ExceptionHandler"})
public class CalculatorRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalculatorRestApplication.class, args);
    }
}
