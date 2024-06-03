package com.bu.credit.backend.repository;

import com.bu.credit.backend.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
  @Query("SELECT d FROM Debt d WHERE d.creditorName LIKE %:creditorName%")
  List<Debt> findDebtByCreditorName(@Param("creditorName") String creditorName);

  List<Debt> findDebtByStatus(String status);
}
