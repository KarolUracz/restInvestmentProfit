package pl.uracz.restinvestmentprofit.service;

import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.enums.CalculationAlgorithm;
import pl.uracz.restinvestmentprofit.exception.IncorrectDateException;

import java.math.BigDecimal;
import java.util.List;

public interface CalculationService {
    List<Calculation> findAllByDepositId(long id);
    Calculation saveCalculation(Deposit deposit, String depositAmount, String algorithm) throws IncorrectDateException;
    BigDecimal calculateDepositInterest (BigDecimal depositAmount, Deposit deposit, CalculationAlgorithm algorithm) throws IncorrectDateException;
}
