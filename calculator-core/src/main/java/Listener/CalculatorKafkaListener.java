package Listener;

import ServiceImp.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalculatorKafkaListener {

    @Autowired
    private CalculatorService calculatorService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "calculator-requests", groupId = "calculator-core-group")
    public void listen(String message) {
        try {
            String[] parts = message.split(",");
            String operation = parts[0];
            BigDecimal a = new BigDecimal(parts[1]);
            BigDecimal b = new BigDecimal(parts[2]);
            BigDecimal result = switch (operation.toLowerCase()) {
                case "sum" -> calculatorService.sum(a, b);
                case "subtract" -> calculatorService.subtract(a, b);
                case "multiply" -> calculatorService.multiply(a, b);
                case "divide" -> calculatorService.divide(a, b);
                default -> throw new IllegalArgumentException("Unknown operation: " + operation);
            };

            kafkaTemplate.send("calculator-responses", result.toPlainString());

        } catch (Exception e) {
            kafkaTemplate.send("calculator-responses", "ERROR: " + e.getMessage());
        }
    }
}
