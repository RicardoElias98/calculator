package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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
        logger.info("Sending request: operation={}, a={}, b={}", operation, a, b);
        kafkaTemplate.send("calculator-requests", operation + "," + a + "," + b);
        String result = responseQueue.take();
        logger.info("Received response: {}", result);
        return new BigDecimal(result);
    }

    @GetMapping("/sum")
    public Result sum(@RequestParam(name = "a") BigDecimal a,
                      @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        return new Result(sendRequest("sum", a, b));
    }

    @GetMapping("/subtract")
    public Result subtract(@RequestParam(name = "a") BigDecimal a,
                           @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        return new Result(sendRequest("subtract", a, b));
    }

    @GetMapping("/multiply")
    public Result multiply(@RequestParam(name = "a") BigDecimal a,
                           @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        return new Result(sendRequest("multiply", a, b));
    }

    @GetMapping("/divide")
    public Result divide(@RequestParam(name = "a") BigDecimal a,
                         @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        return new Result(sendRequest("divide", a, b));
    }

    @KafkaListener(topics = "calculator-responses", groupId = "calculator-group")
    public void listen(String message) {
        responseQueue.offer(message);
    }

    public static class Result {
        private BigDecimal result;

        public Result(BigDecimal result) {
            this.result = result;
        }

        public BigDecimal getResult() {
            return result;
        }

        public void setResult(BigDecimal result) {
            this.result = result;
        }
    }
}
