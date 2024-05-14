package com.remind.api.takingMedicine.controller;

import com.remind.api.takingMedicine.service.TakingMedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TakingMedicineController {
    private final TakingMedicineService takingMedicineService;



}
