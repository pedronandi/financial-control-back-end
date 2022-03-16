package com.ms.financialcontrol.controllers;

import com.ms.financialcontrol.dtos.RevenueDto;
import com.ms.financialcontrol.exceptions.RevenueNotFoundException;
import com.ms.financialcontrol.mappers.RevenueMapper;
import com.ms.financialcontrol.models.RevenueModel;
import com.ms.financialcontrol.services.RevenueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@AllArgsConstructor
@RequestMapping("/revenue")
public class RevenueController {

    private final RevenueMapper revenueMapper;
    private final RevenueService revenueService;
    private static final String REVENUE_FOUND = "revenue {} found";

    @PostMapping
    public ResponseEntity<Object> saveRevenue(@RequestBody @Valid RevenueDto revenueDto) {
        RevenueModel revenueModel = revenueMapper.toModel(revenueDto);
        log.debug("revenueModel mapped");

        return ResponseEntity.status(HttpStatus.CREATED).body(revenueService.saveRevenue(revenueModel));
    }

    @GetMapping
    public ResponseEntity<Page<RevenueModel>> getAllRevenues(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(revenueService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRevenueById(@PathVariable(value = "id") UUID id) {
        RevenueModel revenueRequest = revenueService.findById(id).orElseThrow(() -> new RevenueNotFoundException(id));
        log.debug(REVENUE_FOUND, revenueRequest.getId());

        return ResponseEntity.status(HttpStatus.OK).body(revenueRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRevenue(@PathVariable(value = "id") UUID id, @RequestBody @Valid RevenueDto revenueDto) {
        if(!revenueService.existsById(id)) {
            throw new RevenueNotFoundException(id);
        }

        log.debug(REVENUE_FOUND, id);

        RevenueModel revenueModel = revenueMapper.toModel(revenueDto);
        log.debug("revenueModel mapped");

        revenueModel.setId(id);

        return ResponseEntity.status(HttpStatus.OK).body(revenueService.saveRevenue(revenueModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRevenue(@PathVariable(value = "id") UUID id) {
        RevenueModel revenueModel = revenueService.findById(id).orElseThrow(() -> new RevenueNotFoundException(id));
        log.debug(REVENUE_FOUND, revenueModel.getId());

        revenueService.delete(revenueModel);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("Revenue id %s deleted successfully!", id));
    }
}
