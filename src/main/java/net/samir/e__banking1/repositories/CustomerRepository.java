package net.samir.e__banking1.repositories;

import net.samir.e__banking1.entities.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
