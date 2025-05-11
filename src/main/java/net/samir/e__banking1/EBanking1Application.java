package net.samir.e__banking1;

import net.samir.e__banking1.dtos.CustomerDTO;
import net.samir.e__banking1.entities.BankAccount;
import net.samir.e__banking1.repositories.CustomerRepository;
import net.samir.e__banking1.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class EBanking1Application {

    public static void main(String[] args) {
        SpringApplication.run(EBanking1Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(BankAccountService bankAccountService) {
        return (args) -> {
            Stream.of("Samir", "Eddebbarhi", "said").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail("samir" + name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });


        };
    }
}