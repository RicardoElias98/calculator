package controller;

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

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final BlockingQueue<String> responseQueue = new LinkedBlockingQueue<>();

    private BigDecimal sendRequest(String operation, BigDecimal a, BigDecimal b) throws InterruptedException {
        kafkaTemplate.send("calculator-requests", operation + "," + a + "," + b);
        String result = responseQueue.take();
        return new BigDecimal(result);
    }

    @GetMapping("/sum")
    public BigDecimal sum(@RequestParam(name = "a") BigDecimal a,
                          @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        return sendRequest("sum", a, b);
    }

    @GetMapping("/subtract")
    public BigDecimal subtract(@RequestParam(name = "a") BigDecimal a,
                               @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        return sendRequest("subtract", a, b);
    }

    @GetMapping("/multiply")
    public BigDecimal multiply(@RequestParam(name = "a") BigDecimal a,
                               @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        return sendRequest("multiply", a, b);
    }

    @GetMapping("/divide")
    public BigDecimal divide(@RequestParam(name = "a") BigDecimal a,
                             @RequestParam(name = "b") BigDecimal b) throws InterruptedException {
        return sendRequest("divide", a, b);
    }

    @KafkaListener(topics = "calculator-responses", groupId = "calculator-group")
    public void listen(String message) {
        responseQueue.offer(message);
    }
}
