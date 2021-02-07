package pl.uracz.restinvestmentprofit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculationDataDto {
    private String depositAmount;
    private String calculationDate;
    private String calculationAlgorithm;
    private String profit;
}
