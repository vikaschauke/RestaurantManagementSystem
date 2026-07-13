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

import java.util.List;
import java.util.stream.Collectors;

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

    // Get All Customers
    public List<CustomerDTO> getAllCustomers() {

        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    // Update Customer
    public CustomerDTO updateCustomer(Long id, CustomerRegisterDTO dto) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        // Duplicate email check
        Optional<Customer> existingCustomer =
                customerRepository.findByEmail(dto.getEmail());

        if (existingCustomer.isPresent()
                && !existingCustomer.get().getId().equals(id)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setPassword(dto.getPassword());

        Customer updatedCustomer = customerRepository.save(customer);

        return modelMapper.map(updatedCustomer, CustomerDTO.class);
    }

    // Delete Customer
    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        customerRepository.delete(customer);
    }
}