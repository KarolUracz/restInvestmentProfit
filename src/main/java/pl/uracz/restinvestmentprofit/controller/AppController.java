package pl.uracz.restinvestmentprofit.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.uracz.restinvestmentprofit.dto.*;
import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.exception.IncorrectDateException;
import pl.uracz.restinvestmentprofit.mapper.CalculationMapper;
import pl.uracz.restinvestmentprofit.mapper.DepositMapper;
import pl.uracz.restinvestmentprofit.service.CalculationService;
import pl.uracz.restinvestmentprofit.service.DepositService;

import javax.validation.Valid;
import java.time.LocalDate;
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
    public ResponseEntity<List<DepositDto>> getDeposits() {
        List<DepositDto> depositsDtos = depositService.allDtos();
        HttpHeaders headers = new HttpHeaders();
        headers.set("HttpStatus", "200");
        return ResponseEntity.ok()
                .headers(headers)
                .body(depositsDtos);
    }

    @PostMapping("/investments")
    public ResponseEntity<SavedDepositDto> addDeposit(@Valid @RequestBody DepositAddDto deposit) {
        Deposit save = depositService.save(deposit);
        SavedDepositDto savedDepositDto = depositMapper.fromDeposit(save);
        HttpHeaders headers = new HttpHeaders();
        headers.set("HttpStatus", "204");
        return ResponseEntity.ok()
                .headers(headers)
                .body(savedDepositDto);
    }

    @PostMapping("/investments/{id}/calculations")
    public ResponseEntity<CalculationDto> calculationForDeposit(@PathVariable long id, @RequestParam String depositAmount, @RequestParam String algorithm) throws IncorrectDateException {
        Deposit deposit = depositService.findById(id);
        if (algorithm.equals("TILLNOW") && LocalDate.now().isBefore(deposit.getDepositStartDate())) {
            throw new IncorrectDateException("Cannot make calculation for future deposit");
        }
        Calculation calculation = calculationService.saveCalculation(deposit, depositAmount, algorithm);
        HttpHeaders headers = new HttpHeaders();
        headers.set("HttpStatus", "204");
        CalculationDto calculationDto = calculationMapper.toDto(calculation, deposit);
        return ResponseEntity.ok()
        .headers(headers)
        .body(calculationDto);
    }

    @GetMapping("/investments/{id}/calculations")
    public ResponseEntity<DepositCalculationsDto> getAllCalculationsForDeposit(@PathVariable long id) {
        List<Calculation> allByDepositId = calculationService.findAllByDepositId(id);
        Deposit deposit = depositService.findById(id);
        DepositCalculationsDto depositCalculationsDto = depositMapper.getCalculationsForDeposit(deposit);
        depositCalculationsDto.setCalculationDataDtos(calculationMapper.toCalculationsList(allByDepositId));
        HttpHeaders headers = new HttpHeaders();
        headers.set("HttpStatus", "200");
        return ResponseEntity.ok()
                .headers(headers)
                .body(depositCalculationsDto);
    }
}
