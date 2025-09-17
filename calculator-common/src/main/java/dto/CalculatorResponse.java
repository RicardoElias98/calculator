package dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculatorResponse {
    private BigDecimal result;
    private String error;

    public CalculatorResponse() {}

    public CalculatorResponse(BigDecimal result) {
        this.result = result;
    }

    public CalculatorResponse(BigDecimal result, String error) {
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
