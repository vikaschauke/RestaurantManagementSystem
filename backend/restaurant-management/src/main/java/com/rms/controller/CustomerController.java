package com.rms.controller;

import com.rms.entity.Customer;
import com.rms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation; //Swagger online web page per api hit karne ke liye
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.rms.dto.CustomerDTO;  //Security perpose
import com.rms.dto.CustomerDTO;
import com.rms.dto.CustomerRegisterDTO;


@Tag(
        name = "Customer Controller",
        description = "APIs for Customer Registration and Management"
)

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(
            summary = "Register Customer",
            description = "Registers a new customer into the database"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer Registered Successfully"),
            @ApiResponse(responseCode = "400", description = "Validation Failed or Email Already Exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @PostMapping("/register")
    public CustomerDTO registerCustomer(
            @Valid @RequestBody CustomerRegisterDTO dto) {

        return customerService.registerCustomer(dto);
    }

    @Operation(
            summary = "Get Customer By ID",
            description = "Fetch customer details by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer Found"),
            @ApiResponse(responseCode = "404", description = "Customer Not Found")
    })
    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }
    }
