package pl.uracz.restinvestmentprofit.dto;

import lombok.*;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculationDto {
    private BigDecimal depositAmount;
    private String calculationDate;
    private String depositName;
    private double depositInterest;
    private CapitalizationPeriod depositCapitalizationPeriod;
    private String depositStartDate;
    private String depositEndDate;
    private String calculationAlgorithm;
    private BigDecimal profit;
}
