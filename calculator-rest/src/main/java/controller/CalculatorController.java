package controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import dto.CalculatorRequest;
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
    private KafkaTemplate<String, String> kafkaTemplate;

    private final BlockingQueue<String> responseQueue = new LinkedBlockingQueue<>();

    private BigDecimal sendRequest(String operation, BigDecimal a, BigDecimal b) throws InterruptedException {
        String traceId = MDC.get("traceId");
        CalculatorRequest request = new CalculatorRequest(operation, a, b);

        kafkaTemplate.send(
                MessageBuilder.withPayload(request)
                        .setHeader(KafkaHeaders.TOPIC, "calculator-requests")
                        .setHeader("traceId", traceId)
                        .build()
        );

        String result = responseQueue.take();
        logger.info("Received response: {}", result);
        return new BigDecimal(result);
    }

    @GetMapping("/sum")
    public Result sum(@RequestParam(name = "a") BigDecimal a,
                      @RequestParam(name = "b") BigDecimal b) {
        try {
            return new Result(sendRequest("sum", a, b));
        } catch (Exception e) {
            logger.error("Error in sum request: a={}, b={}", a, b);
            return new Result(null,e.getMessage());
        }
    }

    @GetMapping("/subtract")
    public Result subtract(@RequestParam(name = "a") BigDecimal a,
                           @RequestParam(name = "b") BigDecimal b) {
        try {
            return new Result(sendRequest("subtract", a, b));
        } catch (Exception e) {
            logger.error("Error in substract request: a={}, b={}", a, b);
            return new Result(null,e.getMessage());
        }
    }

    @GetMapping("/multiply")
    public Result multiply(@RequestParam(name = "a") BigDecimal a,
                           @RequestParam(name = "b") BigDecimal b) {
        try {
            return new Result(sendRequest("multiply", a, b));
        } catch (Exception e) {
            logger.error("Error in multiply request: a={}, b={}", a, b);
            return new Result(null,e.getMessage());
        }
    }

    @GetMapping("/divide")
    public Result divide(@RequestParam(name = "a") BigDecimal a,
                         @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            logger.error("Division by zero requested: a={}, b={}", a, b);
            return new Result(null, "Division by zero is not possible");
        }
        BigDecimal result = sendRequest("divide", a, b);
        return new Result(result);
    }

    @KafkaListener(topics = "calculator-responses", groupId = "calculator-group")
    public void listen(String message) {
        responseQueue.offer(message);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Result {
        private BigDecimal result;
        private String error;

        public Result(BigDecimal result) {
            this.result = result;
        }

        public Result(BigDecimal result, String error) {
            this.result = result;
            this.error = error;
        }

        public BigDecimal getResult() {
            return result;
        }

        public void setResult(BigDecimal result) {
            this.result = result;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
