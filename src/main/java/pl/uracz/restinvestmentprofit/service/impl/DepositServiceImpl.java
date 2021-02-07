package pl.uracz.restinvestmentprofit.service.impl;

import org.springframework.stereotype.Service;
import pl.uracz.restinvestmentprofit.dto.DepositAddDto;
import pl.uracz.restinvestmentprofit.dto.DepositDto;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.mapper.DepositMapper;
import pl.uracz.restinvestmentprofit.repository.DepositRepository;
import pl.uracz.restinvestmentprofit.service.DepositService;

import java.util.List;

@Service
public class DepositServiceImpl implements DepositService {

    private DepositRepository depositRepository;
    private DepositMapper depositMapper;

    public DepositServiceImpl(DepositRepository depositRepository, DepositMapper depositMapper) {
        this.depositRepository = depositRepository;
        this.depositMapper = depositMapper;
    }

    @Override
    public List<Deposit> findAll() {
        return depositRepository.findAll();
    }

    @Override
    public List<DepositDto> allDtos() {
        return depositMapper.toDto(findAll());
    }

    @Override
    public Deposit save(DepositAddDto depositAddDto) {
        Deposit deposit = depositMapper.fromDto(depositAddDto);
        return depositRepository.save(deposit);
    }

    @Override
    public Deposit findById(long id) {
        return depositRepository.getOne(id);
    }
}
