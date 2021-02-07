package pl.uracz.restinvestmentprofit.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.enums.CalculationAlgorithm;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class CalculationRepositoryTest {

    @Autowired
    CalculationRepository calculationRepository;

    @Autowired
    DepositRepository depositRepository;

    @Test
    void findAllByDepositId() {
        Deposit deposit = new Deposit();
        deposit.setId(1);
        deposit.setName("test");
        deposit.setInterest(0.05);
        deposit.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit.setDepositStartDate(LocalDate.now());
        deposit.setDepositEndDate(LocalDate.now().plusYears(2));
        depositRepository.save(deposit);
        Calculation calculation1 = new Calculation();
        calculation1.setDepositAmount(BigDecimal.valueOf(5000));
        calculation1.setCalculationDate(LocalDate.now());
        calculation1.setDeposit(deposit);
        calculation1.setCalculationAlgorithm(CalculationAlgorithm.FULLPERIOD);
        calculation1.setProfit(BigDecimal.valueOf(100));
        calculationRepository.save(calculation1);
        List<Calculation> allByDepositId = calculationRepository.findAllByDepositId(1);
        assertEquals(allByDepositId.size(), 1);
    }
}