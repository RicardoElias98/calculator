package test;

import serviceImp.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    void testSum() {
        BigDecimal result = calculatorService.sum(BigDecimal.valueOf(2), BigDecimal.valueOf(3));
        assertEquals(BigDecimal.valueOf(5), result);
    }

    @Test
    void testSubtract() {
        BigDecimal result = calculatorService.subtract(BigDecimal.valueOf(5), BigDecimal.valueOf(3));
        assertEquals(BigDecimal.valueOf(2), result);
    }

    @Test
    void testMultiply() {
        BigDecimal result = calculatorService.multiply(BigDecimal.valueOf(4), BigDecimal.valueOf(3));
        assertEquals(BigDecimal.valueOf(12), result);
    }

    @Test
    void testDivide() {
        BigDecimal result = calculatorService.divide(BigDecimal.valueOf(10), BigDecimal.valueOf(2));
        assertEquals(BigDecimal.valueOf(5).setScale(20), result);
    }

    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () ->
                calculatorService.divide(BigDecimal.ONE, BigDecimal.ZERO));
    }
}
