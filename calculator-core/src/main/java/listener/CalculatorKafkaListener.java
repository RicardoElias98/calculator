package listener;

import serviceImp.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalculatorKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorKafkaListener.class);

    @Autowired
    private CalculatorService calculatorService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "calculator-requests", groupId = "calculator-core-group")
    public void listen(String message, @Header(name = "traceId", required = false) String traceId) {
        try {
            if (traceId != null) MDC.put("traceId", traceId);

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

            logger.info("Calculated {}: {} {} {} = {}", operation, a, operation, b, result);

            kafkaTemplate.send(
                    MessageBuilder.withPayload(result.toPlainString())
                            .setHeader(KafkaHeaders.TOPIC, "calculator-responses")
                            .setHeader("traceId", traceId)
                            .build()
            );
        } finally {
            MDC.clear();
        }
    }

}
