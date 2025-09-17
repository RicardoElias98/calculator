package listener;

import dto.CalculatorRequest;
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
    public void listen(CalculatorRequest request, @Header(name = "traceId", required = false) String traceId) {
        try {
            if (traceId != null) MDC.put("traceId", traceId);

            BigDecimal result = switch (request.getOperation().toLowerCase()) {
                case "sum" -> calculatorService.sum(request.getA(), request.getB());
                case "subtract" -> calculatorService.subtract(request.getA(), request.getB());
                case "multiply" -> calculatorService.multiply(request.getA(), request.getB());
                case "divide" -> calculatorService.divide(request.getA(), request.getB());
                default -> throw new IllegalArgumentException("Unknown operation: " + request.getOperation());
            };

            logger.info("Calculated {}: {} {} {} = {}", request.getOperation(),
                    request.getA(), request.getOperation(), request.getB(), result);

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
