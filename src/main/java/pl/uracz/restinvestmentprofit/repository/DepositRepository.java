package pl.uracz.restinvestmentprofit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uracz.restinvestmentprofit.entity.Deposit;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
