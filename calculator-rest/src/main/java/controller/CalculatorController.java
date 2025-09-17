package controller;

import dto.CalculatorRequest;
import dto.CalculatorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final BlockingQueue<CalculatorResponse> responseQueue = new LinkedBlockingQueue<>();

    private CalculatorResponse sendRequest(String operation, BigDecimal a, BigDecimal b) throws InterruptedException {
        String traceId = MDC.get("traceId");
        CalculatorRequest request = new CalculatorRequest(operation, a, b);

        kafkaTemplate.send(
                MessageBuilder.withPayload(request)
                        .setHeader(KafkaHeaders.TOPIC, "calculator-requests")
                        .setHeader("traceId", traceId)
                        .build()
        );

        CalculatorResponse response = responseQueue.take();
        logger.info("Received response: result={} error={}", response.getResult(), response.getError());
        return response;
    }

    @GetMapping("/sum")
    public CalculatorResponse sum(@RequestParam(name = "a") BigDecimal a,
                                  @RequestParam(name = "b") BigDecimal b) {
        try {
            return sendRequest("sum", a, b);
        } catch (Exception e) {
            logger.error("Error in sum request: a={}, b={}", a, b);
            return new CalculatorResponse(null, e.getMessage());
        }
    }

    @GetMapping("/subtract")
    public CalculatorResponse subtract(@RequestParam(name = "a") BigDecimal a,
                                       @RequestParam(name = "b") BigDecimal b) {
        try {
            return sendRequest("subtract", a, b);
        } catch (Exception e) {
            logger.error("Error in subtract request: a={}, b={}", a, b);
            return new CalculatorResponse(null, e.getMessage());
        }
    }

    @GetMapping("/multiply")
    public CalculatorResponse multiply(@RequestParam(name = "a") BigDecimal a,
                                       @RequestParam(name = "b") BigDecimal b) {
        try {
            return sendRequest("multiply", a, b);
        } catch (Exception e) {
            logger.error("Error in multiply request: a={}, b={}", a, b);
            return new CalculatorResponse(null, e.getMessage());
        }
    }

    @GetMapping("/divide")
    public CalculatorResponse divide(@RequestParam(name = "a") BigDecimal a,
                                     @RequestParam(name = "b") BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            logger.error("Division by zero requested: a={}, b={}", a, b);
            return new CalculatorResponse(null, "Division by zero is not possible");
        }
        try {
            return sendRequest("divide", a, b);
        } catch (Exception e) {
            logger.error("Error in divide request: a={}, b={}", a, b);
            return new CalculatorResponse(null, e.getMessage());
        }
    }

    @KafkaListener(topics = "calculator-responses", groupId = "calculator-group")
    public void listen(CalculatorResponse response) {
        responseQueue.offer(response);
    }
}
