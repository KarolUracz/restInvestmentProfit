package pl.uracz.restinvestmentprofit.entity;

import lombok.*;
import pl.uracz.restinvestmentprofit.enums.CalculationAlgorithm;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Calculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private BigDecimal depositAmount;
    private LocalDate calculationDate;
    @ManyToOne
    private Deposit deposit;
    @Enumerated(EnumType.STRING)
    private CalculationAlgorithm calculationAlgorithm;
    private BigDecimal profit;
}
