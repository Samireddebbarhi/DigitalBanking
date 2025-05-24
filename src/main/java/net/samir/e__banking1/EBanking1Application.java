
package net.samir.e__banking1;

import net.samir.e__banking1.dtos.BankAccountDTO;
import net.samir.e__banking1.dtos.CurrentBankAccountDTO;
import net.samir.e__banking1.dtos.CustomerDTO;
import net.samir.e__banking1.dtos.SavingBankAccountDTO;
import net.samir.e__banking1.entities.BankAccount;
import net.samir.e__banking1.exceptions.CustomerNotFoundException;
import net.samir.e__banking1.mappers.BankAccountMapperImpl;
import net.samir.e__banking1.repositories.CustomerRepository;
import net.samir.e__banking1.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class EBanking1Application {

    public static void main(String[] args) {
        SpringApplication.run(EBanking1Application.class, args);
    }
        @Bean
        public CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
            return (args) -> {
                Stream.of("Samir", "Eddebbarhi", "zaid Benaalouch").forEach(name -> {
                    CustomerDTO customer = new CustomerDTO();
                    customer.setName(name);
                    customer.setEmail("test" + name + "@gmail.com");
                    bankAccountService.saveCustomer(customer);
                });
                bankAccountService.listCustomers().forEach(customer -> {
                    try {
                        bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                        bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());
                    } catch (CustomerNotFoundException e) {
                        e.printStackTrace();
                    }
                });
               List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
                for (BankAccountDTO bankAccount : bankAccounts) {
                    for (int i = 0; i < 10; i++) {
                        String accountId;
                        if (bankAccount instanceof SavingBankAccountDTO) {
                            accountId = ((SavingBankAccountDTO) bankAccount).getId();
                        } else {
                            accountId = ((CurrentBankAccountDTO) bankAccount).getId();
                        }
                        bankAccountService.credit(accountId, 100 + Math.random() * 10, "Credit");
                        bankAccountService.debit(accountId, 100 + Math.random() * 90, "Debit");
                    }
                }


            };
        }
    }


