package com.ms.financialcontrol.repositories;

import com.ms.financialcontrol.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {

    boolean existsByName(String name);
}
