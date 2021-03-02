package pl.uracz.restinvestmentprofit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateValidation {
    String message() default "Cannot parse to proper date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
