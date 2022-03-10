package com.ms.financialcontrol.controllers;

import com.ms.financialcontrol.dtos.RevenueDto;
import com.ms.financialcontrol.mappers.RevenueMapper;
import com.ms.financialcontrol.models.RevenueModel;
import com.ms.financialcontrol.services.RevenueService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RequestMapping("/revenue")
public class RevenueController {

    private final RevenueMapper revenueMapper;
    private final RevenueService revenueService;
    private final Logger logger = LoggerFactory.getLogger(RevenueController.class);

    @PostMapping
    public ResponseEntity<Object> saveRevenue(@RequestBody @Valid RevenueDto revenueDto) {
        RevenueModel revenueModel = revenueMapper.toModel(revenueDto);
        logger.debug("revenueModel mapped");
        return ResponseEntity.status(HttpStatus.CREATED).body(revenueService.saveRevenue(revenueModel));
    }

    @GetMapping
    public ResponseEntity<Page<RevenueModel>> getAllRevenues(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(revenueService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRevenueById(@PathVariable(value = "id") UUID id) {
        Optional<RevenueModel> revenueOptional = revenueService.findById(id);

        if(!revenueOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Revenue id %s not found!", id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(revenueOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRevenue(@PathVariable(value = "id") UUID id, @RequestBody @Valid RevenueDto revenueDto) {
        Optional<RevenueModel> revenueOptional = revenueService.findById(id);

        if(!revenueOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Revenue id %s not found!", id));
        }

        RevenueModel revenueModel = revenueMapper.toModel(revenueDto);
        revenueModel.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(revenueService.saveRevenue(revenueModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRevenue(@PathVariable(value = "id") UUID id) {
        Optional<RevenueModel> revenueOptional = revenueService.findById(id);

        if(!revenueOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Revenue id %s not found!", id));
        }

        revenueService.delete(revenueOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Revenue id %s deleted successfully!", id));
    }
}
