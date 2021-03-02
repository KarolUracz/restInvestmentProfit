package pl.uracz.restinvestmentprofit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.C;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.uracz.restinvestmentprofit.dto.*;
import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;
import pl.uracz.restinvestmentprofit.enums.CalculationAlgorithm;
import pl.uracz.restinvestmentprofit.enums.CapitalizationPeriod;
import pl.uracz.restinvestmentprofit.exception.CustomGlobalExceptionHandler;
import pl.uracz.restinvestmentprofit.mapper.CalculationMapper;
import pl.uracz.restinvestmentprofit.mapper.DepositMapper;
import pl.uracz.restinvestmentprofit.service.CalculationService;
import pl.uracz.restinvestmentprofit.service.DepositService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
        DepositDto depositDto1 = new DepositDto();
        depositDto1.setId(1L);
        depositDto1.setName("test1");
        DepositDto depositDto2 = new DepositDto();
        depositDto2.setId(2L);
        depositDto2.setName("test2");
        List<DepositDto> dtoList = Arrays.asList(depositDto1, depositDto2);

        given(depositService.allDtos()).willReturn(dtoList);

        MockHttpServletResponse response = mockMvc.perform(get("/api/investments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String contentAsString = response.getContentAsString();
        assertThat(contentAsString.contains(depositDto1.getName()));


    }

    @Test
    void it_should_return_created_deposit() throws Exception {
        DepositAddDto depositAddDto = new DepositAddDto();
        depositAddDto.setName("test");
        depositAddDto.setInterest(0.05d);
        depositAddDto.setCapitalizationPeriod("YEARLY");
        depositAddDto.setDepositStartDate(LocalDate.now().toString());
        depositAddDto.setDepositEndDate(LocalDate.now().plusYears(1).toString());

        Deposit deposit = new Deposit();
        deposit.setId(1L);
        deposit.setName(depositAddDto.getName());
        deposit.setInterest(depositAddDto.getInterest());
        deposit.setCapitalizationPeriod(CapitalizationPeriod.valueOf(depositAddDto.getCapitalizationPeriod()));
        deposit.setDepositStartDate(LocalDate.parse(depositAddDto.getDepositStartDate()));
        deposit.setDepositEndDate(LocalDate.parse(depositAddDto.getDepositEndDate()));

        SavedDepositDto savedDepositDto = new SavedDepositDto();
        savedDepositDto.setId(1);
        savedDepositDto.setDepositName(depositAddDto.getName());
        savedDepositDto.setDepositInterest(depositAddDto.getInterest());
        savedDepositDto.setDepositDurationInDays(360);

        given(depositService.save(depositAddDto)).willReturn(deposit);
        given(depositMapper.fromDeposit(any(Deposit.class))).willReturn(savedDepositDto);

        MvcResult result = mockMvc.perform(post("/api/investments")
                .content(mapper.writeValueAsString(depositAddDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
//                .andExpect(jsonPath("$.depositName").value(savedDepositDto.getDepositName()))
                .andReturn();

        assertThat(result.getResponse().getContentAsString().contains("test"));

    }

    @Test
    void calculationForDeposit() throws Exception {
        CalculationInputDataDto calculationInputDataDto = new CalculationInputDataDto();
        calculationInputDataDto.setDepositAmount("1000");
        calculationInputDataDto.setAlgorithm("FULLPERIOD");
        mockMvc.perform(post("/api/investments/{id}/calculations", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(calculationInputDataDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllCalculationsForDeposit() throws Exception {
        Deposit deposit = new Deposit(1, "test", 0.05d, CapitalizationPeriod.YEARLY, LocalDate.parse("2019-01-01"), LocalDate.parse("2021-01-01"));
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
    }
}