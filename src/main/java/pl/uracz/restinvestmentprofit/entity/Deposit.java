package pl.uracz.restinvestmentprofit.entity;

import lombok.*;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double interest;
    @Enumerated (EnumType.STRING)
    private CapitalizationPeriod capitalizationPeriod;
    private LocalDate depositStartDate;
    private LocalDate depositEndDate;
}

