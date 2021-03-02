package pl.uracz.restinvestmentprofit.validation;

import pl.uracz.restinvestmentprofit.dto.DepositAddDto;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DepositDatesValidator implements ConstraintValidator<DepositDates, DepositAddDto> {

    @Override
    public void initialize(DepositDates constraintAnnotation) {
    }

    @Override
    public boolean isValid(DepositAddDto deposit, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate startDate = LocalDate.parse(deposit.getDepositStartDate());
        LocalDate endDate = LocalDate.parse(deposit.getDepositEndDate());
        if (startDate.isAfter(endDate)) {
            return false;
        }
        CapitalizationPeriod capitalizationPeriod = CapitalizationPeriod.valueOf(deposit.getCapitalizationPeriod());
        int monthsToCapitalization;
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
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + capitalizationPeriod);
        }
        long between = ChronoUnit.MONTHS.between(startDate, endDate);
        return (between%monthsToCapitalization == 0);
    }
}
