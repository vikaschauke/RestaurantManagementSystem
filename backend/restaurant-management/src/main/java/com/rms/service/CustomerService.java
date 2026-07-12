package com.rms.service;

import com.rms.dto.CustomerDTO;
import com.rms.dto.CustomerRegisterDTO;
import com.rms.entity.Customer;
import com.rms.exception.EmailAlreadyExistsException;
import com.rms.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Register Customer (DTO -> Entity -> DTO)
    public CustomerDTO registerCustomer(CustomerRegisterDTO dto) {

        Optional<Customer> existingCustomer =
                customerRepository.findByEmail(dto.getEmail());

        if (existingCustomer.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // DTO -> Entity
        Customer customer = modelMapper.map(dto, Customer.class);

        Customer savedCustomer = customerRepository.save(customer);

        // Entity -> DTO
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    // Get Customer By ID (Entity -> DTO)
    public CustomerDTO getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        return modelMapper.map(customer, CustomerDTO.class);
    }
}