package pl.uracz.restinvestmentprofit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
