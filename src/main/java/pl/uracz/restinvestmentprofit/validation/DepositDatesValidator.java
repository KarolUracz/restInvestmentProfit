package pl.uracz.restinvestmentprofit.validation;

import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.temporal.ChronoUnit;

public class DepositDatesValidator implements ConstraintValidator<DepositDates, Deposit> {

    @Override
    public void initialize(DepositDates constraintAnnotation) {
    }

    @Override
    public boolean isValid(Deposit deposit, ConstraintValidatorContext constraintValidatorContext) {
        if (deposit.getDepositStartDate().isAfter(deposit.getDepositEndDate())) {
            return false;
        }
        CapitalizationPeriod capitalizationPeriod = deposit.getCapitalizationPeriod();
        int monthsToCapitalization = 0;
        switch (capitalizationPeriod) {
            case YEARLY:
                monthsToCapitalization = 12;
                break;
            case HALFYEARLY:
                monthsToCapitalization = 6;
                break;
            case QUARTERLY:
                monthsToCapitalization = 3;
                break;
            case MONTHLY:
                monthsToCapitalization = 1;
        }
        long between = ChronoUnit.MONTHS.between(deposit.getDepositStartDate(), deposit.getDepositEndDate());
        return (between%monthsToCapitalization == 0);
    }
}
