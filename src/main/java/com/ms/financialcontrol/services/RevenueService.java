package com.ms.financialcontrol.services;

import com.ms.financialcontrol.exceptions.CategoryNotFoundException;
import com.ms.financialcontrol.models.RevenueModel;
import com.ms.financialcontrol.repositories.RevenueRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RevenueService {

    private final RevenueRepository revenueRepository;
    private final CategoryService categoryService;

    public boolean existsById(UUID id) { return revenueRepository.existsById(id); }

    @Transactional
    public Object saveRevenue(RevenueModel revenueModel) {
        if(!categoryService.existsById(revenueModel.getCategory().getId())) {
            throw new CategoryNotFoundException(revenueModel.getCategory().getId());
        }

        return revenueRepository.save(revenueModel);
    }

    public Page<RevenueModel> findAll(Pageable pageable) {
        return revenueRepository.findAll(pageable);
    }

    public Optional<RevenueModel> findById(UUID id) {
        return revenueRepository.findById(id);
    }

    @Transactional
    public void delete(RevenueModel revenueModel) {
        revenueRepository.delete(revenueModel);
    }
}
