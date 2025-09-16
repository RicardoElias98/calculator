package controller;

import java.math.BigDecimal;

import ServiceImp.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @GetMapping("/sum")
    public BigDecimal sum(@RequestParam(name = "a") BigDecimal a,
                          @RequestParam(name = "b") BigDecimal b) {
        return calculatorService.sum(a, b);
    }

    @GetMapping("/subtract")
    public BigDecimal subtract(@RequestParam(name = "a") BigDecimal a,
                               @RequestParam(name = "b") BigDecimal b) {
        System.out.println("in");
        return calculatorService.subtract(a, b);
    }

    @GetMapping("/multiply")
    public BigDecimal multiply(@RequestParam(name = "a") BigDecimal a,
                               @RequestParam(name = "b") BigDecimal b) {
        return calculatorService.multiply(a, b);
    }

    @GetMapping("/divide")
    public BigDecimal divide(@RequestParam(name = "a") BigDecimal a,
                             @RequestParam(name = "b") BigDecimal b) {
        return calculatorService.divide(a, b);
    }
}
