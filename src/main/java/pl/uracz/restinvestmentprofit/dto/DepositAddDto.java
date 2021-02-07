package pl.uracz.restinvestmentprofit.dto;

import lombok.*;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositAddDto {
    private String name;
    private double interest;
    private CapitalizationPeriod capitalizationPeriod;
    private String depositStartDate;
    private String depositEndDate;
}
