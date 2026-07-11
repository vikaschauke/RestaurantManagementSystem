package com.rms.service;

import com.rms.entity.Customer;
import com.rms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.rms.exception.EmailAlreadyExistsException;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer registerCustomer(Customer customer) {


        Optional<Customer> existingCustomer =
                customerRepository.findByEmail(customer.getEmail());

        if (existingCustomer.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        return customerRepository.save(customer);


    }
}