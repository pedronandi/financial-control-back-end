package com.ms.financialcontrol.repositories;

import com.ms.financialcontrol.models.RevenueModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueModel, Long> {
}
