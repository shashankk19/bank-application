package com.microservices.learning.service.impl;

import com.microservices.learning.constants.AccountsConstants;
import com.microservices.learning.dto.CustomerDto;
import com.microservices.learning.entity.Accounts;
import com.microservices.learning.entity.Customer;
import com.microservices.learning.exception.CustomerAlreadyExistsException;
import com.microservices.learning.mapper.CustomerMapper;
import com.microservices.learning.repository.AccountRepository;
import com.microservices.learning.repository.CustomerRepository;
import com.microservices.learning.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private CustomerRepository customerRepository;

    private AccountRepository accountRepository;

    /**
     *
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {

        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        Optional<Customer> optionalCustomer= customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already exists with given mobile number"+ customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer cutomer){
        Accounts account = new Accounts();
        account.setCustomerId(cutomer.getCustomerId());
        long randomAccountNumber = 1000000000L+ new Random().nextInt(900000000);
        account.setAccountNumber(randomAccountNumber);
        account.setAccountType(AccountsConstants.SAVINGS);
        account.setBranchAddress(AccountsConstants.ADDRESS);
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy("Anonymous");
        return account;
    }
}
