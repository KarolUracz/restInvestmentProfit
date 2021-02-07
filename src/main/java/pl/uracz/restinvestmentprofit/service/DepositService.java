package pl.uracz.restinvestmentprofit.service;

import pl.uracz.restinvestmentprofit.dto.DepositAddDto;
import pl.uracz.restinvestmentprofit.dto.DepositDto;
import pl.uracz.restinvestmentprofit.entity.Deposit;

import java.util.List;

public interface DepositService {
    List<Deposit> findAll();
    List<DepositDto> allDtos();
    Deposit save (DepositAddDto depositAddDto);

    Deposit findById(long id);
}
