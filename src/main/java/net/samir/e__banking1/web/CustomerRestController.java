package net.samir.e__banking1.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.samir.e__banking1.dtos.CustomerDTO;
import net.samir.e__banking1.entities.Customer;
import net.samir.e__banking1.repositories.CustomerRepository;
import net.samir.e__banking1.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j


public class CustomerRestController {
    private final CustomerRepository customerRepository;
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> getCustomers() {

        return bankAccountService.listCustomers();
    }
}
