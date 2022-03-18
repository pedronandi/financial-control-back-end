package com.ms.financialcontrol.repositories;

import com.ms.financialcontrol.models.ExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseModel, UUID> {

    boolean existsById(UUID id);
}
