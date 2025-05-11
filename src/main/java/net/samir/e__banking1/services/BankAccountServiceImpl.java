package net.samir.e__banking1.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.samir.e__banking1.dtos.CurrentBankAccountDTO;
import net.samir.e__banking1.dtos.CustomerDTO;
import net.samir.e__banking1.dtos.SavingBankAccountDTO;
import net.samir.e__banking1.entities.CurrentAccount;
import net.samir.e__banking1.entities.Customer;
import net.samir.e__banking1.entities.SavingAccount;
import net.samir.e__banking1.exceptions.CustomerNotFoundException;
import net.samir.e__banking1.mappers.BankAccountMapperImpl;
import net.samir.e__banking1.repositories.AccountOperationRepository;
import net.samir.e__banking1.repositories.BankAccountRepository;
import net.samir.e__banking1.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


@Service
@Transactional
@AllArgsConstructor
@NoArgsConstructor
@Slf4j


public abstract class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

@Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO){
    log.info("Saving new Customer");
    Customer customer =dtoMapper.fromCustomerDTO(customerDTO);
    Customer savedCustomer = customerRepository.save(customer);
    return dtoMapper.fromCustomer(savedCustomer);

}
@Override
public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
        throws CustomerNotFoundException {
    Customer customer=customerRepository.findById(customerId).orElse(null);
    if(customer==null)
        throw new CustomerNotFoundException("Customer not found");
    CurrentAccount currentAccount=new CurrentAccount();
    currentAccount.setId(UUID.randomUUID().toString());
    currentAccount.setCreatedAt(new Date());
    currentAccount.setOverDraft(overDraft);
    currentAccount.setBalance(initialBalance);
    currentAccount.setCustomer(customer);
    CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
    return dtoMapper.fromCurrentBankAccount(savedBankAccount);
}
    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
            throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        savingAccount.setBalance(initialBalance);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

}
