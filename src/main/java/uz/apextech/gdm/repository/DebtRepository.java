package uz.apextech.gdm.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.apextech.gdm.domain.Debt;

/**
 * Spring Data SQL repository for the Debt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DebtRepository extends JpaRepository<Debt, Long>, JpaSpecificationExecutor<Debt> {}
