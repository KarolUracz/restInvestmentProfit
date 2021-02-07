package pl.uracz.restinvestmentprofit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uracz.restinvestmentprofit.entity.Calculation;

import java.util.List;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {
    List<Calculation> findAllByDepositId(long id);
}
