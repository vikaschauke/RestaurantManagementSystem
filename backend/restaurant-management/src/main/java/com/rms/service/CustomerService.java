package com.rms.service;

import com.rms.dto.CustomerDTO;
import com.rms.dto.CustomerRegisterDTO;
import com.rms.entity.Customer;
import com.rms.exception.EmailAlreadyExistsException;
import com.rms.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//pagination
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.rms.dto.CustomerLoginDTO;

import com.rms.exception.InvalidCredentialsException;

import org.springframework.security.crypto.password.PasswordEncoder; //Password Encoder

import com.rms.dto.LoginResponseDTO;
import com.rms.security.JwtUtil;

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

        customer.setPassword(passwordEncoder.encode(dto.getPassword())); //Password Encodar

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

    // Get Customers with Pagination
    public Page<CustomerDTO> getCustomersWithPagination(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        return customerPage.map(customer ->
                modelMapper.map(customer, CustomerDTO.class));
    }

    // Get Customers with Sorting
    public List<CustomerDTO> getCustomersWithSorting(String field, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        List<Customer> customers = customerRepository.findAll(sort);

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    // Search Customer By Name
    public List<CustomerDTO> searchCustomersByName(String name) {

        List<Customer> customers =
                customerRepository.findByFullNameContainingIgnoreCase(name);

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .toList();
    }

    // Find Customer By Email using @Query
    public CustomerDTO findCustomerByEmail(String email) {

        Customer customer = customerRepository.findCustomerByEmail(email);

        if (customer == null) {
            throw new NoSuchElementException("Customer not found");
        }

        return modelMapper.map(customer, CustomerDTO.class);
    }

    // Login Customer
    public LoginResponseDTO loginCustomer(CustomerLoginDTO dto) {

        Customer customer = customerRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), customer.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);

        String token = jwtUtil.generateToken(customer.getEmail());

        return new LoginResponseDTO(token, customerDTO);
    }
    @Autowired //Password Encoder
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


}