package pl.uracz.restinvestmentprofit.dto;

import lombok.*;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;
import pl.uracz.restinvestmentprofit.validation.CapitalizationPeriodValidation;
import pl.uracz.restinvestmentprofit.validation.DateValidation;
import pl.uracz.restinvestmentprofit.validation.DepositDates;

import javax.validation.GroupSequence;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@GroupSequence({CapitalizationPeriod.class, DateValidation.class, DepositDates.class, DepositAddDto.class})
@DepositDates(groups = DepositDates.class)
public class DepositAddDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Min(0)
    @Max(100)
    private double interest;
    @CapitalizationPeriodValidation(enumClass = CapitalizationPeriod.class, groups = CapitalizationPeriod.class)
    private String capitalizationPeriod;
    @DateValidation(groups = DateValidation.class)
    private String depositStartDate;
    @DateValidation(groups = DateValidation.class)
    private String depositEndDate;
}
