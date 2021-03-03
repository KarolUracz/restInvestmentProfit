package pl.uracz.restinvestmentprofit.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;
    private String requestMethod;

    public ApiError(HttpStatus status, String message, List<String> errors, String method) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.requestMethod = method;
    }

    public ApiError(HttpStatus status, String message, String error, String method) {
        super();
        this.status = status;
        this.message = message;
        this.requestMethod = method;
        errors = Collections.singletonList(error);
    }
}
