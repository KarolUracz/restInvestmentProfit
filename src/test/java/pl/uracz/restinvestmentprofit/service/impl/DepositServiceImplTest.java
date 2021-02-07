package pl.uracz.restinvestmentprofit.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.uracz.restinvestmentprofit.dto.DepositAddDto;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;
import pl.uracz.restinvestmentprofit.mapper.DepositMapper;
import pl.uracz.restinvestmentprofit.repository.DepositRepository;
import pl.uracz.restinvestmentprofit.service.DepositService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class DepositServiceImplTest {

    private DepositRepository depositRepository;
    private DepositService depositService;
    private DepositMapper depositMapper;

    @Test
    void findAll() {
        depositRepository = mock(DepositRepository.class);
        depositService = new DepositServiceImpl(depositRepository, depositMapper);
        List<Deposit> deposits = new ArrayList<>();
        Deposit deposit1 = new Deposit();
        deposit1.setId(1);
        deposit1.setName("test1");
        deposit1.setInterest(0.03);
        deposit1.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit1.setDepositStartDate(LocalDate.now());
        deposit1.setDepositEndDate(LocalDate.now().plusYears(2));
        deposits.add(deposit1);
        Deposit deposit2 = new Deposit();
        deposit2.setId(2);
        deposit2.setName("test2");
        deposit2.setInterest(0.03);
        deposit2.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit2.setDepositStartDate(LocalDate.now());
        deposit2.setDepositEndDate(LocalDate.now().plusYears(2));
        deposits.add(deposit2);

        when(depositRepository.findAll()).thenReturn(deposits);

        List<Deposit> all = depositService.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void save() {
        depositRepository = mock(DepositRepository.class);
        depositMapper = mock(DepositMapper.class);
        depositService =  new DepositServiceImpl(depositRepository, depositMapper);
        DepositAddDto depositAddDto = new DepositAddDto();
        depositAddDto.setName("test");
        depositAddDto.setInterest(0.05);
        depositAddDto.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        depositAddDto.setDepositStartDate(LocalDate.now().toString());
        depositAddDto.setDepositEndDate(LocalDate.now().plusYears(2).toString());

        Deposit deposit = new Deposit();
        deposit.setId(1);
        deposit.setName("test");
        deposit.setInterest(0.05);
        deposit.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit.setDepositStartDate(LocalDate.now());
        deposit.setDepositEndDate(LocalDate.now().plusYears(2));;

        when(depositService.save(any(DepositAddDto.class))).thenReturn(deposit);

        Deposit save = depositService.save(depositAddDto);

        assertEquals(1, save.getId());

    }

    @Test
    void findById() {
        depositRepository = mock(DepositRepository.class);
        depositService =  new DepositServiceImpl(depositRepository, depositMapper);
        Deposit deposit = new Deposit();
        deposit.setId(1);
        deposit.setName("test");
        deposit.setInterest(0.05);
        deposit.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        deposit.setDepositStartDate(LocalDate.now());
        deposit.setDepositEndDate(LocalDate.now().plusYears(2));

        when(depositRepository.getOne(any())).thenReturn(deposit);

        Deposit byId = depositService.findById(1);

        assertNotNull(byId);
    }
}