package pl.uracz.restinvestmentprofit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.uracz.restinvestmentprofit.dto.CalculationDataDto;
import pl.uracz.restinvestmentprofit.dto.DepositAddDto;
import pl.uracz.restinvestmentprofit.dto.DepositCalculationsDto;
import pl.uracz.restinvestmentprofit.dto.SavedDepositDto;
import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.enums.CalculationAlgorithm;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;
import pl.uracz.restinvestmentprofit.mapper.CalculationMapper;
import pl.uracz.restinvestmentprofit.mapper.DepositMapper;
import pl.uracz.restinvestmentprofit.service.CalculationService;
import pl.uracz.restinvestmentprofit.service.DepositService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepositService depositService;
    @MockBean
    private CalculationService calculationService;
    @MockBean
    private CalculationMapper calculationMapper;
    @MockBean
    private DepositMapper depositMapper;

    ObjectMapper mapper = new ObjectMapper();


    @Test
    void shouldReturnGetDepositsResponseCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/investments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void it_should_return_created_deposit() throws Exception {
        DepositAddDto depositAddDto = new DepositAddDto();
        depositAddDto.setName("test");
        depositAddDto.setInterest(0.05d);
        depositAddDto.setCapitalizationPeriod(CapitalizationPeriod.ROK);
        depositAddDto.setDepositStartDate(LocalDate.now().toString());
        depositAddDto.setDepositEndDate(LocalDate.now().plusYears(1).toString());

        SavedDepositDto savedDepositDto = new SavedDepositDto();
        savedDepositDto.setId(1);
        savedDepositDto.setDepositName(depositAddDto.getName());
        savedDepositDto.setDepositInterest(depositAddDto.getInterest());
        savedDepositDto.setDepositDurationInDays(360);

        MvcResult result = mockMvc.perform(post("/api/investments")
                .content(mapper.writeValueAsString(depositAddDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String httpStatus = result.getResponse().getHeader("HttpStatus");
        Assert.assertEquals("204", httpStatus);

    }

    @Test
    void calculationForDeposit() throws Exception {
        mockMvc.perform(post("/api/investments/{id}/calculations", 1)
                .param("depositAmount", "1000")
                .param("algorithm", "FULLPERIOD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllCalculationsForDeposit() throws Exception {
        Deposit deposit = new Deposit(1, "test", 0.05d, CapitalizationPeriod.ROK, LocalDate.parse("2019-01-01"), LocalDate.parse("2021-01-01"));
        Calculation calculation1 = new Calculation(1, BigDecimal.valueOf(1000), LocalDate.parse("2020-10-01"), deposit, CalculationAlgorithm.FULLPERIOD, BigDecimal.valueOf(300));
        Calculation calculation2 = new Calculation(2, BigDecimal.valueOf(2000), LocalDate.parse("2020-12-01"), deposit, CalculationAlgorithm.FULLPERIOD, BigDecimal.valueOf(300));
        List<Calculation> calculations = Arrays.asList(calculation1, calculation2);
        when(calculationService.findAllByDepositId(1)).thenReturn(calculations);

        when(depositService.findById(1)).thenReturn(deposit);

        CalculationDataDto calculationDataDto1 = new CalculationDataDto("1000", "2020-10-01", "FULLPERIOD", "300");
        CalculationDataDto calculationDataDto2 = new CalculationDataDto("2000", "2020-12-01", "FULLPERIOD", "300");
        List<CalculationDataDto> calculationDataDtos = Arrays.asList(calculationDataDto1, calculationDataDto2);

        DepositCalculationsDto depositCalculationsDto = new DepositCalculationsDto(deposit.getName(), deposit.getInterest(),
                deposit.getCapitalizationPeriod(), deposit.getDepositStartDate(), deposit.getDepositEndDate(), calculationDataDtos);

        when(depositMapper.getCalculationsForDeposit(deposit)).thenReturn(depositCalculationsDto);
        when(calculationMapper.toCalculationsList(calculations)).thenReturn(calculationDataDtos);

        MvcResult result = mockMvc.perform(get("/api/investments/{id}/calculations", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String httpStatus = result.getResponse().getHeader("HttpStatus");
        Assert.assertEquals("200", httpStatus);
    }
}