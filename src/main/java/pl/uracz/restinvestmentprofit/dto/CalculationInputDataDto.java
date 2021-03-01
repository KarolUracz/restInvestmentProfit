package pl.uracz.restinvestmentprofit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculationInputDataDto {
    private String depositAmount;
    private String algorithm;
}
