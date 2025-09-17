package ServiceImp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);
    private static final int DIVISION_SCALE = 20;

    public BigDecimal sum(BigDecimal a, BigDecimal b) {
        logger.info("SUM input: a={}, b={}", a, b);
        BigDecimal result = a.add(b);
        logger.info("SUM result: {}", result);
        return result;
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        logger.info("SUBTRACT input: a={}, b={}", a, b);
        BigDecimal result = a.subtract(b);
        logger.info("SUBTRACT result: {}", result);
        return result;
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        logger.info("MULTIPLY input: a={}, b={}", a, b);
        BigDecimal result = a.multiply(b);
        logger.info("MULTIPLY result: {}", result);
        return result;
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        logger.info("DIVIDE input: a={}, b={}", a, b);
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            logger.error("Division by zero attempted: a={}, b={}", a, b);
            throw new ArithmeticException("Division by zero is not possible");
        }
        BigDecimal result = a.divide(b, DIVISION_SCALE, RoundingMode.HALF_UP);
        logger.info("DIVIDE result: {}", result);
        return result;
    }
}
