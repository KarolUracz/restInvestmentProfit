package pl.uracz.restinvestmentprofit.dto;

import lombok.*;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositCalculationsDto {
    private String depositName;
    private double depositInterest;
    private CapitalizationPeriod capitalizationPeriod;
    private LocalDate depositStartDate;
    private LocalDate depositEndDate;
    private List<CalculationDataDto> calculationDataDtos;
}
