package pl.uracz.restinvestmentprofit.service.impl;

import org.springframework.stereotype.Service;
import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.enums.CalculationAlgorithm;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;
import pl.uracz.restinvestmentprofit.exception.IncorrectDateException;
import pl.uracz.restinvestmentprofit.repository.CalculationRepository;
import pl.uracz.restinvestmentprofit.service.CalculationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static pl.uracz.restinvestmentprofit.enums.CalculationAlgorithm.*;

@Service
public class CalculationServiceImpl implements CalculationService {

    private final CalculationRepository calculationRepository;

    public CalculationServiceImpl(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    @Override
    public List<Calculation> findAllByDepositId(long id) {
        return calculationRepository.findAllByDepositId(id);
    }

    @Override
    public Calculation saveCalculation(Deposit deposit, String depositAmount, String algorithm) {
        Calculation calculation = new Calculation();
        calculation.setCalculationDate(LocalDate.now());
        calculation.setCalculationAlgorithm(valueOf(algorithm));
        calculation.setDeposit(deposit);
        calculation.setDepositAmount(new BigDecimal(depositAmount));

        BigDecimal profit = calculateDepositInterest(calculation.getDepositAmount(), deposit, calculation.getCalculationAlgorithm());

        calculation.setProfit(profit);
        Calculation save = calculationRepository.save(calculation);
        return save;
    }

    @Override
    public BigDecimal calculateDepositInterest(BigDecimal depositAmount, Deposit deposit, CalculationAlgorithm algorithm) {
        CapitalizationPeriod capitalizationPeriod = deposit.getCapitalizationPeriod();
        long numberOfMonths = 0;
        switch (algorithm) {
            case FULLPERIOD:
                numberOfMonths = ChronoUnit.MONTHS.between(deposit.getDepositStartDate(), deposit.getDepositEndDate());
                break;
            case TILLNOW:
                if (deposit.getDepositStartDate().isAfter(LocalDate.now())) {
                    throw new IncorrectDateException("Can't make partial calculation for future deposit");
                }
                numberOfMonths = ChronoUnit.MONTHS.between(deposit.getDepositStartDate(), LocalDate.now());
                break;
        }
        int capitalizationPeriodInteger = 1;
        int numberOfCapitalization = 0;
        switch (capitalizationPeriod) {
            case MIESIAC:
                capitalizationPeriodInteger = 12;
                numberOfCapitalization = (int) numberOfMonths;
                break;
            case TRZYMIESIACE:
                capitalizationPeriodInteger = 4;
                numberOfCapitalization = (int) (numberOfMonths/3);
                break;
            case SZESCMIESIECY:
                capitalizationPeriodInteger = 2;
                numberOfCapitalization = (int) (numberOfMonths/6);
                break;
            case ROK:
                capitalizationPeriodInteger = 1;
                numberOfCapitalization = (int) (numberOfMonths/12);
        }
        BigDecimal divide = BigDecimal.valueOf(deposit.getInterest()).divide(BigDecimal.valueOf(capitalizationPeriodInteger));
        BigDecimal inBrackets = divide.add(BigDecimal.ONE);
        BigDecimal power = inBrackets.pow(numberOfCapitalization);
        BigDecimal profit = depositAmount.multiply(power);
        BigDecimal subtract = profit.subtract(depositAmount);
        return subtract.setScale(2, RoundingMode.HALF_UP);
    }
}
