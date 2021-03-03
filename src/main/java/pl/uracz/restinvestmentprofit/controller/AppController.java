package pl.uracz.restinvestmentprofit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.uracz.restinvestmentprofit.dto.*;
import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.mapper.CalculationMapper;
import pl.uracz.restinvestmentprofit.mapper.DepositMapper;
import pl.uracz.restinvestmentprofit.service.CalculationService;
import pl.uracz.restinvestmentprofit.service.DepositService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    private final DepositService depositService;
    private final CalculationService calculationService;
    private final CalculationMapper calculationMapper;
    private final DepositMapper depositMapper;

    public AppController(DepositService depositService, CalculationService calculationService, CalculationMapper calculationMapper, DepositMapper depositMapper) {
        this.depositService = depositService;
        this.calculationService = calculationService;
        this.calculationMapper = calculationMapper;
        this.depositMapper = depositMapper;
    }

    @GetMapping("/investments")
    @ResponseStatus(HttpStatus.OK)
    public List<DepositDto> getDeposits() {
        return depositService.allDtos();
    }

    @PostMapping("/investments")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SavedDepositDto addDeposit(@Valid @RequestBody DepositAddDto deposit) {
        Deposit save = depositService.save(deposit);
        SavedDepositDto savedDepositDto = depositMapper.fromDeposit(save);
        return savedDepositDto;
    }

    @PostMapping("/investments/{id}/calculations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CalculationDto calculationForDeposit(@PathVariable long id, @RequestBody CalculationInputDataDto calculationInputDataDto){
        Deposit deposit = depositService.findById(id);
        Calculation calculation = calculationService.saveCalculation(deposit, calculationInputDataDto.getDepositAmount(), calculationInputDataDto.getAlgorithm());
        CalculationDto calculationDto = calculationMapper.toDto(calculation, deposit);
        return calculationDto;
    }

    @GetMapping("/investments/{id}/calculations")
    @ResponseStatus(HttpStatus.OK)
    public DepositCalculationsDto getAllCalculationsForDeposit(@PathVariable long id) {
        List<Calculation> allByDepositId = calculationService.findAllByDepositId(id);
        Deposit deposit = depositService.findById(id);
        DepositCalculationsDto depositCalculationsDto = depositMapper.getCalculationsForDeposit(deposit);
        depositCalculationsDto.setCalculationDataDtos(calculationMapper.toCalculationsList(allByDepositId));
        return depositCalculationsDto;
    }
}
