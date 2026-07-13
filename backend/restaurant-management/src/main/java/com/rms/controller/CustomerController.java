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
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.rms.dto.CustomerRegisterDTO;
import java.util.List;


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
            @ApiResponse(responseCode = "201", description = "Customer Registered Successfully"),
            @ApiResponse(responseCode = "400", description = "Validation Failed or Email Already Exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @PostMapping("/register")
    public ResponseEntity<CustomerDTO> registerCustomer(
            @Valid @RequestBody CustomerRegisterDTO dto) {

        CustomerDTO customerDTO = customerService.registerCustomer(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
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
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {

        CustomerDTO customerDTO = customerService.getCustomerById(id);

        return ResponseEntity.ok(customerDTO);
    }


    @Operation(
            summary = "Get All Customers",
            description = "Fetch all customers from the database"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customers Found")
    })

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        List<CustomerDTO> customers = customerService.getAllCustomers();

        return ResponseEntity.ok(customers);
    }

    @Operation(
            summary = "Update Customer",
            description = "Update customer details by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer Updated Successfully"),
            @ApiResponse(responseCode = "400", description = "Validation Failed or Email Already Exists"),
            @ApiResponse(responseCode = "404", description = "Customer Not Found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRegisterDTO dto) {

        CustomerDTO customerDTO = customerService.updateCustomer(id, dto);

        return ResponseEntity.ok(customerDTO);
    }

    @Operation(
            summary = "Delete Customer",
            description = "Delete customer by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer Deleted Successfully"),
            @ApiResponse(responseCode = "404", description = "Customer Not Found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }
    }


