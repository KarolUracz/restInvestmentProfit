package pl.uracz.restinvestmentprofit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CapitalizationPeriodValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CapitalizationPeriodValidation {
    Class<? extends Enum<?>> enumClass();
    String message() default "Not proper capitalization period";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
