package com.rms.controller;

import com.rms.dto.DashboardResponseDTO;
import com.rms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard() {

        DashboardResponseDTO dashboard =
                dashboardService.getDashboardData();

        return ResponseEntity.ok(dashboard);
    }
}