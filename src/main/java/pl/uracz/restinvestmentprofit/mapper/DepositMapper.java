package pl.uracz.restinvestmentprofit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.uracz.restinvestmentprofit.dto.DepositAddDto;
import pl.uracz.restinvestmentprofit.dto.DepositCalculationsDto;
import pl.uracz.restinvestmentprofit.dto.DepositDto;
import pl.uracz.restinvestmentprofit.dto.SavedDepositDto;
import pl.uracz.restinvestmentprofit.entity.Deposit;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepositMapper {

    List<DepositDto> toDto (List<Deposit> deposits);

    @Mapping(target = "id", ignore = true)
    Deposit fromDto (DepositAddDto depositAddDto);

    @Mapping(target = "depositName", source = "deposit.name")
    @Mapping(target = "depositInterest", source = "deposit.interest")
    @Mapping(target = "depositDurationInDays", expression = "java(java.time.temporal.ChronoUnit.DAYS.between(deposit.getDepositStartDate(), deposit.getDepositEndDate()))")
    SavedDepositDto fromDeposit (Deposit deposit);

    @Mapping(target = "calculationDataDtos", ignore = true)
    @Mapping(target = "depositName", source = "deposit.name")
    @Mapping(target = "depositInterest", source = "deposit.interest")
    DepositCalculationsDto getCalculationsForDeposit (Deposit deposit);
}
