package pl.uracz.restinvestmentprofit.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.uracz.restinvestmentprofit.dto.CalculationDataDto;
import pl.uracz.restinvestmentprofit.dto.CalculationDto;
import pl.uracz.restinvestmentprofit.entity.Calculation;
import pl.uracz.restinvestmentprofit.entity.Deposit;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepositMapper.class})
public interface CalculationMapper {

    @Mapping(target = "depositCapitalizationPeriod", source = "deposit.capitalizationPeriod")
    @Mapping(target = "depositInterest", source = "deposit.interest")
    @Mapping(target = "depositName", source = "deposit.name")
    CalculationDto toDto(Calculation calculation, Deposit deposit);

    List<CalculationDataDto> toCalculationsList (List<Calculation> calculations);

}
