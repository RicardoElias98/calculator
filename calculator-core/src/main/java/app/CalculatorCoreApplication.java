package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = {"ServiceImp", "Listener", "app"})
@EnableKafka
public class CalculatorCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalculatorCoreApplication.class, args);
    }
}
