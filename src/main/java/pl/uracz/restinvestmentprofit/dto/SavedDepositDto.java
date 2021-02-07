package pl.uracz.restinvestmentprofit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavedDepositDto {
    private long id;
    private String depositName;
    private double depositInterest;
    private long depositDurationInDays;
}
