package dto;

import java.math.BigDecimal;

public class CalculatorRequest {
    private String operation;
    private BigDecimal a;
    private BigDecimal b;

    public CalculatorRequest() {}

    public CalculatorRequest(String operation, BigDecimal a, BigDecimal b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public BigDecimal getA() {
        return a;
    }

    public void setA(BigDecimal a) {
        this.a = a;
    }

    public BigDecimal getB() {
        return b;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }
}
