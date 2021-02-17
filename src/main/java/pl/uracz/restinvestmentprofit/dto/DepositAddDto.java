package pl.uracz.restinvestmentprofit.dto;

import lombok.*;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;
import pl.uracz.restinvestmentprofit.validation.DepositDates;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DepositDates
public class DepositAddDto {
    private String name;
    private double interest;
    private CapitalizationPeriod capitalizationPeriod;
    private String depositStartDate;
    private String depositEndDate;
}
