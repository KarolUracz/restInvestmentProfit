package pl.uracz.restinvestmentprofit.service.impl;

import org.junit.jupiter.api.Test;
import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.enums.CalculationAlgorithm;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;
import pl.uracz.restinvestmentprofit.repository.CalculationRepository;
import pl.uracz.restinvestmentprofit.service.CalculationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalculationServiceImplTest {

    private CalculationRepository calculationRepository;
    private CalculationService calculationService;

    @Test
    void shouldFindAllByDepositId() {
        calculationRepository = mock(CalculationRepository.class);
        calculationService = new CalculationServiceImpl(calculationRepository);
        Calculation calculation1 = new Calculation();
        calculation1.setId(1);
        calculation1.setDepositAmount(BigDecimal.valueOf(5000));
        Deposit deposit1 = new Deposit();
        deposit1.setId(1);
        deposit1.setName("test1");
        deposit1.setInterest(0.03);
        deposit1.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit1.setDepositStartDate(LocalDate.now());
        deposit1.setDepositEndDate(LocalDate.now().plusYears(2));
        calculation1.setDeposit(deposit1);
        calculation1.setCalculationDate(LocalDate.parse("2020-10-10"));
        calculation1.setCalculationAlgorithm(CalculationAlgorithm.FULLPERIOD);
        calculation1.setProfit(BigDecimal.valueOf(300));

        Calculation calculation2 = new Calculation();
        calculation2.setId(2);
        calculation2.setDepositAmount(BigDecimal.valueOf(3000));
        Deposit deposit2 = new Deposit();
        deposit2.setId(2);
        deposit2.setName("test2");
        deposit2.setInterest(0.03);
        deposit2.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit2.setDepositStartDate(LocalDate.now());
        deposit2.setDepositEndDate(LocalDate.now().plusYears(2));
        calculation2.setDeposit(deposit2);
        calculation2.setCalculationDate(LocalDate.parse("2019-10-10"));
        calculation2.setCalculationAlgorithm(CalculationAlgorithm.FULLPERIOD);
        calculation2.setProfit(BigDecimal.valueOf(200));

        List<Calculation> calculations = new ArrayList<>();
        calculations.add(calculation2);

        when(calculationRepository.findAllByDepositId(2)).thenReturn(calculations);

        List<Calculation> allByDepositId = calculationService.findAllByDepositId(2);

        assertEquals(1, allByDepositId.size());
        assertEquals(2, calculation2.getId());

    }

    @Test
    void shouldSaveCalculation() {
        calculationRepository = mock(CalculationRepository.class);
        calculationService = new CalculationServiceImpl(calculationRepository);
        Deposit deposit1 = new Deposit();
        deposit1.setId(1);
        deposit1.setName("test1");
        deposit1.setInterest(0.03);
        deposit1.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit1.setDepositStartDate(LocalDate.parse("2019-01-01"));
        deposit1.setDepositEndDate(LocalDate.parse("2019-01-01").plusYears(2));

        String depositAmount = "5000";
        String algorithm = "FULLPERIOD";

        Calculation calculation = new Calculation();
        calculation.setId(1);
        calculation.setCalculationDate(LocalDate.parse("2020-02-01"));
        calculation.setCalculationAlgorithm(CalculationAlgorithm.valueOf(algorithm));
        calculation.setDeposit(deposit1);
        calculation.setDepositAmount(new BigDecimal(depositAmount));
        calculation.setProfit(BigDecimal.valueOf(300));

        when(calculationRepository.save(any(Calculation.class))).thenReturn(calculation);

        Calculation saveCalculation = calculationService.saveCalculation(deposit1, depositAmount, algorithm);

        assertEquals(1, saveCalculation.getId());
    }

    @Test
    void calculateDepositInterest() {
        calculationService = new CalculationServiceImpl(calculationRepository);
        Deposit deposit1 = new Deposit();
        deposit1.setId(1);
        deposit1.setName("test1");
        deposit1.setInterest(0.03);
        deposit1.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit1.setDepositStartDate(LocalDate.parse("2019-01-01"));
        deposit1.setDepositEndDate(LocalDate.parse("2019-01-01").plusYears(2));

        BigDecimal depositAmount = BigDecimal.valueOf(1000);
        CalculationAlgorithm calculationAlgorithm = CalculationAlgorithm.FULLPERIOD;

        BigDecimal interest = calculationService.calculateDepositInterest(depositAmount, deposit1, calculationAlgorithm);

        assertEquals(BigDecimal.valueOf(60.90).setScale(2, RoundingMode.HALF_UP), interest);
    }
}