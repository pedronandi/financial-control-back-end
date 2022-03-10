package com.ms.financialcontrol.repositories;

import com.ms.financialcontrol.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {

    boolean existsByName(String name);
}
