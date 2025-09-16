package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"app", "controller", "ServiceImp"})
public class CalculatorRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalculatorRestApplication.class, args);
    }
}
