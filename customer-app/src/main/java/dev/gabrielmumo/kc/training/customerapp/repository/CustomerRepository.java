package dev.gabrielmumo.kc.training.customerapp.repository;

import dev.gabrielmumo.kc.training.customerapp.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
