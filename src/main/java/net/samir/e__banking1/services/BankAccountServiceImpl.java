package net.samir.e__banking1.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.samir.e__banking1.dtos.BankAccountDTO;
import net.samir.e__banking1.dtos.CurrentBankAccountDTO;
import net.samir.e__banking1.dtos.CustomerDTO;
import net.samir.e__banking1.dtos.SavingBankAccountDTO;
import net.samir.e__banking1.entities.*;
import net.samir.e__banking1.enums.OperationType;
import net.samir.e__banking1.exceptions.BalanceNotSufficientException;
import net.samir.e__banking1.exceptions.BankAccountNotFoundException;
import net.samir.e__banking1.exceptions.CustomerNotFoundException;
import net.samir.e__banking1.mappers.BankAccountMapperImpl;
import net.samir.e__banking1.repositories.AccountOperationRepository;
import net.samir.e__banking1.repositories.BankAccountRepository;
import net.samir.e__banking1.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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
    @Override
    public List<CustomerDTO> listCustomers() {

    List<Customer> customers = (List<Customer>) customerRepository.findAll();
    List<CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
    return customerDTOS;
}
@Override
public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
    BankAccount bankAccount=bankAccountRepository.findById(accountId)
            .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    if(bankAccount instanceof SavingAccount){
        SavingAccount savingAccount= (SavingAccount) bankAccount;
        return dtoMapper.fromSavingBankAccount(savingAccount);
    } else {
        CurrentAccount currentAccount= (CurrentAccount) bankAccount;
        return dtoMapper.fromCurrentBankAccount(currentAccount);
    }
}
@Override
    public void debit(String AccountId,double amount, String Description) throws BankAccountNotFoundException , BalanceNotSufficientException {
BankAccount bankAccount =bankAccountRepository.findById(AccountId).orElseThrow(()-> new BankAccountNotFoundException("BankAccountNotfound") );
if(bankAccount.getBalance()<amount){
    throw new BalanceNotSufficientException("Balance not sufficient");
}
    AccountOperation accountOperation=new AccountOperation();
accountOperation.setDescription(Description);
accountOperation.setAmount(amount);
accountOperation.setType(
        OperationType.Debit
);
accountOperation.setOperationDate(new Date());
accountOperation.setBankAccount(bankAccount);
accountOperationRepository.save(accountOperation);
bankAccount.setBalance(bankAccount.getBalance()-amount);
bankAccountRepository.save(bankAccount);

}


@Override
public void credit(String AccountId,double amount, String Description) throws BankAccountNotFoundException  {
    BankAccount bankAccount =bankAccountRepository.findById(AccountId).orElseThrow(()-> new BankAccountNotFoundException("BankAccountNotfound") );
    AccountOperation accountOperation=new AccountOperation();
    accountOperation.setDescription(Description);
    accountOperation.setAmount(amount);
    accountOperation.setType(
            OperationType.Credit
    );
    accountOperation.setOperationDate(new Date());
    accountOperation.setBankAccount(bankAccount);
    accountOperationRepository.save(accountOperation);
    bankAccount.setBalance(bankAccount.getBalance()-amount);
    bankAccountRepository.save(bankAccount);
}

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws
            BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
    }

@Override
public List<BankAccountDTO> bankAccountList(){
    List<BankAccount> bankAccounts = bankAccountRepository.findAll();
    List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount-> {
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }).collect(Collectors.toList());
    return bankAccountDTOS;
}
@Override
public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
    return dtoMapper.fromCustomer(customer);
}
@Override
public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
    log.info("Saving new Customer");
    Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
    Customer savedCustomer = customerRepository.save(customer);
    return dtoMapper.fromCustomer(savedCustomer);
}


}