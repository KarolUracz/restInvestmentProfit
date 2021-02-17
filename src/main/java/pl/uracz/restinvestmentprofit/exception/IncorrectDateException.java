package pl.uracz.restinvestmentprofit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Can't make partial calculation for future deposit")
public class IncorrectDateException extends RuntimeException{
    public IncorrectDateException(String message) {
        super(message);
    }
}
