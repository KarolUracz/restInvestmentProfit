package pl.uracz.restinvestmentprofit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DepositDatesValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DepositDates {
    String message() default "Wrong expiry date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
